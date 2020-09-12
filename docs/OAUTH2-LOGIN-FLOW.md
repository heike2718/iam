# Login OAuth-Flow

Als Beispiel wird der Login-Flow von der profil-app aus beschrieben.

[Sequencediagramm](./login-flow.png)

__Schritt 1: Login-URL anfordern__

Auslöser:

    profil-app Click auf Login

Aufzurufende url (profil-server):

    http://localhost:9600/profil-api/auth/login

Method:

    GET

Backend-Resource:

    ProfilSessionResource.getLoginUrl()

__Schritt 2: ClientAccessToken anfordern__

Akteur

    profil-server

Aufzurufende url (authprovider):

    http://localhost:9000/authprovider/clients/client/accesstoken

Method:

    POST

Request-Payload

    OAuthClientCredentials

Response-Payload:

    OAuthAccessTokenPayload

Backend-Resource:

    ClientResource.authenticateClient()

__Schritt 3: ClientAccessToken erzeugen und speichern__

Der authprovider validiert die OAuthClientCredentials, erzeugt und verwaltet ein ClientAccessToken
und gibt ein OAuthAccessTokenPayload zurück.

__Schritt 4: OAuthAccessTokenPayload an profil-server senden__

Akteur:

	authprovider

Backend-Resource:

    ClientResource.authenticateClient()

__Schritt 5: OAuthAccessTokenPayload in redirect-url umwandeln__

Akteur:

	profil-server

Backend-Resource:

    ProfilSessionResource.getLoginUrl()

Response-Payload:

    ResponsePayload ohne data. Die Url steht in message.message

__Schritt 6: Redirect zu auth-app__

Akteur:

    profil-app: auth.service.logIn()

Schlichtes Browser-Redirect mit der url aus dem ResponsePayload


__Schritt 7: Anonyme session (sessionId und CSRF-Token) anfordern__

Akteur:

    auth-app: log-in.component.ts - ngOnInit() und auth.service.ts - createAnonymousSession()

Aufzurufende url (authprovider):

    http://localhost:9000/authprovider/auth/session

Method:

    GET

Backend-Resource:

    AnonymousSessionResource.createSession()


Response-Payload:

    AuthSession


__Schritt 8: AuthSession erzeugen__

authprovider erzeugt eine AuthSession mit begrenzter Gültigkeit, einer SessionID und
einem CSRF-Token und persistiert diese für das folgende Login

Response-Payload:

    AuthSession

In PROD wird die SessionId mit dem SESSION-Cookie verknüft



__Schritt 9: Sende Login-Credentials (login)__

Aktuer:

    auth-app: login.component.ts und user.service.ts

Aufzurufende url (authprovider):

    http://localhost:9000/authprovider/auth/sessions/auth-token-grant

Method:

    POST

Request-Payload:

    LoginCredentials, Session-ID (DEV: Header, PROD: Cookie), CSRF-Token (X_XSRF_TOKEN-Header)

Response-Payload:

    SignUpLogInResponseData

Backend-Resource (authprovider):

	AuthenticationResource

__Schritt 10: Authentisieren des Users__

Akteur:

    authprovider:

Wenn die Login-Credentials stimmen, werden

* ein JWT erzeugt
* ein Einmaltoken erzeugt
* JWT, Einmaltoken und ClientId in einem Objekt OneTimeTokenJwtData im Heap gehalten

Response-Payload:

    SignUpLogInResponseData

__Schritt 11: aus SignUpLogInResponseData Redirect-Url erzeugen__

Akteur:

    auth-app: user.service.ts

Ergebnis:

    http://localhost:4200#state=login&nonce=null&idToken=bfe889bb-a26c-4d2f-be7d-5cd6b637fc65&oauthFlowType=AUTHORIZATION_TOKEN_GRANT

__Schritt 12: AuthSession beenden__

Akteur:

    auth-app: user.service.ts

Aufzurufende url (authprovider)

    http://localhost:9000/authprovider/auth/session/invalidate

    http://localhost:9000/authprovider/auth/session/dev/invalidate

Method:

	DELETE

Backend-Resource:

    AnonymousSessionResource.clearSession()


__Schritt 13: Redirect zu profil-app (redirectUrl aus Schritt 11)__

Akteur:

    auth-app: login.component.ts

__Schritt 14: Teil hinter # aus redirect url parsen__

Akteur:

    profil-app: app.component.ts

Ergebnis:

    AuthResult

Dieses enthält das Einmaltoken aus Schritt 10

__Schritt 15 Session beim profil-server anfordern__

Akteur:

    profil-app: auth.service.ts

Aufzurufende url (profil-server):

    http://localhost:9600/profil-api/auth/session

Method:

    POST

Request Payload (text/plain)

    AuthResult.idToken (das Einmaltoken aus Schritt 10)

Backend-Resource:

    ProfilSessionResource.getTheJwtAndCreateSession()

__Schritt 16: Beim authprovider das Einmaltoken gegen das JWT tauschen__

Akteur:

    profil-server: TokenExchangeService.exchangeTheOneTimeToken()

Aufzurufende url (authprovider):

    http://localhost:9000/authprovider/token/exchange/oneTimeToken

(oneTimeToken ist PathParameter)

Method:

	PUT

Request-Payload:

	OAuthClientCredentials

Backend-Resource:

    authprovider: TokenExchangeResource.exchangeOneTimeTokenWithJwt()

__Schritt 17: Tausch JWT für Einmaltoken__

Akteur:

	authprovider: AuthorizationService.exchangeTheOneTimeToken()

Es wird im OneTimeTokenJwtRepository nach dem zum Einmaltoken passenden JWT gesucht. Es muss außerdem
auch die clientId aus den OneTimeTokenJwtData mit der aus den OAuthClientCredentials verifizierten
clientId übereinstimmen.


Wenn das passt, wird der Value mit dem Einmaltoken gelöscht

__Schritt 18: JWT zurückgeben__

Akteur:

	authprovider: TokenExchangeResource

__Schritt 19: Session aus JWT bauen__

Akteur:

	profil-server: AuthenticatedUserService

Aus dem JWT wird die UUID geholt und es wird geprüft, ob es für profil einen User mit dieser UUID gibt
Dann wird eine Session mit ca. 60 min Laufzeit erzeugt und gespeichert. Im Fall der Profil-Anwendung werden weitere Daten vom
authprovider nachgeladen (mit Hilfe der client-Session beim authprovider vermittelt durch das ClientAccessToken).

__Schritt 20: Sesson an profil-app geben__

Die Session- Daten werden mit dem AuthenticatedUser und (in PROD) mit dem SESSION_ID-Cookie zurückgegeben.

