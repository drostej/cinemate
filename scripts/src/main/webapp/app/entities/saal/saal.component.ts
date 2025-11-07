import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import SaalService from './saal.service';
import { type ISaal } from '@/shared/model/saal.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Saal',
  setup() {
    const { t: t$ } = useI18n();
    const saalService = inject('saalService', () => new SaalService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const saals: Ref<ISaal[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveSaals = async () => {
      isFetching.value = true;
      try {
        const res = await saalService().retrieve();
        saals.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveSaals();
    };

    onMounted(async () => {
      await retrieveSaals();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: ISaal) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeSaal = async () => {
      try {
        await saalService().delete(removeId.value);
        const message = t$('cinebuddyApp.saal.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveSaals();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      saals,
      handleSyncList,
      isFetching,
      retrieveSaals,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeSaal,
      t$,
    };
  },
});
