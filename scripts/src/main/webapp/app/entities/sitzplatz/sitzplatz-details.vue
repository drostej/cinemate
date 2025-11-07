<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="sitzplatz">
        <h2 class="jh-entity-heading" data-cy="sitzplatzDetailsHeading">
          <span v-text="t$('cinebuddyApp.sitzplatz.detail.title')"></span> {{ sitzplatz.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('cinebuddyApp.sitzplatz.reihe')"></span>
          </dt>
          <dd>
            <span>{{ sitzplatz.reihe }}</span>
          </dd>
          <dt>
            <span v-text="t$('cinebuddyApp.sitzplatz.nummer')"></span>
          </dt>
          <dd>
            <span>{{ sitzplatz.nummer }}</span>
          </dd>
          <dt>
            <span v-text="t$('cinebuddyApp.sitzplatz.saal')"></span>
          </dt>
          <dd>
            <div v-if="sitzplatz.saal">
              <router-link :to="{ name: 'SaalView', params: { saalId: sitzplatz.saal.id } }">{{ sitzplatz.saal.name }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('cinebuddyApp.sitzplatz.reservierungen')"></span>
          </dt>
          <dd>
            <span v-for="(reservierungen, i) in sitzplatz.reservierungens" :key="reservierungen.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'ReservierungView', params: { reservierungId: reservierungen.id } }">{{
                reservierungen.id
              }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="sitzplatz.id"
          :to="{ name: 'SitzplatzEdit', params: { sitzplatzId: sitzplatz.id } }"
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

<script lang="ts" src="./sitzplatz-details.component.ts"></script>
