# auth-app Release Notes

__Release 5.1.0:__

jwt mit authorization token grant flow, damit das JWT niemals im Browser landet.


__Release 5.0.2:__

fix npm audit hints

__Release 5.0.1:__

[keine remote Mailvalidierung](https://github.com/heike2718/auth-app/issues/26)

__Release 4.0.0:__

update to angular 9

__Release 3.4.0:__

[Keinen Wechsel zwischen Login und Signup ermöglichen](https://github.com/heike2718/auth-app/issues/20)


__Release 3.3.2:__

[Weniger Query-Parameter in redirect url](https://github.com/heike2718/auth-app/issues/18)

__Release 3.3.1:__

[Add XSRF Protection](https://github.com/heike2718/auth-app/issues/8)

[Link zur Profilapp auf auth-app/home](https://github.com/heike2718/auth-app/issues/16)

__Release 3.3.0:__

[upgrade to hewi-ng-lib@4.0.0](https://github.com/heike2718/auth-app/issues/9)

__Release 3.2.3:__

[Festhängen mit 401 nach Verwendung des Back-Buttons](https://github.com/heike2718/auth-app/issues/6)

__Release 3.2.2:__

logging framework replaced by hewi-ng-lib

__Release 3.2.1:__

migrated to angular 8

__Release 3.1.0:__

default role heißt jetzt STANDARD, statt USER

__Release 3.0.0:__

(nicht abwärtskompatibel) Signatur JWTPayload hat sich geändert, clients. kein refreshToken mehr erforderlich - redirect url geändert

__Release 2.0.0:__

(nicht abwärtskompatibel) Clients müssen statt der clientID ein gültiges accessToken mitgeben (siehe URLs oben)

__Release 1.3.0:__

handle foregot password

__Release 1.2.0:__

provide surename and given name

__Release 1.1.3:__

small devices: less content and smaller font-sizes, forms nerarer to top of page

__Release 1.1.2:__

fix-fstream-cve

__Release 1.1.1:__

rename labels submit buttons

__Release 1.1.0:__

assets werden jetzt immer gefunden

