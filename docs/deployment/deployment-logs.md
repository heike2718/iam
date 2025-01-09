# Deplpyment log für I0076 (komplettes Redisign der Architektur)

## Docker (lokale Entwicklung)

### Datenbank

2 Migrationen und zusätzlich wegen

```
2025-01-09 06:50:48,992 WARN (executor-thread-2) clientId=, correlationId=, [de.egladil.web.authprovider.service.ClientService:245] Possible BOT Attack: redirect url 'http://localhost:9600/benutzerprofil/' fehlt in DB CLIENTS.REDIRECT_URLS (f?hrendes http:// wird ignoriert)
```

__Achtung:__

redirect_urls dürfen kein endendes / haben, da authprovider dieses vor dem Vergleich abschneidet.

```
update CLIENTS set name = 'Benutzerprofil', zurueck_text = 'zuück zu Benutzerprofil', REDIRECT_URLS = 'localhost:9600/benutzerprofil', base_url = 'http://localhost:9600/benutzerprofil/' where ID = 4;
```


### authprovider

Standardkonfiguration gefixed.

Insbesondere müssen target-origin und cors-allowed origins korrekt gesetzt werden:

```
# das ist der origin, mit dem die Requests vom browser gesendet werden
TARGET_ORIGIN=localhost:9000

QUARKUS.HTTP_CORS_ORIGINS=http://loclahost:9000,http://localhost:9600,http://localhost:4200
```

Default-Dinge liegen jetzt unter /deployments bzw. /deployments/logs. 

private- und public-key location werden ab jetzt bereits in application.properties auf /deployments gesetzt, müssen also nicht überschrieben werden!!!!

=> musste Dockerfile anpassen

config/application.properties durch .env ersetzt

docker-volumes für server.log und access.log: Änderung: soll nach /opt/data/authprovider/logs gemounted werden. Auf dem host muss dieses Verzeichnis angelegt werden und dem user 1001 bzw. der Gruppe 1001 erlaubt werden. docker-compose.yaml muss angepasst werden!!!



__smoketest__

http://localhost:9000/authprovider/api/dev

oder 

http://heiketux:9000/authprovider/api/dev



### benutzerprofil

__.env__

Pfade anpassen:

Standardkonfiguration gefixed.

Insbesondere müssen target-origin und cors-allowed origins korrekt gesetzt werden:

```
# das ist der origin, mit dem die Requests vom browser gesendet werden
TARGET_ORIGIN=localhost:9000

QUARKUS.HTTP_CORS_ORIGINS=http://loclahost:9000,http://localhost:9600,http://localhost:4200
```

Default-Dinge liegen jetzt unter /deployments bzw. /deployments/logs. 

config/application.properties durch .env ersetzt

docker-volumes für server.log und access.log: Änderung: soll nach /opt/data/benutzerprofil/logs gemounted werden. Auf dem host muss dieses Verzeichnis angelegt werden und dem user 1001 bzw. der Gruppe 1001 erlaubt werden. docker-compose.yaml muss angepasst werden!!!


### alle Clients von authprovider

die login- bzw. signup- Urls vom Backend enthalten alle noch den # nach der auth-aüpp-URL, müssen also ebenfalls neu deployed werden :/

+ Checklistenserver
+ BV-Admin-Server
+ mk-gateway
