// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `.angular-cli.json`.

export const environment = {
  production: false,
  version: '7.2.2',
  envName: 'a300',
  apiUrl: 'http://a300:9600/profil-api',
  assetsUrl: 'http://a300/profil-app/assets',
  loginRedirectUrl: 'http://a300/profil-app',
  consoleLogActive: true,
  serverLogActive: true,
  loglevel: 1
};
