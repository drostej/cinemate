import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import VorfuehrungService from './vorfuehrung.service';
import { useDateFormat } from '@/shared/composables';
import { type IVorfuehrung } from '@/shared/model/vorfuehrung.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'VorfuehrungDetails',
  setup() {
    const dateFormat = useDateFormat();
    const vorfuehrungService = inject('vorfuehrungService', () => new VorfuehrungService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const vorfuehrung: Ref<IVorfuehrung> = ref({});

    const retrieveVorfuehrung = async vorfuehrungId => {
      try {
        const res = await vorfuehrungService().find(vorfuehrungId);
        vorfuehrung.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.vorfuehrungId) {
      retrieveVorfuehrung(route.params.vorfuehrungId);
    }

    return {
      ...dateFormat,
      alertService,
      vorfuehrung,

      previousState,
      t$: useI18n().t,
    };
  },
});
