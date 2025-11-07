import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import SitzplatzUpdate from './sitzplatz-update.vue';
import SitzplatzService from './sitzplatz.service';
import AlertService from '@/shared/alert/alert.service';

import SaalService from '@/entities/saal/saal.service';
import ReservierungService from '@/entities/reservierung/reservierung.service';

type SitzplatzUpdateComponentType = InstanceType<typeof SitzplatzUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const sitzplatzSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<SitzplatzUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Sitzplatz Management Update Component', () => {
    let comp: SitzplatzUpdateComponentType;
    let sitzplatzServiceStub: SinonStubbedInstance<SitzplatzService>;

    beforeEach(() => {
      route = {};
      sitzplatzServiceStub = sinon.createStubInstance<SitzplatzService>(SitzplatzService);
      sitzplatzServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          sitzplatzService: () => sitzplatzServiceStub,
          saalService: () =>
            sinon.createStubInstance<SaalService>(SaalService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          reservierungService: () =>
            sinon.createStubInstance<ReservierungService>(ReservierungService, {
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
        const wrapper = shallowMount(SitzplatzUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.sitzplatz = sitzplatzSample;
        sitzplatzServiceStub.update.resolves(sitzplatzSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(sitzplatzServiceStub.update.calledWith(sitzplatzSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        sitzplatzServiceStub.create.resolves(entity);
        const wrapper = shallowMount(SitzplatzUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.sitzplatz = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(sitzplatzServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        sitzplatzServiceStub.find.resolves(sitzplatzSample);
        sitzplatzServiceStub.retrieve.resolves([sitzplatzSample]);

        // WHEN
        route = {
          params: {
            sitzplatzId: `${sitzplatzSample.id}`,
          },
        };
        const wrapper = shallowMount(SitzplatzUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.sitzplatz).toMatchObject(sitzplatzSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        sitzplatzServiceStub.find.resolves(sitzplatzSample);
        const wrapper = shallowMount(SitzplatzUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
