import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import Vorfuehrung from './vorfuehrung.vue';
import VorfuehrungService from './vorfuehrung.service';
import AlertService from '@/shared/alert/alert.service';

type VorfuehrungComponentType = InstanceType<typeof Vorfuehrung>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Vorfuehrung Management Component', () => {
    let vorfuehrungServiceStub: SinonStubbedInstance<VorfuehrungService>;
    let mountOptions: MountingOptions<VorfuehrungComponentType>['global'];

    beforeEach(() => {
      vorfuehrungServiceStub = sinon.createStubInstance<VorfuehrungService>(VorfuehrungService);
      vorfuehrungServiceStub.retrieve.resolves({ headers: {} });

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
          vorfuehrungService: () => vorfuehrungServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        vorfuehrungServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Vorfuehrung, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(vorfuehrungServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.vorfuehrungs[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: VorfuehrungComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Vorfuehrung, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        vorfuehrungServiceStub.retrieve.reset();
        vorfuehrungServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        vorfuehrungServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeVorfuehrung();
        await comp.$nextTick(); // clear components

        // THEN
        expect(vorfuehrungServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(vorfuehrungServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
