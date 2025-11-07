import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import ReservierungService from './reservierung.service';
import { type IReservierung } from '@/shared/model/reservierung.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ReservierungDetails',
  setup() {
    const reservierungService = inject('reservierungService', () => new ReservierungService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const reservierung: Ref<IReservierung> = ref({});

    const retrieveReservierung = async reservierungId => {
      try {
        const res = await reservierungService().find(reservierungId);
        reservierung.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.reservierungId) {
      retrieveReservierung(route.params.reservierungId);
    }

    return {
      alertService,
      reservierung,

      previousState,
      t$: useI18n().t,
    };
  },
});
