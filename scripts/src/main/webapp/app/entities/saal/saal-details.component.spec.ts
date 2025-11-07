import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import SaalDetails from './saal-details.vue';
import SaalService from './saal.service';
import AlertService from '@/shared/alert/alert.service';

type SaalDetailsComponentType = InstanceType<typeof SaalDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const saalSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Saal Management Detail Component', () => {
    let saalServiceStub: SinonStubbedInstance<SaalService>;
    let mountOptions: MountingOptions<SaalDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      saalServiceStub = sinon.createStubInstance<SaalService>(SaalService);

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
          saalService: () => saalServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        saalServiceStub.find.resolves(saalSample);
        route = {
          params: {
            saalId: `${123}`,
          },
        };
        const wrapper = shallowMount(SaalDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.saal).toMatchObject(saalSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        saalServiceStub.find.resolves(saalSample);
        const wrapper = shallowMount(SaalDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
