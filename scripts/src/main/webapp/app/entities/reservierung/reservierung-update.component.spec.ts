import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ReservierungUpdate from './reservierung-update.vue';
import ReservierungService from './reservierung.service';
import AlertService from '@/shared/alert/alert.service';

import SitzplatzService from '@/entities/sitzplatz/sitzplatz.service';
import VorfuehrungService from '@/entities/vorfuehrung/vorfuehrung.service';

type ReservierungUpdateComponentType = InstanceType<typeof ReservierungUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const reservierungSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ReservierungUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Reservierung Management Update Component', () => {
    let comp: ReservierungUpdateComponentType;
    let reservierungServiceStub: SinonStubbedInstance<ReservierungService>;

    beforeEach(() => {
      route = {};
      reservierungServiceStub = sinon.createStubInstance<ReservierungService>(ReservierungService);
      reservierungServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          reservierungService: () => reservierungServiceStub,
          sitzplatzService: () =>
            sinon.createStubInstance<SitzplatzService>(SitzplatzService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          vorfuehrungService: () =>
            sinon.createStubInstance<VorfuehrungService>(VorfuehrungService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(ReservierungUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.reservierung = reservierungSample;
        reservierungServiceStub.update.resolves(reservierungSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(reservierungServiceStub.update.calledWith(reservierungSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        reservierungServiceStub.create.resolves(entity);
        const wrapper = shallowMount(ReservierungUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.reservierung = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(reservierungServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        reservierungServiceStub.find.resolves(reservierungSample);
        reservierungServiceStub.retrieve.resolves([reservierungSample]);

        // WHEN
        route = {
          params: {
            reservierungId: `${reservierungSample.id}`,
          },
        };
        const wrapper = shallowMount(ReservierungUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.reservierung).toMatchObject(reservierungSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        reservierungServiceStub.find.resolves(reservierungSample);
        const wrapper = shallowMount(ReservierungUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
