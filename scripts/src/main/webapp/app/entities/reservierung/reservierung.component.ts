import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import ReservierungService from './reservierung.service';
import { type IReservierung } from '@/shared/model/reservierung.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Reservierung',
  setup() {
    const { t: t$ } = useI18n();
    const reservierungService = inject('reservierungService', () => new ReservierungService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const reservierungs: Ref<IReservierung[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveReservierungs = async () => {
      isFetching.value = true;
      try {
        const res = await reservierungService().retrieve();
        reservierungs.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveReservierungs();
    };

    onMounted(async () => {
      await retrieveReservierungs();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IReservierung) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeReservierung = async () => {
      try {
        await reservierungService().delete(removeId.value);
        const message = t$('cinebuddyApp.reservierung.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveReservierungs();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      reservierungs,
      handleSyncList,
      isFetching,
      retrieveReservierungs,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeReservierung,
      t$,
    };
  },
});
