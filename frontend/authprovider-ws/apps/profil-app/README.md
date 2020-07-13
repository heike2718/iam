# ProfilApp

[ProfilApp](https://opa-wetterwachs.de/profil-app) ist ein Client zum Pflegen des Benutzerprofils in [Minikänguru-Benutzerkonten](https://opa-wetterwachs.de/auth-app#/)

### ReleaseNotes

__Release 5.1.0:__

jwt auf authorization token grant umgestellt, damit kein JWT im Browser landet.

__Release 5.0.1:__

npm audit hints resolved

__Release 5.0.0:__

update to angular 9

__Release 4.3.0:__

[Userdaten überleben refresh nicht](https://github.com/heike2718/profil-app/issues/12)

[Benutzerkonto löschen](https://github.com/heike2718/profil-app/issues/9)

__Release 4.2.0:__

[Add XSRF Protection](https://github.com/heike2718/profil-app/issues/2)

[Ladeindikator anzeigen wegen Latenz](https://github.com/heike2718/profil-app/issues/10)

[Hinweis auf neuere Version](https://github.com/heike2718/profil-app/issues/7)

__Release 4.0.1:__

prefixed localStorage keys

__Release 4.0.0:__

server side session handling and replace localStorage with session cookies

__Release 3.3.2:__

Beim Aufruf der app wird jetzt immer ein neues client-access-token gezogen

__Release 3.3.1:__

migrated to angular 8

__Release 3.2.0:__

default role renamed: STANDARD

__Release 3.1.0:__

no sleep mode token refresh, serverside client error logs

__Release-3.0.0__:

fixed 401 when an expired jwt is present

__Release-2.0.0__:

automatically refresh client access token and JWT
