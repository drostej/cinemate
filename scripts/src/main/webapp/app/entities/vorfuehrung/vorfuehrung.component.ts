import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import VorfuehrungService from './vorfuehrung.service';
import { type IVorfuehrung } from '@/shared/model/vorfuehrung.model';
import { useDateFormat } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Vorfuehrung',
  setup() {
    const { t: t$ } = useI18n();
    const dateFormat = useDateFormat();
    const vorfuehrungService = inject('vorfuehrungService', () => new VorfuehrungService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const vorfuehrungs: Ref<IVorfuehrung[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveVorfuehrungs = async () => {
      isFetching.value = true;
      try {
        const res = await vorfuehrungService().retrieve();
        vorfuehrungs.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveVorfuehrungs();
    };

    onMounted(async () => {
      await retrieveVorfuehrungs();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IVorfuehrung) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeVorfuehrung = async () => {
      try {
        await vorfuehrungService().delete(removeId.value);
        const message = t$('cinebuddyApp.vorfuehrung.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveVorfuehrungs();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      vorfuehrungs,
      handleSyncList,
      isFetching,
      retrieveVorfuehrungs,
      clear,
      ...dateFormat,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeVorfuehrung,
      t$,
    };
  },
});
