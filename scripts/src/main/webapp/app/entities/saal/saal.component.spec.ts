import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import Saal from './saal.vue';
import SaalService from './saal.service';
import AlertService from '@/shared/alert/alert.service';

type SaalComponentType = InstanceType<typeof Saal>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Saal Management Component', () => {
    let saalServiceStub: SinonStubbedInstance<SaalService>;
    let mountOptions: MountingOptions<SaalComponentType>['global'];

    beforeEach(() => {
      saalServiceStub = sinon.createStubInstance<SaalService>(SaalService);
      saalServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          saalService: () => saalServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        saalServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Saal, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(saalServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.saals[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: SaalComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Saal, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        saalServiceStub.retrieve.reset();
        saalServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        saalServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeSaal();
        await comp.$nextTick(); // clear components

        // THEN
        expect(saalServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(saalServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
