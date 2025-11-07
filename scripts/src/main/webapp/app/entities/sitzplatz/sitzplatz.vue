<template>
  <div>
    <h2 id="page-heading" data-cy="SitzplatzHeading">
      <span v-text="t$('cinebuddyApp.sitzplatz.home.title')" id="sitzplatz-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('cinebuddyApp.sitzplatz.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'SitzplatzCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-sitzplatz"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('cinebuddyApp.sitzplatz.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && sitzplatzs && sitzplatzs.length === 0">
      <span v-text="t$('cinebuddyApp.sitzplatz.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="sitzplatzs && sitzplatzs.length > 0">
      <table class="table table-striped" aria-describedby="sitzplatzs">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('cinebuddyApp.sitzplatz.reihe')"></span></th>
            <th scope="row"><span v-text="t$('cinebuddyApp.sitzplatz.nummer')"></span></th>
            <th scope="row"><span v-text="t$('cinebuddyApp.sitzplatz.saal')"></span></th>
            <th scope="row"><span v-text="t$('cinebuddyApp.sitzplatz.reservierungen')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="sitzplatz in sitzplatzs" :key="sitzplatz.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'SitzplatzView', params: { sitzplatzId: sitzplatz.id } }">{{ sitzplatz.id }}</router-link>
            </td>
            <td>{{ sitzplatz.reihe }}</td>
            <td>{{ sitzplatz.nummer }}</td>
            <td>
              <div v-if="sitzplatz.saal">
                <router-link :to="{ name: 'SaalView', params: { saalId: sitzplatz.saal.id } }">{{ sitzplatz.saal.name }}</router-link>
              </div>
            </td>
            <td>
              <span v-for="(reservierungen, i) in sitzplatz.reservierungens" :key="reservierungen.id"
                >{{ i > 0 ? ', ' : '' }}
                <router-link
                  class="form-control-static"
                  :to="{ name: 'ReservierungView', params: { reservierungId: reservierungen.id } }"
                  >{{ reservierungen.id }}</router-link
                >
              </span>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'SitzplatzView', params: { sitzplatzId: sitzplatz.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'SitzplatzEdit', params: { sitzplatzId: sitzplatz.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(sitzplatz)"
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
        <span id="cinebuddyApp.sitzplatz.delete.question" data-cy="sitzplatzDeleteDialogHeading" v-text="t$('entity.delete.title')"></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-sitzplatz-heading" v-text="t$('cinebuddyApp.sitzplatz.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-sitzplatz"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeSitzplatz()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./sitzplatz.component.ts"></script>
