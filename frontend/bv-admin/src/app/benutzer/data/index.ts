export { benutzerActions } from './+state/benutzer.actions';
export { fromBenutzer } from './+state/benutzer.selectors';

// exportieren, damit der dataProvider in die API kann.
export { benutzerFeature } from './+state/benutzer.reducer';
export { BenutzerEffects } from './+state/benutzer.effects';