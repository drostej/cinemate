<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="cinebuddyApp.vorfuehrung.home.createOrEditLabel"
          data-cy="VorfuehrungCreateUpdateHeading"
          v-text="t$('cinebuddyApp.vorfuehrung.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="vorfuehrung.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="vorfuehrung.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('cinebuddyApp.vorfuehrung.filmTitel')" for="vorfuehrung-filmTitel"></label>
            <input
              type="text"
              class="form-control"
              name="filmTitel"
              id="vorfuehrung-filmTitel"
              data-cy="filmTitel"
              :class="{ valid: !v$.filmTitel.$invalid, invalid: v$.filmTitel.$invalid }"
              v-model="v$.filmTitel.$model"
              required
            />
            <div v-if="v$.filmTitel.$anyDirty && v$.filmTitel.$invalid">
              <small class="form-text text-danger" v-for="error of v$.filmTitel.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('cinebuddyApp.vorfuehrung.datumZeit')" for="vorfuehrung-datumZeit"></label>
            <div class="d-flex">
              <input
                id="vorfuehrung-datumZeit"
                data-cy="datumZeit"
                type="datetime-local"
                class="form-control"
                name="datumZeit"
                :class="{ valid: !v$.datumZeit.$invalid, invalid: v$.datumZeit.$invalid }"
                required
                :value="convertDateTimeFromServer(v$.datumZeit.$model)"
                @change="updateInstantField('datumZeit', $event)"
              />
            </div>
            <div v-if="v$.datumZeit.$anyDirty && v$.datumZeit.$invalid">
              <small class="form-text text-danger" v-for="error of v$.datumZeit.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('cinebuddyApp.vorfuehrung.saal')" for="vorfuehrung-saal"></label>
            <select class="form-control" id="vorfuehrung-saal" data-cy="saal" name="saal" v-model="vorfuehrung.saal">
              <option :value="null"></option>
              <option
                :value="vorfuehrung.saal && saalOption.id === vorfuehrung.saal.id ? vorfuehrung.saal : saalOption"
                v-for="saalOption in saals"
                :key="saalOption.id"
              >
                {{ saalOption.name }}
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
<script lang="ts" src="./vorfuehrung-update.component.ts"></script>
