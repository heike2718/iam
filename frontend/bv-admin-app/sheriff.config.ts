import { noDependencies, sameTag, SheriffConfig } from '@softarc/sheriff-core';

export const config: SheriffConfig = {
  version: 1,
  tagging: {
    'src/app': {
      'shared/<type>': 'shared:<type>',
      'shared/messages/<type>': 'type:<type>',
      'users/api': ['type:api', 'domain:users:api'],
      '<domain>/<type>': ['domain:<domain>', 'type:<type>'],
    },
  },
  depRules: {
    root: ['type:feature', 'shared:*', 'domain:*', 'type:ui', 'type:api'],
    'domain:*': sameTag,
    'type:api': 'type:*',
    'type:feature': [
      'type:*',
      'shared:config',
    ],
    'type:data': [
      'type:model',
      'shared:http',
      'shared:messages:api',
    ],
    'type:ui': ['type:model', 'type:api', 'shared:form', 'shared:ui'],    
    'type:model': noDependencies,
    'shared:http': ['shared:config'],
  },
};
