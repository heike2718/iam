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

#### Beim Synchronisieren gibt es noch unterschiedliches Verhalten


Beim bv-admin klappt es: Das Löschen eines Benutzers funktioniert

```
2025-01-11 20:01:40,093 INFO (executor-thread-2) clientId=, correlationId=, [de.egladil.web.bv_admin.domain.auth.login.LoginLogoutService:71] idToken=fe032b0c...
2025-01-11 20:01:40,396 INFO (executor-thread-2) clientId=, correlationId=, [de.egladil.web.bv_admin.domain.auth.session.SessionService:100] User eingeloggt: AuthenticatedUser [uuid=68be767f..., roles=[ADMIN, AUTH_ADMIN, AUTOR, STANDARD]]
2025-01-11 20:01:59,904 INFO (executor-thread-2) clientId=, correlationId=, [de.egladil.web.bv_admin.domain.events.PropagateEventService:115] sync: mkGatewayResponse.status=200
2025-01-11 20:01:59,930 INFO (executor-thread-2) clientId=, correlationId=, [de.egladil.web.bv_admin.domain.benutzer.BenutzerService:184] delete 281b5a28-c80e-41a4-a113-87d7492e0c9c synchronized with mk-gateway
```

Beim Ändern von Daten über Benutzerprofil wird dieses über authprovider an mk-gateway propagiert, aber das klappt nicht:

```
22025-01-11 17:14:53,507 INFO (executor-thread-1) clientId=, correlationId=, [de.egladil.web.authprovider.endpoints.TokenExchangeResource:68] OTT exchanged:INFO
2025-01-11 17:15:02,831 INFO (executor-thread-1) clientId=, correlationId=, [de.egladil.web.authprovider.service.profile.ChangeDataService:95] ResourceOwner [uuid=68be767f..., fullName=Dr. Heike Winkelvoß, l
oginName=ruth-68, email=public@egladil.de]: Daten geaendert
2025-01-11 17:15:03,467 INFO (executor-thread-1) clientId=, correlationId=, [de.egladil.web.authprovider.event.AuthproviderEventHandler:316] sende UserChanged für ResourceOwnerEventPayload [uuid=68be767f-64d
b-43a0-9437-43f197e80a58, vorname=Dr. Heike, nachname=Winkelvoß] an mk-gateway
2025-01-11 17:15:06,488 ERROR (executor-thread-1) clientId=, correlationId=, [de.egladil.web.authprovider.event.AuthproviderEventHandler:351] WebApplicationException beim Propagieren des CreateUserEvents: st
atus=404 - Received: 'Not Found, status code 404' when invoking REST Client method: 'de.egladil.web.authprovider.restclient.MkGatewayRestClient#propagateUserChanged': org.jboss.resteasy.reactive.ClientWebApp
licationException: Received: 'Not Found, status code 404' when invoking REST Client method: 'de.egladil.web.authprovider.restclient.MkGatewayRestClient#propagateUserChanged'
	at org.jboss.resteasy.reactive.client.impl.RestClientRequestContext.unwrapException(RestClientRequestContext.java:205)
	at org.jboss.resteasy.reactive.common.core.AbstractResteasyReactiveContext.handleException(AbstractResteasyReactiveContext.java:331)
	at org.jboss.resteasy.reactive.common.core.AbstractResteasyReactiveContext.run(AbstractResteasyReactiveContext.java:175)
	at org.jboss.resteasy.reactive.client.impl.RestClientRequestContext$1.lambda$execute$0(RestClientRequestContext.java:324)
	at io.vertx.core.impl.ContextInternal.dispatch(ContextInternal.java:270)
	at io.vertx.core.impl.ContextInternal.dispatch(ContextInternal.java:252)
	at io.vertx.core.impl.ContextInternal.lambda$runOnContext$0(ContextInternal.java:50)
	at io.netty.util.concurrent.AbstractEventExecutor.runTask(AbstractEventExecutor.java:173)
	at io.netty.util.concurrent.AbstractEventExecutor.safeExecute(AbstractEventExecutor.java:166)
	at io.netty.util.concurrent.SingleThreadEventExecutor.runAllTasks(SingleThreadEventExecutor.java:469)
	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:566)
	at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:994)
	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
	at java.base/java.lang.Thread.run(Thread.java:840)
Caused by: jakarta.ws.rs.WebApplicationException: Not Found, status code 404
	at io.quarkus.arc.impl.AroundInvokeInvocationContext.proceed(AroundInvokeInvocationContext.java:73)
	at io.quarkus.arc.impl.AroundInvokeInvocationContext.proceed(AroundInvokeInvocationContext.java:62)
	at io.smallrye.faulttolerance.FaultToleranceInterceptor.lambda$syncFlow$3(FaultToleranceInterceptor.java:267)
	at io.smallrye.faulttolerance.core.InvocationContext.call(InvocationContext.java:19)

```

#### Das Loggen in authprovider ist noch krumplig

es werden sämtliche SQL-Statements geloggt nebst bind variables. Möglicherweise ist aber nur das console.log im container betroffen, denn im server.log findet man keine SQLs und bind variables.

Muss ich nichmal schauen, wenn ich nicht mehr ganz so müde bin.


