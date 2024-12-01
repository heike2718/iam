# Neue Base-URLs

Ziel: Einsparung eines reverse proxies => Vereinfachte Konfiguration des Webservers

## 1. bv-admin-app / auth-admin-api

Neue base-URL: benutzerverwaltung , benutzerverwaltung/api

### Auswirkungen

| Anwendung | Änderungen | Files |
|-----------|------------|-------|
| Authprovider | name und redirect urls des clients | sql-script |
| Apache | Konfiguration reverse proxy | conf-Files |

## 2. mkbiza-app

Neue base-URL: mkbiza , mkbiza/api

### Auswirkungen

| Anwendung | Änderungen | Files |
|-----------|------------|-------|
| Authprovider | name und redirect urls des clients | sql-script |
| mja-app | noch nicht bekannt | noch nicht bekannt |
| mkv-app | Bewertungsformular | große Änderungen |
| Website Minikänguru | links im Archiv | archiv.html | 
| Apache | Konfiguration reverse proxy | conf-Files |



## 2. checklistenapp / checklistenserver

Neue base-URL: checklisten , checklisten/api

### Auswirkungen

| Anwendung | Änderungen | Files |
|-----------|------------|-------|
| Authprovider | Link zur profil-app, name und redirect urls des clients | environment.ts, sql-script |
| Apache | Konfiguration reverse proxy | conf-Files |

__Aktuell lässt sich checklistenapp nicht mehr bauen__ Das ist ein Showstopper. Erst wenn checklisten neu released wreden könnten, kann auth-app / authprovider und profil-app / profil-server ändern!!!!


__Aktuell lässt sich checklistenapp nicht mehr bauen__ Das ist ein Showstopper. Erst wenn checklisten neu released wreden könnten, kann auth-app / authprovider und profil-app / profil-server ändern!!!!

## profil-app / profil-server

Neue base-URL: benutzerprofil und benutzerprofil/api

### Auswirkungen

| Anwendung | Änderungen | Files |
|-----------|------------|-------|
| Minikänguru | Link zur profil-app | environment.ts |
| Authprovider | Link zur profil-app, name und redirect urls des clients | environment.ts, sql-script |
| Apache | Konfiguration reverse proxy | conf-Files |


## auth-app / authprovider

Neue base-URL: identity, identity/api

### Auswirkungen

| Anwendung | Änderungen | Files |
|-----------|------------|-------|
| Minikänguru | Link zur auth-app, REST-Client-Configuration | environment.ts, application.properties, .env |
| Minkänguru-Administration | Link zur auth-app, REST-Client-Configuration | environment.ts, application.properties, .env |
| Checklistenapp | Link zur auth-app, REST-Client-Configuration | environment.ts, application.properties, .env |
| Mathe-jung-Alt-app | Link zur auth-app, REST-Client-Configuration | environment.ts, application.properties, .env |
| BV-Administration | Link zur auth-app, REST-Client-Configuration | environment.ts, application.properties, .env |
| Apache | Konfiguration reverse proxy | conf-Files |



