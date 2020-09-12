# Authentication Provider

Monorepo für die eigene Benutzerverwaltung

## Inhalt

Dies ist ein authentication provider für Anwendungen auf mathe-jung-alt.de, unsichtbare-homepage.de, opa-wetterwachs.de
und ggf. weiteren domains aus dem Winkel-Universum

## 2 Anwendungen

* authprovider: Login authentication für verschiedene Clients
* profil-server: Pflege des eigenen Benutzerkontos (Name, Email, Passwort, Löschen des Kontos) ist ein eigener Client für den authprovider.

## Development Server

Sarten des Servers mit Maven:

	mvn clean compile quarkus:dev

### Root-Resource zum Testen, ob das Backend da ist:

	http://localhost:9000/authprovider/dev

	http://localhost:9000/authprovider/heartbeats?heartbeatId=heartbeat

### Confirm-Url zum Testen:

	http://localhost:9000/authprovider/registration/confirmation?code=96ea688d525f4f1f988902c488625fb8

### curl commands

	curl -X POST -H 'Accept: application/json' -H 'Content-Type: application/json' -i http://localhost:9000/authprovider/temppwd -d '{"email":"zeze@egladil.de","clientCredentials":{"clientId":"WLJLH4vsldWapZrMZi2U5HKRBVpgyUiRTWwX7aiJd8nX","redirectUrl":"localhost:4200"}}'

## Beispiel-Clientanwendung

Ein Showcase für eine Clientanwendung, die diesen AuthProvider nutzt, ist [checklistenapp](https://github.com/heike2718/checklistenapp) mit
[checklistenserver](https://github.com/heike2718/checklistenserver)


# Allgemeines (Abgrenzung zu Identity-Providern)

Der AuthProvider dient ausschließlich zum Authentisieren mittels login-Name / email und Passwort.

Als einzige Daten werden Mailadresse, Loginname, Vor- und Nachname sowie Rollen erhoben. Beim Anlegen eines Benutzerkontos können vom Client Rollen im Query-Parameter groups als kommaseparierte Liste mitgegeben werden.
Alle Benutzerkonten bekommen außerdem die Rolle STANDARD automatisch. D.h. wenn eine Anwendung außer STANDARD keine weiteren Rollen benötigt, kann der Query-Parameter leer bleiben.

__Hinweis:__ Enthält die groups-Liste ADMIN, so wird die Mailadresse desjenigen geloggt, der ein Benutzerkonto anlegen will und die Registrierung mit 403 abgebrochen..

Daten des Benutzerkontos (außer Rollen) können nur in der [profil-app](https://github.com/heike2718/profil-app) geändert werden.

## Workflows

### Registrieren (SignUp)

[Workflow Signup](docs/OAUTH2-SIGNUP-FLOW.md)

### Login (Authentisierung)

[Workflow Login](docs/OAUTH2-LOGIN-FLOW.md)

## Umgang mit dem JWT

### Validierung

* Das JWT und expiresAt wird am bestem im localstorage gespeichert
* expiresAt (= Anzahl Sekunden seit 1.1.1970) kann dann synchron für loggedInGuards verwendet werden, um Navigationsrouten zu enablen/disablen
* Einen HttpRequestInterceptor implementieren, der bei jedem Request das JWT als Authorization Bearer-Header setzt
* im Backend einen AuthorizationFilter implementieren, der bei jedem Request das JWT aus dem Header liest und validiert (mittels JWT-Wrapper)

## AuthProvider-Client anlegen

Jede Anwendung, die AuthProvider nutzen will, muss mittels "insert into CLIENTS"- Skript in der AuthProvider-DB registriert werden und authentisiert sich über eine clientID und ein clientSecret. Beide Credentials dürfen nur von Server zu Server ausgetauscht werden und. Hierfür steht beim AuthProvider die REST-Resource

	http://localhost:9000/authprovider/clients/oauth/token

zur Verfügung, die als Payload ein JSON-Objekt der Form

	{"clientId":"WLJLH4vsldWapZrMZi2U5HKRBVpgyUiRTWwX7aiJd8nX","clientSecret":"start123","nonce":"horst"}

entgegennimmt. Das nonce kann und sollte vom Client in der Form eines CSRF-Tokens verwendet werden, um zu prüfen, ob der Response vom Authprovider unterwegs manipuliert wurde. Es wird vom Authprovider ungeänder im Response-payload zurückgegeben.


## Migration Minikänguru-Konten
Die Tabelle benutzer abgelöst durch USERS:

### Import-Schnittstelle

URL ist

	/authprovider/users/import

Im Unterverzeichnis bv-migration/import im application-config-Verzeichnis liegen die zu importierenden Benutzerdaten als JSON-Dateien, die auf dem anderen Server exportiert wurden. Aufruf der URL importiert die Daten, sofern sie noch nicht vorhanden sind, in die DB des authproviders.

### Export-Schnittstelle

siehe [mkverwaltung](https://gitlab.com/heik2718/de.egladil.mkverwaltung)


## ER-Diagramme

users sind die ResourceOwner, also diejenigen, die sich bei einer Resource authentisieren (Lehrer, Privatpersonen, Anwendungsadmins,...)

[ER-Diagram USERS](docs/datamodel/er-users.png)

Das folgende Bild zeigt das bisherige Datenmodell. Daten der Tabelle BENUTZER werden migriert nach USERS und behalten dadurch die
bisherigen Login-Credetials bei. Die bisherigen Rollenzuordnungen wandern in die Clients des AuthProviders.

[ER-Diagram benutzer](docs/datamodel/er-benutzer.png)

Migration der Minikänguru-Konten mittels Export- Import- Schnittstellen (Export auf Server 1, Import auf Server 2, dazwischen Dateien kopieren).

Die IDs der zu migrierenden Benutzerkonten werden aus der DB mittels select in eine Datei uuid.lst geschrieben, die der Export-Runner verwendet. Die exportierten Benutzerdaten liegen anschließend als JSON-Dateien vor.

### OAuth-Clients- Tabelle

Jede Anwendung, die diesen AuthProvider nutzen möchte, registriert sich (über Mail) als Client. Sie erhält eine client-ID, die bei der
Kommunikation mit dem AuthProvider benötigt. Weitere Attribute werden für den Redirect-Mechanismus benötigt.
Der Client kann konfigurieren, ob er einen von der Mailadresse verschiedenen Loginnamen als Authentisierungsbestandteil verwenden
möchte (LOGIN_MIT_LOGINNAME) und wie lange ein JWT gültig sein soll (JWT_EXPIRATION_MINUTES)


[ER-Diagram CLIENTS](docs/datamodel/er-clients.png)

## Releasenotes

[Release-Notes](RELEASE-NOTES.md)



