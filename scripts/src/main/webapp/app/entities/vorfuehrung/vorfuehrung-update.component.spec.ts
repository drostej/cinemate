import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import dayjs from 'dayjs';
import VorfuehrungUpdate from './vorfuehrung-update.vue';
import VorfuehrungService from './vorfuehrung.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';
import AlertService from '@/shared/alert/alert.service';

import SaalService from '@/entities/saal/saal.service';

type VorfuehrungUpdateComponentType = InstanceType<typeof VorfuehrungUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const vorfuehrungSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<VorfuehrungUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Vorfuehrung Management Update Component', () => {
    let comp: VorfuehrungUpdateComponentType;
    let vorfuehrungServiceStub: SinonStubbedInstance<VorfuehrungService>;

    beforeEach(() => {
      route = {};
      vorfuehrungServiceStub = sinon.createStubInstance<VorfuehrungService>(VorfuehrungService);
      vorfuehrungServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          vorfuehrungService: () => vorfuehrungServiceStub,
          saalService: () =>
            sinon.createStubInstance<SaalService>(SaalService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('load', () => {
      beforeEach(() => {
        const wrapper = shallowMount(VorfuehrungUpdate, { global: mountOptions });
        comp = wrapper.vm;
      });
      it('Should convert date from string', () => {
        // GIVEN
        const date = new Date('2019-10-15T11:42:02Z');

        // WHEN
        const convertedDate = comp.convertDateTimeFromServer(date);

        // THEN
        expect(convertedDate).toEqual(dayjs(date).format(DATE_TIME_LONG_FORMAT));
      });

      it('Should not convert date if date is not present', () => {
        expect(comp.convertDateTimeFromServer(null)).toBeNull();
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(VorfuehrungUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.vorfuehrung = vorfuehrungSample;
        vorfuehrungServiceStub.update.resolves(vorfuehrungSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(vorfuehrungServiceStub.update.calledWith(vorfuehrungSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        vorfuehrungServiceStub.create.resolves(entity);
        const wrapper = shallowMount(VorfuehrungUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.vorfuehrung = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(vorfuehrungServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        vorfuehrungServiceStub.find.resolves(vorfuehrungSample);
        vorfuehrungServiceStub.retrieve.resolves([vorfuehrungSample]);

        // WHEN
        route = {
          params: {
            vorfuehrungId: `${vorfuehrungSample.id}`,
          },
        };
        const wrapper = shallowMount(VorfuehrungUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.vorfuehrung).toMatchObject(vorfuehrungSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        vorfuehrungServiceStub.find.resolves(vorfuehrungSample);
        const wrapper = shallowMount(VorfuehrungUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
