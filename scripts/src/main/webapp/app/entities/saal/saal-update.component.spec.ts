import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import SaalUpdate from './saal-update.vue';
import SaalService from './saal.service';
import AlertService from '@/shared/alert/alert.service';

type SaalUpdateComponentType = InstanceType<typeof SaalUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const saalSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<SaalUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Saal Management Update Component', () => {
    let comp: SaalUpdateComponentType;
    let saalServiceStub: SinonStubbedInstance<SaalService>;

    beforeEach(() => {
      route = {};
      saalServiceStub = sinon.createStubInstance<SaalService>(SaalService);
      saalServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          saalService: () => saalServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(SaalUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.saal = saalSample;
        saalServiceStub.update.resolves(saalSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(saalServiceStub.update.calledWith(saalSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        saalServiceStub.create.resolves(entity);
        const wrapper = shallowMount(SaalUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.saal = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(saalServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        saalServiceStub.find.resolves(saalSample);
        saalServiceStub.retrieve.resolves([saalSample]);

        // WHEN
        route = {
          params: {
            saalId: `${saalSample.id}`,
          },
        };
        const wrapper = shallowMount(SaalUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.saal).toMatchObject(saalSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        saalServiceStub.find.resolves(saalSample);
        const wrapper = shallowMount(SaalUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
