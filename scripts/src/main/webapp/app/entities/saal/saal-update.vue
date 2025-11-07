<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="cinebuddyApp.saal.home.createOrEditLabel"
          data-cy="SaalCreateUpdateHeading"
          v-text="t$('cinebuddyApp.saal.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="saal.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="saal.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('cinebuddyApp.saal.name')" for="saal-name"></label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="saal-name"
              data-cy="name"
              :class="{ valid: !v$.name.$invalid, invalid: v$.name.$invalid }"
              v-model="v$.name.$model"
              required
            />
            <div v-if="v$.name.$anyDirty && v$.name.$invalid">
              <small class="form-text text-danger" v-for="error of v$.name.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('cinebuddyApp.saal.kapazitaet')" for="saal-kapazitaet"></label>
            <input
              type="number"
              class="form-control"
              name="kapazitaet"
              id="saal-kapazitaet"
              data-cy="kapazitaet"
              :class="{ valid: !v$.kapazitaet.$invalid, invalid: v$.kapazitaet.$invalid }"
              v-model.number="v$.kapazitaet.$model"
              required
            />
            <div v-if="v$.kapazitaet.$anyDirty && v$.kapazitaet.$invalid">
              <small class="form-text text-danger" v-for="error of v$.kapazitaet.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
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
<script lang="ts" src="./saal-update.component.ts"></script>
