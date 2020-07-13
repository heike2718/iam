# Signup OAuth2-Flow

Als Beispiel wird der Signup-Flow von der checlistenapp aus beschrieben.


### Wie kann Kontext aus dem authprovider-Client durchgereicht werden?

Die redirect-Urls enthalten einen Query-Parameter nonce, der vom authprovider an den Client zurückgegeben wird. Mit diesem
Parameter kann Kontext aus dem authprovider-Client über den Workflow unverändert wieder zurück transportiert werden.
(Beispielsweise bei Lehrern die ID der Schule, die in dem mkv-app als erstes ausgewählt
werden muss.)

__Schritt 1: Signup-URL anfordern__

Auslöser:

    checklistenapp Click auf "registrieren"

Aufzurufende url (checklistenserver):

    http://localhost:9300/checklisten-api/auth/signup

Method:

    GET

Backend-Resource:

    ChecklistenSessionResource.getSignupUrl()

__Schritt 2: ClientAccessToken anfordern__

Akteur

    checklistenserver

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

__Schritt 4: OAuthAccessTokenPayload an checklistenserver senden__

Akteur:

	authprovider

Backend-Resource:

    ClientResource.authenticateClient()

__Schritt 5: OAuthAccessTokenPayload in redirect-url umwandeln__

Akteur:

	checklistenserver

Backend-Resource:

    ChecklistenSessionResource.getSignupUrl()

Response-Payload:

    ResponsePayload ohne data. Die Url steht in message.message

__Schritt 6: Redirect zu auth-app__

Akteur:

    checklistenapp: auth.service.ts: signUp()

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



__Schritt 9: Sende Signup-Daten (registrieren)__

Aktuer:

    auth-app: sign-up.component.ts: submitUser() und user.service.ts: registerUser()

Aufzurufende url (authprovider):

    http://localhost:9000/authprovider/auth/sessions/auth-token-grant

Method:

    POST

Request-Payload:

    SignUpCredentials, Session-ID (DEV: Header, PROD: Cookie), CSRF-Token (X_XSRF_TOKEN-Header)

Response-Payload:

    SignUpLogInResponseData

Backend-Resource (authprovider):

	AuthenticationResource

__Schritt 10: User anlegen und speichern__

Akteur:

    authprovider: RegistrationService.createNewResourceOwner() und AuthorizationService.createAndStoreAuthorization()

Wenn die Login-Credentials stimmen, werden

* ein neuer User erzeugt und gespeichert
* ein Aktivierungslink erzeugt, gespeichert und per Mail versendet
* ein Einmaltoken erzeugt
* ein JWT erzeugt
* ein Einmaltoken erzeugt
* JWT, Einmaltoken und ClientId in einem Objekt OneTimeTokenJwtData im Heap gehalten

Response-Payload:

    SignUpLogInResponseData

Das einmaltoken steht in SignUpLogInResponseData.idToken

__Schritt 11: aus SignUpLogInResponseData Redirect-Url erzeugen__

Akteur:

    auth-app: user.service.ts

Ergebnis:

    http://localhost:4200#state=signup&nonce=null&idToken=bfe889bb-a26c-4d2f-be7d-5cd6b637fc65&oauthFlowType=AUTHORIZATION_TOKEN_GRANT

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


__Schritt 13: Redirect zu checklistenapp (redirectUrl aus Schritt 11)__

Akteur:

    auth-app: sign-up.component.ts: sendRedirect()

__Schritt 14: Teil hinter # aus redirect url parsen__

Akteur:

    checklistenapp: app.component.ts

Ergebnis:

    AuthResult

Dieses enthält das Einmaltoken aus Schritt 10

__Schritt 15 Temporäre Session anlegen__

Akteur:

    checklistenapp: auth.service.ts: createSession()

Aufzurufende url (checklistenserver):

    http://localhost:9300/checklisten-api/auth/session

Method:

    POST

Request Payload (text/plain)

    AuthResult.idToken (das Einmaltoken aus Schritt 10)

Backend-Resource:

    ChecklistenSessionResource.getTheJwtAndCreateSession()

__Schritt 16: Beim authprovider das Einmaltoken gegen das JWT tauschen__

Akteur:

    checklistenserver: TokenExchangeService.exchangeTheOneTimeToken()

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

	checklistenserver: AuthenticatedUserService

Aus dem JWT wird die UUID geholt und es wird geprüft, ob es für profil einen User mit dieser UUID gibt
Dann wird eine Session mit ca. 60 min Laufzeit erzeugt und gespeichert. Im Fall der Profil-Anwendung werden weitere Daten vom
authprovider nachgeladen (mit Hilfe der client-Session beim authprovider vermittelt durch das ClientAccessToken).

### Achtung: Individuelle Implementierungen durch die authprovider-Clients möglich

An dieser Stelle generiert die Checklistenapp eine Kurzzeitsession mit dem Einmaltoken. Anwendungen
können aber stattdessen auch den state (login/signup) in app.component.ts auswerten und mit dem Einmaltoken über den Tausch gegen
das JWT und die daraus gewonnen Daten einen User anlegen.

__Schritt 20: Session an checklistenapp geben__

Die Session- Daten werden mit dem AuthenticatedUser und (in PROD) mit dem SESSION_ID-Cookie zurückgegeben.

__Schritt 21: Neuen User bei der Anwendung anlegen__

Akteur:

	checklistenapp: auth.setvice.ts: createUser()

Aufzurufende Url:

	http://localhost:9300/checklisten-api/signup/user

Method:

	POST

Request-Payload:

	sessionID (PROD: Cookie, DEV: HEADER)

Backend Resource:

	SignUpResource.createUser()

