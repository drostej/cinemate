import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import SitzplatzService from './sitzplatz.service';
import { type ISitzplatz } from '@/shared/model/sitzplatz.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'SitzplatzDetails',
  setup() {
    const sitzplatzService = inject('sitzplatzService', () => new SitzplatzService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const sitzplatz: Ref<ISitzplatz> = ref({});

    const retrieveSitzplatz = async sitzplatzId => {
      try {
        const res = await sitzplatzService().find(sitzplatzId);
        sitzplatz.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.sitzplatzId) {
      retrieveSitzplatz(route.params.sitzplatzId);
    }

    return {
      alertService,
      sitzplatz,

      previousState,
      t$: useI18n().t,
    };
  },
});
