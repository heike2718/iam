export { infomailsActions } from './+state/infomails.actions';
export { fromInfomails } from './+state/infomails.selectors';

// exportieren, damit der dataProvider in die API kann.
export { infomailsFeature } from './+state/infomails.reducer';
export { InfomailsEffects } from './+state/infomails.effects';