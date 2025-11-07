import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ReservierungDetails from './reservierung-details.vue';
import ReservierungService from './reservierung.service';
import AlertService from '@/shared/alert/alert.service';

type ReservierungDetailsComponentType = InstanceType<typeof ReservierungDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const reservierungSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Reservierung Management Detail Component', () => {
    let reservierungServiceStub: SinonStubbedInstance<ReservierungService>;
    let mountOptions: MountingOptions<ReservierungDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      reservierungServiceStub = sinon.createStubInstance<ReservierungService>(ReservierungService);

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
          reservierungService: () => reservierungServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        reservierungServiceStub.find.resolves(reservierungSample);
        route = {
          params: {
            reservierungId: `${123}`,
          },
        };
        const wrapper = shallowMount(ReservierungDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.reservierung).toMatchObject(reservierungSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        reservierungServiceStub.find.resolves(reservierungSample);
        const wrapper = shallowMount(ReservierungDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
