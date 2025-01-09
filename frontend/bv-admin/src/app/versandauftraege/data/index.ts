export { versandauftraegeActions } from './+state/versandauftraege.actions';
export { fromVersandauftraege } from './+state/versandauftraege.selectors';

// exportieren, damit der dataProvider in die API kann.
export { versandauftraegeFeature } from './+state/versandauftraege.reducer';
export { VersandauftraegeEffects } from './+state/versandauftraege.effects';