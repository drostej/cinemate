<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="cinebuddyApp.reservierung.home.createOrEditLabel"
          data-cy="ReservierungCreateUpdateHeading"
          v-text="t$('cinebuddyApp.reservierung.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="reservierung.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="reservierung.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('cinebuddyApp.reservierung.kundeName')" for="reservierung-kundeName"></label>
            <input
              type="text"
              class="form-control"
              name="kundeName"
              id="reservierung-kundeName"
              data-cy="kundeName"
              :class="{ valid: !v$.kundeName.$invalid, invalid: v$.kundeName.$invalid }"
              v-model="v$.kundeName.$model"
              required
            />
            <div v-if="v$.kundeName.$anyDirty && v$.kundeName.$invalid">
              <small class="form-text text-danger" v-for="error of v$.kundeName.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('cinebuddyApp.reservierung.status')" for="reservierung-status"></label>
            <select
              class="form-control"
              name="status"
              :class="{ valid: !v$.status.$invalid, invalid: v$.status.$invalid }"
              v-model="v$.status.$model"
              id="reservierung-status"
              data-cy="status"
              required
            >
              <option
                v-for="reservierungsStatus in reservierungsStatusValues"
                :key="reservierungsStatus"
                :value="reservierungsStatus"
                :label="t$('cinebuddyApp.ReservierungsStatus.' + reservierungsStatus)"
              >
                {{ reservierungsStatus }}
              </option>
            </select>
            <div v-if="v$.status.$anyDirty && v$.status.$invalid">
              <small class="form-text text-danger" v-for="error of v$.status.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label v-text="t$('cinebuddyApp.reservierung.plaetze')" for="reservierung-plaetze"></label>
            <select
              class="form-control"
              id="reservierung-plaetzes"
              data-cy="plaetze"
              multiple
              name="plaetze"
              v-if="reservierung.plaetzes !== undefined"
              v-model="reservierung.plaetzes"
            >
              <option
                :value="getSelected(reservierung.plaetzes, sitzplatzOption, 'id')"
                v-for="sitzplatzOption in sitzplatzs"
                :key="sitzplatzOption.id"
              >
                {{ sitzplatzOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('cinebuddyApp.reservierung.vorfuehrung')" for="reservierung-vorfuehrung"></label>
            <select
              class="form-control"
              id="reservierung-vorfuehrung"
              data-cy="vorfuehrung"
              name="vorfuehrung"
              v-model="reservierung.vorfuehrung"
            >
              <option :value="null"></option>
              <option
                :value="
                  reservierung.vorfuehrung && vorfuehrungOption.id === reservierung.vorfuehrung.id
                    ? reservierung.vorfuehrung
                    : vorfuehrungOption
                "
                v-for="vorfuehrungOption in vorfuehrungs"
                :key="vorfuehrungOption.id"
              >
                {{ vorfuehrungOption.filmTitel }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.cancel')"></span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.save')"></span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./reservierung-update.component.ts"></script>
