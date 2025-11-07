import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import ReservierungService from './reservierung.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import SitzplatzService from '@/entities/sitzplatz/sitzplatz.service';
import { type ISitzplatz } from '@/shared/model/sitzplatz.model';
import VorfuehrungService from '@/entities/vorfuehrung/vorfuehrung.service';
import { type IVorfuehrung } from '@/shared/model/vorfuehrung.model';
import { type IReservierung, Reservierung } from '@/shared/model/reservierung.model';
import { ReservierungsStatus } from '@/shared/model/enumerations/reservierungs-status.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ReservierungUpdate',
  setup() {
    const reservierungService = inject('reservierungService', () => new ReservierungService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const reservierung: Ref<IReservierung> = ref(new Reservierung());

    const sitzplatzService = inject('sitzplatzService', () => new SitzplatzService());

    const sitzplatzs: Ref<ISitzplatz[]> = ref([]);

    const vorfuehrungService = inject('vorfuehrungService', () => new VorfuehrungService());

    const vorfuehrungs: Ref<IVorfuehrung[]> = ref([]);
    const reservierungsStatusValues: Ref<string[]> = ref(Object.keys(ReservierungsStatus));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const initRelationships = () => {
      sitzplatzService()
        .retrieve()
        .then(res => {
          sitzplatzs.value = res.data;
        });
      vorfuehrungService()
        .retrieve()
        .then(res => {
          vorfuehrungs.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      kundeName: {
        required: validations.required(t$('entity.validation.required').toString()),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 100 }).toString(), 100),
      },
      status: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      plaetzes: {},
      vorfuehrung: {},
    };
    const v$ = useVuelidate(validationRules, reservierung as any);
    v$.value.$validate();

    return {
      reservierungService,
      alertService,
      reservierung,
      previousState,
      reservierungsStatusValues,
      isSaving,
      currentLanguage,
      sitzplatzs,
      vorfuehrungs,
      v$,
      t$,
    };
  },
  created(): void {
    this.reservierung.plaetzes = [];
  },
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.reservierung.id) {
        this.reservierungService()
          .update(this.reservierung)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('cinebuddyApp.reservierung.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.reservierungService()
          .create(this.reservierung)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('cinebuddyApp.reservierung.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },

    getSelected(selectedVals, option, pkField = 'id'): any {
      if (selectedVals) {
        return selectedVals.find(value => option[pkField] === value[pkField]) ?? option;
      }
      return option;
    },
  },
});
