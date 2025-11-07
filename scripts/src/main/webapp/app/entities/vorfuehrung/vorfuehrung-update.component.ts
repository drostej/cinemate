import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import VorfuehrungService from './vorfuehrung.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import SaalService from '@/entities/saal/saal.service';
import { type ISaal } from '@/shared/model/saal.model';
import { type IVorfuehrung, Vorfuehrung } from '@/shared/model/vorfuehrung.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'VorfuehrungUpdate',
  setup() {
    const vorfuehrungService = inject('vorfuehrungService', () => new VorfuehrungService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const vorfuehrung: Ref<IVorfuehrung> = ref(new Vorfuehrung());

    const saalService = inject('saalService', () => new SaalService());

    const saals: Ref<ISaal[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveVorfuehrung = async vorfuehrungId => {
      try {
        const res = await vorfuehrungService().find(vorfuehrungId);
        res.datumZeit = new Date(res.datumZeit);
        vorfuehrung.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.vorfuehrungId) {
      retrieveVorfuehrung(route.params.vorfuehrungId);
    }

    const initRelationships = () => {
      saalService()
        .retrieve()
        .then(res => {
          saals.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      filmTitel: {
        required: validations.required(t$('entity.validation.required').toString()),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 200 }).toString(), 200),
      },
      datumZeit: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      reservierungens: {},
      saal: {},
    };
    const v$ = useVuelidate(validationRules, vorfuehrung as any);
    v$.value.$validate();

    return {
      vorfuehrungService,
      alertService,
      vorfuehrung,
      previousState,
      isSaving,
      currentLanguage,
      saals,
      v$,
      ...useDateFormat({ entityRef: vorfuehrung }),
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.vorfuehrung.id) {
        this.vorfuehrungService()
          .update(this.vorfuehrung)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('cinebuddyApp.vorfuehrung.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.vorfuehrungService()
          .create(this.vorfuehrung)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('cinebuddyApp.vorfuehrung.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
