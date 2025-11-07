import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import SitzplatzService from './sitzplatz.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import SaalService from '@/entities/saal/saal.service';
import { type ISaal } from '@/shared/model/saal.model';
import ReservierungService from '@/entities/reservierung/reservierung.service';
import { type IReservierung } from '@/shared/model/reservierung.model';
import { type ISitzplatz, Sitzplatz } from '@/shared/model/sitzplatz.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'SitzplatzUpdate',
  setup() {
    const sitzplatzService = inject('sitzplatzService', () => new SitzplatzService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const sitzplatz: Ref<ISitzplatz> = ref(new Sitzplatz());

    const saalService = inject('saalService', () => new SaalService());

    const saals: Ref<ISaal[]> = ref([]);

    const reservierungService = inject('reservierungService', () => new ReservierungService());

    const reservierungs: Ref<IReservierung[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const initRelationships = () => {
      saalService()
        .retrieve()
        .then(res => {
          saals.value = res.data;
        });
      reservierungService()
        .retrieve()
        .then(res => {
          reservierungs.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      reihe: {
        required: validations.required(t$('entity.validation.required').toString()),
        minLength: validations.minLength(t$('entity.validation.minlength', { min: 1 }).toString(), 1),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 1 }).toString(), 1),
      },
      nummer: {
        required: validations.required(t$('entity.validation.required').toString()),
        integer: validations.integer(t$('entity.validation.number').toString()),
      },
      saal: {},
      reservierungens: {},
    };
    const v$ = useVuelidate(validationRules, sitzplatz as any);
    v$.value.$validate();

    return {
      sitzplatzService,
      alertService,
      sitzplatz,
      previousState,
      isSaving,
      currentLanguage,
      saals,
      reservierungs,
      v$,
      t$,
    };
  },
  created(): void {
    this.sitzplatz.reservierungens = [];
  },
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.sitzplatz.id) {
        this.sitzplatzService()
          .update(this.sitzplatz)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('cinebuddyApp.sitzplatz.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.sitzplatzService()
          .create(this.sitzplatz)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('cinebuddyApp.sitzplatz.created', { param: param.id }).toString());
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
