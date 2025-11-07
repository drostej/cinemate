import { Authority } from '@/shared/security/authority';
const Entities = () => import('@/entities/entities.vue');

const Saal = () => import('@/entities/saal/saal.vue');
const SaalUpdate = () => import('@/entities/saal/saal-update.vue');
const SaalDetails = () => import('@/entities/saal/saal-details.vue');

const Sitzplatz = () => import('@/entities/sitzplatz/sitzplatz.vue');
const SitzplatzUpdate = () => import('@/entities/sitzplatz/sitzplatz-update.vue');
const SitzplatzDetails = () => import('@/entities/sitzplatz/sitzplatz-details.vue');

const Vorfuehrung = () => import('@/entities/vorfuehrung/vorfuehrung.vue');
const VorfuehrungUpdate = () => import('@/entities/vorfuehrung/vorfuehrung-update.vue');
const VorfuehrungDetails = () => import('@/entities/vorfuehrung/vorfuehrung-details.vue');

const Reservierung = () => import('@/entities/reservierung/reservierung.vue');
const ReservierungUpdate = () => import('@/entities/reservierung/reservierung-update.vue');
const ReservierungDetails = () => import('@/entities/reservierung/reservierung-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'saal',
      name: 'Saal',
      component: Saal,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'saal/new',
      name: 'SaalCreate',
      component: SaalUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'saal/:saalId/edit',
      name: 'SaalEdit',
      component: SaalUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'saal/:saalId/view',
      name: 'SaalView',
      component: SaalDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'sitzplatz',
      name: 'Sitzplatz',
      component: Sitzplatz,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'sitzplatz/new',
      name: 'SitzplatzCreate',
      component: SitzplatzUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'sitzplatz/:sitzplatzId/edit',
      name: 'SitzplatzEdit',
      component: SitzplatzUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'sitzplatz/:sitzplatzId/view',
      name: 'SitzplatzView',
      component: SitzplatzDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'vorfuehrung',
      name: 'Vorfuehrung',
      component: Vorfuehrung,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'vorfuehrung/new',
      name: 'VorfuehrungCreate',
      component: VorfuehrungUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'vorfuehrung/:vorfuehrungId/edit',
      name: 'VorfuehrungEdit',
      component: VorfuehrungUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'vorfuehrung/:vorfuehrungId/view',
      name: 'VorfuehrungView',
      component: VorfuehrungDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'reservierung',
      name: 'Reservierung',
      component: Reservierung,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'reservierung/new',
      name: 'ReservierungCreate',
      component: ReservierungUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'reservierung/:reservierungId/edit',
      name: 'ReservierungEdit',
      component: ReservierungUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'reservierung/:reservierungId/view',
      name: 'ReservierungView',
      component: ReservierungDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
