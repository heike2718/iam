import { FlatCompat } from '@eslint/eslintrc';
import { dirname } from 'path';
import { fileURLToPath } from 'url';
import js from '@eslint/js';
import nx from '@nx/eslint-plugin';

const compat = new FlatCompat({
    baseDirectory: dirname(fileURLToPath(import.meta.url)),
    recommendedConfig: js.configs.recommended,
});

export default [
    ...nx.configs['flat/base'],
    ...nx.configs['flat/typescript'],
    {
        files: ['**/*.ts', '**/*.tsx'],
        rules: {
            'no-extra-semi': 'off',
        },
    },
    ...nx.configs['flat/javascript'],
    {
        files: ['**/*.js', '**/*.jsx'],
        rules: {
            '@typescript-eslint/no-extra-semi': 'error',
            'no-extra-semi': 'off',
        },
    },
    ...nx.configs['flat/angular'],
    ...compat
        .config({
            extends: ['plugin:@angular-eslint/template/process-inline-templates'],
        })
        .map(config => ({
            ...config,
            files: ['**/*.ts'],
            rules: {
                ...config.rules,
                '@angular-eslint/directive-selector': [
                    'error',
                    {
                        type: 'attribute',
                        prefix: 'bv-admin',
                        style: 'camelCase',
                    },
                ],
                '@angular-eslint/component-selector': [
                    'error',
                    {
                        type: 'element',
                        prefix: 'bv-admin',
                        style: 'kebab-case',
                    },
                ],
            },
        })),
    ...nx.configs['flat/angular-template'],
    ...compat
        .config({
            extends: ['plugin:@softarc/sheriff/default'],
        })
        .map(config => ({
            ...config,
            files: ['**/*.ts'],
            rules: {
                ...config.rules,
            },
        })),
];
