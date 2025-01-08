# Deplpyment log für I0076 (komplettes Redisign der Architektur)

## Docker (lokale Entwicklung)

### Datenbank

2 Migrationen und zusätzlich

```
update CLIENTS set name = 'Benutzerprofil', zurueck_text = 'zuück zu Benutzerprofil', REDIRECT_URLS = 'localhost:9600/benutzerprofil/', base_url = 'http://localhost:9600/benutzerprofil/' where ID = 4;
```

### authprovider

Standardkonfiguration gefixed

Default-Dinge liegen jetzt unter /deployments bzw. /deployments/logs. 

=> musste Dockerfile anpassen

config/application.properties durch .env ersetzt

docker-volumes für server.log und access.log: Änderung: soll nach /opt/data/authprovider/logs. Auf dem host muss dieses Verzeichnis angelegt werden und dem user 1001 bzw. der Gruppe 1001 erlaubt werden

__smoketest__

http://localhost:9000/authprovider/api/dev

oder 

http://heiketux:9000/authprovider/api/dev



### benutzerprofil

__.env__

Pfade anpassen:

AUTH_APP_URL=http://localhost:80/auth-app
PUBLIC_REDIRECT_URL=http://localhost:9600/benutzerprofil/
QUARKUS_REST_CLIENT_AUTHPROVIDER_URL=http://localhost:9000/authprovider

### alle Clients von authprovider

die login- bzw. signup- Urls vom Backend enthalten alle noch den # nach der auth-aüpp-URL, müssen also ebenfalls neu deployed werden :/

+ Checklistenserver
+ BV-Admin-Server
+ mk-gateway
