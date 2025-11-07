<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="reservierung">
        <h2 class="jh-entity-heading" data-cy="reservierungDetailsHeading">
          <span v-text="t$('cinebuddyApp.reservierung.detail.title')"></span> {{ reservierung.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('cinebuddyApp.reservierung.kundeName')"></span>
          </dt>
          <dd>
            <span>{{ reservierung.kundeName }}</span>
          </dd>
          <dt>
            <span v-text="t$('cinebuddyApp.reservierung.status')"></span>
          </dt>
          <dd>
            <span v-text="t$('cinebuddyApp.ReservierungsStatus.' + reservierung.status)"></span>
          </dd>
          <dt>
            <span v-text="t$('cinebuddyApp.reservierung.plaetze')"></span>
          </dt>
          <dd>
            <span v-for="(plaetze, i) in reservierung.plaetzes" :key="plaetze.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'SitzplatzView', params: { sitzplatzId: plaetze.id } }">{{ plaetze.id }}</router-link>
            </span>
          </dd>
          <dt>
            <span v-text="t$('cinebuddyApp.reservierung.vorfuehrung')"></span>
          </dt>
          <dd>
            <div v-if="reservierung.vorfuehrung">
              <router-link :to="{ name: 'VorfuehrungView', params: { vorfuehrungId: reservierung.vorfuehrung.id } }">{{
                reservierung.vorfuehrung.filmTitel
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="reservierung.id"
          :to="{ name: 'ReservierungEdit', params: { reservierungId: reservierung.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./reservierung-details.component.ts"></script>
