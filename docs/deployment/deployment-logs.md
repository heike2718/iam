# Deployment log für I0076 (komplettes Redisign der Architektur)

## Docker (lokale Entwicklung)

### 1 authprovider und co

#### 1.1 Datenbank

2 Migrationen und zusätzlich wegen

```
2025-01-09 06:50:48,992 WARN (executor-thread-2) clientId=, correlationId=, [de.egladil.web.authprovider.service.ClientService:245] Possible BOT Attack: redirect url 'http://localhost:9600/benutzerprofil/' fehlt in DB CLIENTS.REDIRECT_URLS (f?hrendes http:// wird ignoriert)
```

__Achtung:__

redirect_urls dürfen kein endendes / haben, da authprovider dieses vor dem Vergleich abschneidet.

Für die lokale Entwicklung von benutzerprofil fehlt 'localhost:4400'. Für die lokale Entwicklung anderer Angular-Anwendungen fehlt localhost:4200

```
update CLIENTS set name = 'Benutzerprofil', zurueck_text = 'zurück zu Benutzerprofil', REDIRECT_URLS = 'localhost:9600/benutzerprofil,localhost:4200,localhost:4400', base_url = 'http://localhost:9600/benutzerprofil/' where ID = 4;
```


#### 1.2 authprovider (/)

Standardkonfiguration gefixed.

Insbesondere müssen target-origin und cors-allowed origins korrekt gesetzt werden:

```
# das ist der origin, mit dem die Requests vom browser gesendet werden
TARGET_ORIGIN=localhost:9000

QUARKUS.HTTP_CORS_ORIGINS=http://loclahost:9000,http://localhost:9600,http://localhost:4200
```

Es gab noch ein issue mit der TEMP_PWD_URL in der .env!!! Da fehlte ein authprovider. 
Wegen Unwägbarkeiten mit der baseURI : __neuer config-Parameter account.activation.url__

Default-Dinge liegen jetzt unter /deployments bzw. /deployments/logs. 

private- und public-key location werden ab jetzt bereits in application.properties auf /deployments gesetzt, müssen also nicht überschrieben werden!!!!

=> musste Dockerfile anpassen

config/application.properties durch .env ersetzt

docker-volumes für server.log und access.log: Änderung: soll nach /opt/data/authprovider/logs gemounted werden. Auf dem host muss dieses Verzeichnis angelegt werden und dem user 1001 bzw. der Gruppe 1001 erlaubt werden. docker-compose.yaml muss angepasst werden!!!

__Deployment:__ 

1. app bauen
2. nach src/main/resources/META-INF/resources kopieren
3. authprovider packagen
4. quarkus-apps nach /media/veracrypt1/ansible/docker/authprovider/authprovider kopieren
5. .env prüfen
6. Dockerfile prüfen (wegen /deployments/logs)
7. docker-compose.yaml prüfen (wegen volume für /deployment/logs)
8. image neu bauen: docker image build -t heik2718/authprovider ./authprovider



__smoketest__

http://localhost:9000/authprovider/api/dev

oder 

http://heiketux:9000/authprovider/api/dev



#### 1.3 benutzerprofil (/)

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

__Deployment:__ 

1. app bauen
2. nach src/main/resources/META-INF/resources kopieren
3. authprovider packagen
4. quarkus-apps nach /media/veracrypt1/ansible/docker/authprovider/benutzerprofil kopieren
5. .env prüfen
6. Dockerfile prüfen (wegen /deployments/logs)
7. docker-compose.yaml prüfen (wegen volume für /deployment/logs)
6. image neu bauen: docker image build -t heik2718/benutzerprofil ./benutzerprofil

#### 1.4 bv-admin (/)

```
update CLIENTS set name = 'BV-Admin', base_url = 'localhost:4200', redirect_urls='localhost:4200,localhost:9020/bv-admin', zurueck_text = 'zurück zu BV-Admin'  where id = 9;
```

AuthProviderRestClient:

@Path("authprovider/api")

Beim Anfordern des client access tokens gab es eine 400 vom authprovider unter docker

```
2025-01-09 10:06:23,023 INFO (vert.x-eventloop-thread-13) clientId=, correlationId=, [de.egladil.web.authprovider.filters.OriginReferrerFilter:48] POST : /api/clients/client/accesstoken
2025-01-09 10:06:23,026 ERROR (executor-thread-8) clientId=, correlationId=, [de.egladil.web.authprovider.filters.ValidationReportResponseFilter:67] MessagePayload [level=ERROR, message={de.egladil.constraints.invalidChars}]
```

_Grund:_ die clientId erlaubt keine Minus und in .env stand noch was mit ueberschreiben-mit-...

__Deployment:__ 

wird nicht auf lokalen docker deployed

### 2 Minikänguru (/)

Pfade zu logs geändert. => Dockerfile und docker-compose.yaml muss angepasst werden. Zielverzeichnis: /opt/data/mk-kataloge/logs bzw. /opt/data/mk-gateway/logs

#### 2.1 Docker container für mk-gateway und mk-kataloge für Lokale Entwicklung

__dies wird auf später verschoben!!!__

/media/veracrypt1/ansible/docker/minikaenguru

Es werden nur die backendservices deployed

__Deployment:__ 

3. mk-gateway packagen
4. quarkus-apps nach /media/veracrypt1/ansible/docker/minikaenguru/mk-gateway kopieren
5. .env prüfen
6. image bauen: docker image build -t heik2718/benutzerprofil ./benutzerprofil


### 3 Checklisten

Nur den server. Das wird ein Blindspiel, weil die app sich nicht mehr bauen lässt.

Pfade zu logs geändert. => Dockerfile und docker-compose.yaml muss angepasst werden. Zielverzeichnis: /opt/data/checklisten/logs

__Deployment:__

wird nicht auf lokalen docker deployed


### 4 Mathe-jung-alt

Pfade zu logs geändert. => Dockerfile und docker-compose.yaml muss angepasst werden. Zielverzeichnis: /opt/data/mathe-jung-alt/logs

localhost:9210/mja-app fehlte in redirect_urls in docker-DB

__Deployment:__

wird nicht auf lokalen docker deployed


## x300 (QS)

### 1 authprovider und co

Die Files liegen jetzt hier:

```
cd /media/veracrypt1/ansible/vserver/roles/x300-heikeqs/files/iam
```

2 DB-Migrationsskripte müssen nach iam/datenbank/dumps

Daher müssen auch alle tasks umgezogen und die dortigen Pfade angepasst werden.

Für alle 3 Anwendungen:

1. app bauen
2. nach src/main/resources/META-INF/resources kopieren
3. packagen
4. quarkus-apps nach /media/veracrypt1/ansible/vserver/roles/x300-heikeqs/files/iam in das jeweilige gleichnamige Unterverzeichnis kopieren
5. .env anpassen
6. Dockerfile anpassen
7. docker-compose anpassen


CLIENTS:

```
update CLIENTS set zurueck_text = 'zurück zu BV-Admin', base_url = 'http://heikeqs/bv-admin/', redirect_urls = 'heikeqs/bv-admin' where id = 14;
```


#### ansible-Tasks

neue Task zum anlegen und permission setzen von 

/opt/data/authprovider/logs
/opt/data/benutzerprofil/logs
/opt/data/bv-admin/logs

imagenamen haben sich geändert!!!!


docker container rm auth-admin-api authprovider profil-server auth-database
docker image rm heik2718/profil-server
heik2718/auth-admin-api


#### Konfigurationstests

auf dem server:

```
curl -X GET -i http://localhost:9600/benutzerprofil/api/about

curl -X GET -i http://localhost:9000/authprovider/api/about

curl -X GET -i http://localhost:9020/bv-admin/api/about
curl -X GET -i http://localhost:9020/api/about

```

im Browser

```
{
  "clientId": "MNybdaIwsd0hdzxIobYDY64yQUFjD19LhSuLwLfcSy0P", 
  "clientSecret": "XfUHHaEn2Gm1JPcz3TRV", 
  "nonce": "nonce-value-12345"
}
```

```
http://heikeqs/authprovider/api/about/

curl -X POST -H 'Content-Type: application/json' -H 'Accept: application/json' -i http://heikeqs/authprovider/api/clients/client/accesstoken --data '{
  "clientId": "MNybdaIwsd0hdzxIobYDY64yQUFjD19LhSuLwLfcSy0P", 
  "clientSecret": "XfUHHaEn2Gm1JPcz3TRV", 
  "nonce": "nonce-value-12345"
}'
```

### checklistenserver

#### Umkonfigurieren der logs

x300-00-opt-data.yml
Dockerfile und docker-compose.yaml umkonfigurieren!!!
.env anpassen

```
cd /home/drpwzrd/docker-volumes/
sudo chown -R 1001 checklisten
sudo chgrp -R 1001 checklisten
```

