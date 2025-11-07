<template>
  <div>
    <b-dropdown-item to="/saal">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.saal')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/sitzplatz">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.sitzplatz')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/vorfuehrung">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.vorfuehrung')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/reservierung">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.reservierung')"></span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
