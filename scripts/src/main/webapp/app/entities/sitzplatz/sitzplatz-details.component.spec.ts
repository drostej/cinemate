import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import SitzplatzDetails from './sitzplatz-details.vue';
import SitzplatzService from './sitzplatz.service';
import AlertService from '@/shared/alert/alert.service';

type SitzplatzDetailsComponentType = InstanceType<typeof SitzplatzDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const sitzplatzSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Sitzplatz Management Detail Component', () => {
    let sitzplatzServiceStub: SinonStubbedInstance<SitzplatzService>;
    let mountOptions: MountingOptions<SitzplatzDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      sitzplatzServiceStub = sinon.createStubInstance<SitzplatzService>(SitzplatzService);

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          sitzplatzService: () => sitzplatzServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        sitzplatzServiceStub.find.resolves(sitzplatzSample);
        route = {
          params: {
            sitzplatzId: `${123}`,
          },
        };
        const wrapper = shallowMount(SitzplatzDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.sitzplatz).toMatchObject(sitzplatzSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        sitzplatzServiceStub.find.resolves(sitzplatzSample);
        const wrapper = shallowMount(SitzplatzDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
