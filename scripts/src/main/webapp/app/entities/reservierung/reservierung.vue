<template>
  <div>
    <h2 id="page-heading" data-cy="ReservierungHeading">
      <span v-text="t$('cinebuddyApp.reservierung.home.title')" id="reservierung-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('cinebuddyApp.reservierung.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'ReservierungCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-reservierung"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('cinebuddyApp.reservierung.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && reservierungs && reservierungs.length === 0">
      <span v-text="t$('cinebuddyApp.reservierung.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="reservierungs && reservierungs.length > 0">
      <table class="table table-striped" aria-describedby="reservierungs">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('cinebuddyApp.reservierung.kundeName')"></span></th>
            <th scope="row"><span v-text="t$('cinebuddyApp.reservierung.status')"></span></th>
            <th scope="row"><span v-text="t$('cinebuddyApp.reservierung.plaetze')"></span></th>
            <th scope="row"><span v-text="t$('cinebuddyApp.reservierung.vorfuehrung')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="reservierung in reservierungs" :key="reservierung.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ReservierungView', params: { reservierungId: reservierung.id } }">{{
                reservierung.id
              }}</router-link>
            </td>
            <td>{{ reservierung.kundeName }}</td>
            <td v-text="t$('cinebuddyApp.ReservierungsStatus.' + reservierung.status)"></td>
            <td>
              <span v-for="(plaetze, i) in reservierung.plaetzes" :key="plaetze.id"
                >{{ i > 0 ? ', ' : '' }}
                <router-link class="form-control-static" :to="{ name: 'SitzplatzView', params: { sitzplatzId: plaetze.id } }">{{
                  plaetze.id
                }}</router-link>
              </span>
            </td>
            <td>
              <div v-if="reservierung.vorfuehrung">
                <router-link :to="{ name: 'VorfuehrungView', params: { vorfuehrungId: reservierung.vorfuehrung.id } }">{{
                  reservierung.vorfuehrung.filmTitel
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ReservierungView', params: { reservierungId: reservierung.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ReservierungEdit', params: { reservierungId: reservierung.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(reservierung)"
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
          id="cinebuddyApp.reservierung.delete.question"
          data-cy="reservierungDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-reservierung-heading" v-text="t$('cinebuddyApp.reservierung.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-reservierung"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeReservierung()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./reservierung.component.ts"></script>
