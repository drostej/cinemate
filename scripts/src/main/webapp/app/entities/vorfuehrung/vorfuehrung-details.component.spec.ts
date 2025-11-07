import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import VorfuehrungDetails from './vorfuehrung-details.vue';
import VorfuehrungService from './vorfuehrung.service';
import AlertService from '@/shared/alert/alert.service';

type VorfuehrungDetailsComponentType = InstanceType<typeof VorfuehrungDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const vorfuehrungSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Vorfuehrung Management Detail Component', () => {
    let vorfuehrungServiceStub: SinonStubbedInstance<VorfuehrungService>;
    let mountOptions: MountingOptions<VorfuehrungDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      vorfuehrungServiceStub = sinon.createStubInstance<VorfuehrungService>(VorfuehrungService);

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
          vorfuehrungService: () => vorfuehrungServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        vorfuehrungServiceStub.find.resolves(vorfuehrungSample);
        route = {
          params: {
            vorfuehrungId: `${123}`,
          },
        };
        const wrapper = shallowMount(VorfuehrungDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.vorfuehrung).toMatchObject(vorfuehrungSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        vorfuehrungServiceStub.find.resolves(vorfuehrungSample);
        const wrapper = shallowMount(VorfuehrungDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
