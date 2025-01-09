import { noDependencies, sameTag, SheriffConfig } from '@softarc/sheriff-core';

/**
 * Documentation: https://softarc-consulting.github.io/sheriff/docs/introduction
 */

/**
  * Minimal configuration for Sheriff
  * Assigns the 'noTag' tag to all modules and
  * allows all modules to depend on each other.
  * 
  */

export const config: SheriffConfig = {
  version: 1,
  autoTagging: false,
  tagging: {
    'src/app': {
      'benutzer/model': ['domain:benutzer:model'],
      'benutzer/data': ['domain:benutzer:data'],
      'benutzer/api': ['domain:benutzer:api'],
      'benutzer/feature': ['domain:benutzer:feature'],
      'infomails/model': ['domain:infomails:model'],
      'infomails/data': ['domain:infomails:data'],
      'infomails/api': ['domain:infomails:api'],
      'infomails/feature': ['domain:infomails:feature'],
      'versandauftraege/model': ['domain:versandauftraege:model'],
      'versandauftraege/data': ['domain:versandauftraege:data'],
      'versandauftraege/api': ['domain:versandauftraege:api'],
      'versandauftraege/feature': ['domain:versandauftraege:feature'],
      'shared/api/<type>': ['shared:api:<type>'],
      'shared/messages/<type>': ['shared:messages:<type>'],
      'shared/auth/<type>': ['shared:auth:<type>'],
      'shared/config': ['shared:config'],
      'shared/http': ['shared:http'],
      'shared/local-storage-data': ['shared:local-storage-data'],
      'shared/model': ['type:shared:model'],
      'shared/ui/benutzerbasket-status': ['shared:ui:benutzerbasket-status'],
      'shared/ui/components': ['shared:ui:components'],
      'shared/util': ['type:util']
    }
  }, // apply tags to your modules
  depRules: {
    // root is a virtual module, which contains all files not being part
    // of any module, e.g. application shell, main.ts, etc.
    'root': [
      'type:feature',
      'type:api',
      'type:model',
      'shared:config',
      'shared:http',
      'shared:local-storage-data',
      'shared:messages:api',
      'shared:messages:ui',
      'shared:auth:api',
      'shared:auth:model',
      'shared:ui:benutzerbasket-status',
      ({ to }) => to.startsWith('domain:'),
      'noTag'],
    'domain:benutzer:feature': [
      'shared:auth:api',
      'type:shared:model',
      'type:util',
      'domain:benutzer:model',
      'domain:benutzer:api',
      'shared:ui:components'
    ],
    'domain:benutzer:api': [
      'shared:auth:api',
      'type:shared:model',
      'type:util',
      'domain:benutzer:model',
      'domain:benutzer:data'
    ],
    'domain:benutzer:data': [
      'shared:auth:api',
      'type:shared:model',
      'type:util',
      'domain:benutzer:model',
      'shared:messages:api'
    ],
    'domain:benutzer:model': ['type:util', 'type:shared:model'],
    'domain:infomails:feature': [
      'shared:auth:api',
      'type:shared:model',
      'type:util',
      'domain:infomails:model',
      'domain:infomails:api',
      'domain:benutzer:api',
      'domain:versandauftraege:api',
      'shared:ui:components'
    ],
    'domain:infomails:data': [
      'shared:auth:api',
      'type:shared:model',
      'type:util',
      'type:model',
      'shared:messages:api',
      'domain:infomails:model',
      'domain:versandauftraege:api',
      'domain:benutzer:api'
    ],
    'domain:infomails:api': [
      'shared:auth:api',
      'type:shared:model',
      'type:util',
      'domain:infomails:model',
      'domain:infomails:data'
    ],
    'domain:infomails:model': ['type:util', 'type:shared:model'],
    'domain:versandauftraege:feature': [
      'shared:auth:api',
      'type:shared:model',
      'type:util',
      'domain:versandauftraege:model',
      'domain:versandauftraege:api',
      'shared:ui:components'
    ],
    'domain:versandauftraege:api': [
      'shared:auth:api',
      'type:shared:model',
      'type:util',
      'type:data',
      'domain:versandauftraege:model',
      'domain:versandauftraege:data'
    ],
    'domain:versandauftraege:data': [
      'shared:auth:api',
      'shared:messages:api',
      'type:shared:model',
      'type:util',
      'domain:versandauftraege:model'
    ],
    'domain:versandauftraege:model': ['type:util', 'type:shared:model'],
    'type:feature': [
      'type:model',
      'type:api',
      'shared:ui:components',
      'shared:ui:benutzerbasket-status'
    ],
    'type:api': [
      'type:model',
      'type:data',
      'type:util'
    ],
    'type:data': [
      'type:model',
      'type:util',
      'shared:auth:api',
      'shared:messages:api'
    ],
    'type:ui': [
      'type:api',
      'type:model'
    ],
    'type:util': noDependencies,
    'shared:ui:componente': noDependencies,
    'shared:ui:benutzerbasket-status': [
      'type:api',
      'domain:benutzer:api',
      'shared:auth:api'
    ],
    'shared:auth:api': [
      'shared:auth:data',
      'shared:auth:model',
      'shared:messages:api',
      'type:api',
      'type:util'
    ],
    'shared:auth:data': [
      'shared:auth:model',
      'shared:messages:api',
      'shared:http',
      'type:api',
      'type:util'
    ],
    'shared:messages:ui': [
      'shared:messages:api'
    ],
    'shared:messages:api': noDependencies,
    'shared:http': [
      'shared:config',
      'shared:messages:api'
    ],
    'shared:local-storage-data': [
      'shared:data',
      'type:util',
      'shared:auth:api'
    ],
    'type:model': ['type:shared:model'],
    'type:shared:model': noDependencies,
    'shared:config': noDependencies,
    noTag: ['noTag', 'root'],


    // add your dependency rules here
  },
};
