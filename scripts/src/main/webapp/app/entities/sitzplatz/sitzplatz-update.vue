<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="cinebuddyApp.sitzplatz.home.createOrEditLabel"
          data-cy="SitzplatzCreateUpdateHeading"
          v-text="t$('cinebuddyApp.sitzplatz.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="sitzplatz.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="sitzplatz.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('cinebuddyApp.sitzplatz.reihe')" for="sitzplatz-reihe"></label>
            <input
              type="text"
              class="form-control"
              name="reihe"
              id="sitzplatz-reihe"
              data-cy="reihe"
              :class="{ valid: !v$.reihe.$invalid, invalid: v$.reihe.$invalid }"
              v-model="v$.reihe.$model"
              required
            />
            <div v-if="v$.reihe.$anyDirty && v$.reihe.$invalid">
              <small class="form-text text-danger" v-for="error of v$.reihe.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('cinebuddyApp.sitzplatz.nummer')" for="sitzplatz-nummer"></label>
            <input
              type="number"
              class="form-control"
              name="nummer"
              id="sitzplatz-nummer"
              data-cy="nummer"
              :class="{ valid: !v$.nummer.$invalid, invalid: v$.nummer.$invalid }"
              v-model.number="v$.nummer.$model"
              required
            />
            <div v-if="v$.nummer.$anyDirty && v$.nummer.$invalid">
              <small class="form-text text-danger" v-for="error of v$.nummer.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('cinebuddyApp.sitzplatz.saal')" for="sitzplatz-saal"></label>
            <select class="form-control" id="sitzplatz-saal" data-cy="saal" name="saal" v-model="sitzplatz.saal">
              <option :value="null"></option>
              <option
                :value="sitzplatz.saal && saalOption.id === sitzplatz.saal.id ? sitzplatz.saal : saalOption"
                v-for="saalOption in saals"
                :key="saalOption.id"
              >
                {{ saalOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label v-text="t$('cinebuddyApp.sitzplatz.reservierungen')" for="sitzplatz-reservierungen"></label>
            <select
              class="form-control"
              id="sitzplatz-reservierungens"
              data-cy="reservierungen"
              multiple
              name="reservierungen"
              v-if="sitzplatz.reservierungens !== undefined"
              v-model="sitzplatz.reservierungens"
            >
              <option
                :value="getSelected(sitzplatz.reservierungens, reservierungOption, 'id')"
                v-for="reservierungOption in reservierungs"
                :key="reservierungOption.id"
              >
                {{ reservierungOption.id }}
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
<script lang="ts" src="./sitzplatz-update.component.ts"></script>
