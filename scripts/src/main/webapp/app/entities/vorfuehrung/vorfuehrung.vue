<template>
  <div>
    <h2 id="page-heading" data-cy="VorfuehrungHeading">
      <span v-text="t$('cinebuddyApp.vorfuehrung.home.title')" id="vorfuehrung-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('cinebuddyApp.vorfuehrung.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'VorfuehrungCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-vorfuehrung"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('cinebuddyApp.vorfuehrung.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && vorfuehrungs && vorfuehrungs.length === 0">
      <span v-text="t$('cinebuddyApp.vorfuehrung.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="vorfuehrungs && vorfuehrungs.length > 0">
      <table class="table table-striped" aria-describedby="vorfuehrungs">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('cinebuddyApp.vorfuehrung.filmTitel')"></span></th>
            <th scope="row"><span v-text="t$('cinebuddyApp.vorfuehrung.datumZeit')"></span></th>
            <th scope="row"><span v-text="t$('cinebuddyApp.vorfuehrung.saal')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="vorfuehrung in vorfuehrungs" :key="vorfuehrung.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'VorfuehrungView', params: { vorfuehrungId: vorfuehrung.id } }">{{ vorfuehrung.id }}</router-link>
            </td>
            <td>{{ vorfuehrung.filmTitel }}</td>
            <td>{{ formatDateShort(vorfuehrung.datumZeit) || '' }}</td>
            <td>
              <div v-if="vorfuehrung.saal">
                <router-link :to="{ name: 'SaalView', params: { saalId: vorfuehrung.saal.id } }">{{ vorfuehrung.saal.name }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'VorfuehrungView', params: { vorfuehrungId: vorfuehrung.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'VorfuehrungEdit', params: { vorfuehrungId: vorfuehrung.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(vorfuehrung)"
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
        <span
          id="cinebuddyApp.vorfuehrung.delete.question"
          data-cy="vorfuehrungDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-vorfuehrung-heading" v-text="t$('cinebuddyApp.vorfuehrung.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-vorfuehrung"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeVorfuehrung()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./vorfuehrung.component.ts"></script>
