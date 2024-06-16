import { provideEffects } from '@ngrx/effects';
import { provideState } from '@ngrx/store';
import { authFeature, AuthEffects } from '@bv-admin-app/shared/auth/data';

export const authDataProvider = [
    provideState(authFeature),
    provideEffects(AuthEffects)
];

export * from '@bv-admin-app/shared/auth/data';