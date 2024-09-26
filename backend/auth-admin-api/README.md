# auth-admin-api

## DB-Migrationen

werden manuell gemacht.

### Für Entwicklung mittels Flayway

Zuerst dump ziehen:

```
mysqldump --databases authbv --dump-date --add-drop-database -u root -p  -h 172.18.0.2  > V15__authbv_complete_dump.sql
```

Migrationsscripte kommen nach

```
/home/heike/git/konfigurationen/flyway/authbv/sql
```

Anschließend kann man migrieren:

```
sudo /opt/flyway-5.2.4/flyway -configFile=/home/heike/git/konfigurationen/flyway/authbv/conf/flyway.conf migrate
```

### Docker-DB für authprovider

Hier ist es das einfachste, sich hineninzuexecen und das Skript in die CLI zu pasten.


### heikeqs und prod

Scripte in den DB-Container kopieren und von dort in der richtigen Reihenfolge ausführen

### CLIENT-Eintrag


```
INSERT INTO SLZ (ROUNDS,ALGORITHM,VERSION,WERT) values (4098,'SHA-256',1,'d0lxajVjZzkyZGVGbXB0M0t4aWJJZz09');
```

```
INSERT INTO PW (PWHASH,VERSION,SLZ) values ('vNsQNrfviyRO4rNqLRRxB9rj/N1djrZ9L/xmEzFI6Us=',1,755);
```

```
insert into CLIENTS (CLIENT_ID, NAME, ZURUECK_TEXT, REDIRECT_URLS, PID, LOGIN_MIT_LOGINNAME, AGB_URL, JWT_EXPIRATION_MINUTES)
 values ('bv-admin-app','BV Admin', 'zurück zu BV Admin','localhost:4200,localhost:9020/bv-admin-app',755,1,'https://www.egladil.de/datenschutz.html',360);
```

