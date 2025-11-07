<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="vorfuehrung">
        <h2 class="jh-entity-heading" data-cy="vorfuehrungDetailsHeading">
          <span v-text="t$('cinebuddyApp.vorfuehrung.detail.title')"></span> {{ vorfuehrung.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('cinebuddyApp.vorfuehrung.filmTitel')"></span>
          </dt>
          <dd>
            <span>{{ vorfuehrung.filmTitel }}</span>
          </dd>
          <dt>
            <span v-text="t$('cinebuddyApp.vorfuehrung.datumZeit')"></span>
          </dt>
          <dd>
            <span v-if="vorfuehrung.datumZeit">{{ formatDateLong(vorfuehrung.datumZeit) }}</span>
          </dd>
          <dt>
            <span v-text="t$('cinebuddyApp.vorfuehrung.saal')"></span>
          </dt>
          <dd>
            <div v-if="vorfuehrung.saal">
              <router-link :to="{ name: 'SaalView', params: { saalId: vorfuehrung.saal.id } }">{{ vorfuehrung.saal.name }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="vorfuehrung.id"
          :to="{ name: 'VorfuehrungEdit', params: { vorfuehrungId: vorfuehrung.id } }"
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

<script lang="ts" src="./vorfuehrung-details.component.ts"></script>
