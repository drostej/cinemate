import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import Reservierung from './reservierung.vue';
import ReservierungService from './reservierung.service';
import AlertService from '@/shared/alert/alert.service';

type ReservierungComponentType = InstanceType<typeof Reservierung>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Reservierung Management Component', () => {
    let reservierungServiceStub: SinonStubbedInstance<ReservierungService>;
    let mountOptions: MountingOptions<ReservierungComponentType>['global'];

    beforeEach(() => {
      reservierungServiceStub = sinon.createStubInstance<ReservierungService>(ReservierungService);
      reservierungServiceStub.retrieve.resolves({ headers: {} });

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
          reservierungService: () => reservierungServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        reservierungServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Reservierung, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(reservierungServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.reservierungs[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: ReservierungComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Reservierung, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        reservierungServiceStub.retrieve.reset();
        reservierungServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        reservierungServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeReservierung();
        await comp.$nextTick(); // clear components

        // THEN
        expect(reservierungServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(reservierungServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
