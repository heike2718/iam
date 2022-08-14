# authprovider Release Notes

__Release 7.3.5__:

[Loading-Indicator bei diversen http-Requests](https://github.com/heike2718/authenticationprovider/issues/51)
[Security upgrades](https://github.com/heike2718/authenticationprovider/issues/52)
[Erwartete UI-Version vom Server holen](https://github.com/heike2718/authenticationprovider/issues/45)

__Release 7.3.2__:

[Kein Logout und keine Erfolgsmeldung nach dem Löschen des Benutzerkontos](https://github.com/heike2718/authenticationprovider/issues/44)

__Release 7.3.1.1__:

[401 when incorrect XSRF-TOKEN](https://github.com/heike2718/authenticationprovider/issues/22)

__Release 7.3.1__:

* [Passwort einblendbar machen](https://github.com/heike2718/authenticationprovider/issues/40)
* bump hewi-java-commons
* bump quarkus

__Release 7.3.0__:

* [Frontend Fehlermeldung Email leer](https://github.com/heike2718/authenticationprovider/issues/11)
* [Profilserver Http-Client connextion leak](https://github.com/heike2718/authenticationprovider/issues/26)
* [red border for delete account](https://github.com/heike2718/authenticationprovider/issues/4)
* [Version server anzeigen](https://github.com/heike2718/authenticationprovider/issues/2)
* [Info toggle Passwortregeln funktioniert nicht](https://github.com/heike2718/authenticationprovider/issues/35)
* [Neue Quarkus- und Angular-Versionen](https://github.com/heike2718/authenticationprovider/issues/36)

__Release 7.2.3__:

* [Einmalpasswort ändern- Seite: Link auf Passwort-vergessen-Dokumentation](https://github.com/heike2718/authenticationprovider/issues/29)
* [ClassCastException AuthproviderEventhandler](https://github.com/heike2718/authenticationprovider/issues/30)

__Release 7.2.3__:

bcrypt CVE fixed

__Release 7.2.2__:

* bump quarkus to 1.10.5-Final
* sec patches crypto

__Release 7.2.1__:

[weniger INFO ins authlog](https://github.com/heike2718/authenticationprovider/issues/24)

__Release 7.2.0__:

* bumped quarkus to 1.10.2.Final
* [Info an mkv beim Löschen von Benutzerkonten während des Registrierungsprozesses](https://github.com/heike2718/authenticationprovider/issues/20)

__Release 7.1.1__:

* bumped quarkus to 1.7.3-Final
* propagate delete user to minikaenguru
* handshake beim Propagieren von user change/delete evente
* log into files
* new common release because of shiro CVE
* bumped quarkus to 1.7.2-Final

__Release 7.1.0__:

[Umstellung von implicite grant auf Authorization Code Grant](https://github.com/heike2718/authprovider/issues/32)

__Release 7.0.1__:

[keine Mailvalidierung](https://github.com/heike2718/auth-app/issues/26)

__Release 7.0.0__:

update quarkus to 1.3.0-Final

__Release 6.2.2__:

CVE-2020-8840: upgrade to [hewi-java-commons version 1.4.4](https://github.com/heike2718/hewi-java-commons/releases/tag/1.4.4)

__Release 6.2.1__:

[ER-Diagramm von CLIENTS aktualisieren](https://github.com/heike2718/authprovider/issues/22)

__Release 6.2.0__:

[REST-API zum Löschen des Benutzerkontos](https://github.com/heike2718/authprovider/issues/13)

[URIs ohne UUIDs](https://github.com/heike2718/authprovider/issues/20)

[Info per Mail über Löschung eines Accounts](https://github.com/heike2718/authprovider/issues/21)

__Release 6.1.2__:

[DB Clients und Accesstokens zurückbauen](https://github.com/heike2718/authprovider/issues/17)

__Release 6.1.1__:

[Add XSRF protection](https://github.com/heike2718/authprovider/issues/7)

[README aktualisieren](https://github.com/heike2718/authprovider/issues/15)

__Release 6.1.0__:

[fix logger categories](https://github.com/heike2718/authprovider/issues/10)

upgrade to [hewi-java-commons version 1.4.1](https://github.com/heike2718/hewi-java-commons/releases/tag/1.4.1)

__Release 6.0.2__:

log endpoint for apps

__Release 6.0.1__:

security leak in change profile data fixed

__Release 6.0.0__:

client access tokens only serverside

extracted the api for changing profile to an own microservice profil-server

__Release 5.2.1__:

CORS: restricted the allowed origins to only one URL

__Release 5.2.0__:

newer quarkus version in order to fix several CVEs

__Release 5.1.1__:

resiliente Implementierung von authorizeClient. Back-Button führte bisher dazu, dass man in der auth-app mit 401 hängen blieb. Jetzt wird redirected, damit ein neues gültiges client access token generiert wird.

__Release 5.1.0__:

upgrade to quarkus 0.26.1

__Release 5.0.3__:

JWTRefreshService: NPE weil ContainerRequestContext null war

__Release 5.0.2__:

404 on client access token replace fixed

__Release 5.0.1__:

SessionExpiredException moved

Allowed Headers fixed

new API /authprovider/version

__Release 5.0.0__:

now runs with __quarkus__ :D

__Release 4.1.1__:

bugfixes for import users (mapped directories changed)

__Release 4.1.0__:

default role renamed to STANDARD

import api for existing users in mkv

__Release 4.0.0__:

sign up with roles

serverside logs of client errors

refresh tokens in sleep modus removed

delete clientAccessTokens on refresh

__Release 3.0.2__:

allow nultiple chlientAccessTokens on refreshing jwt because of single sign on

__Release 3.0.1__:

issue transaction required wehn refreshing jwt

__Release 3.0.0__:

Backend zum refreshen des Client-Accesstokens sowie des JWT. Beides nicht abwärtskompatibel.

__Release 2.1.0__:

Backend zum Ändern der Daten des Benutzerkontos (Name, Mail, Passwort,...) für profil-app. Exceptionhandling stabilisiert
