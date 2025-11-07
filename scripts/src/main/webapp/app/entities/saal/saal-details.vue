<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="saal">
        <h2 class="jh-entity-heading" data-cy="saalDetailsHeading">
          <span v-text="t$('cinebuddyApp.saal.detail.title')"></span> {{ saal.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('cinebuddyApp.saal.name')"></span>
          </dt>
          <dd>
            <span>{{ saal.name }}</span>
          </dd>
          <dt>
            <span v-text="t$('cinebuddyApp.saal.kapazitaet')"></span>
          </dt>
          <dd>
            <span>{{ saal.kapazitaet }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="saal.id" :to="{ name: 'SaalEdit', params: { saalId: saal.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./saal-details.component.ts"></script>
