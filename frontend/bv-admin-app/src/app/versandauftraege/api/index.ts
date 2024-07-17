import { versandauftraegeActions} from '@bv-admin-app/versandauftraege/data';

export { VersandauftraegeFacade } from './versandauftraege.facade';
export { versandauftraegeDataProvider  } from './versandauftraege-data.provider';

// Dieses event soll auch modulübergreifend zur Verfügung stehen
export const versandauftragScheduledEvent = versandauftraegeActions.vERSANDAUFTRAG_SCHEDULED;