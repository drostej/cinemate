import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import SaalService from './saal.service';
import { type ISaal } from '@/shared/model/saal.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'SaalDetails',
  setup() {
    const saalService = inject('saalService', () => new SaalService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const saal: Ref<ISaal> = ref({});

    const retrieveSaal = async saalId => {
      try {
        const res = await saalService().find(saalId);
        saal.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.saalId) {
      retrieveSaal(route.params.saalId);
    }

    return {
      alertService,
      saal,

      previousState,
      t$: useI18n().t,
    };
  },
});
