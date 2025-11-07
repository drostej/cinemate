<template>
  <div>
    <h2 id="page-heading" data-cy="SaalHeading">
      <span v-text="t$('cinebuddyApp.saal.home.title')" id="saal-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('cinebuddyApp.saal.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'SaalCreate' }" custom v-slot="{ navigate }">
          <button @click="navigate" id="jh-create-entity" data-cy="entityCreateButton" class="btn btn-primary jh-create-entity create-saal">
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('cinebuddyApp.saal.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && saals && saals.length === 0">
      <span v-text="t$('cinebuddyApp.saal.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="saals && saals.length > 0">
      <table class="table table-striped" aria-describedby="saals">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('cinebuddyApp.saal.name')"></span></th>
            <th scope="row"><span v-text="t$('cinebuddyApp.saal.kapazitaet')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="saal in saals" :key="saal.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'SaalView', params: { saalId: saal.id } }">{{ saal.id }}</router-link>
            </td>
            <td>{{ saal.name }}</td>
            <td>{{ saal.kapazitaet }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'SaalView', params: { saalId: saal.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'SaalEdit', params: { saalId: saal.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(saal)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.delete')"></span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span id="cinebuddyApp.saal.delete.question" data-cy="saalDeleteDialogHeading" v-text="t$('entity.delete.title')"></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-saal-heading" v-text="t$('cinebuddyApp.saal.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-saal"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeSaal()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./saal.component.ts"></script>
