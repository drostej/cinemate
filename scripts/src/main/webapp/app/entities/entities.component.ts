import { defineComponent, provide } from 'vue';

import SaalService from './saal/saal.service';
import SitzplatzService from './sitzplatz/sitzplatz.service';
import VorfuehrungService from './vorfuehrung/vorfuehrung.service';
import ReservierungService from './reservierung/reservierung.service';
import UserService from '@/entities/user/user.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Entities',
  setup() {
    provide('userService', () => new UserService());
    provide('saalService', () => new SaalService());
    provide('sitzplatzService', () => new SitzplatzService());
    provide('vorfuehrungService', () => new VorfuehrungService());
    provide('reservierungService', () => new ReservierungService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});
