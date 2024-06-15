# auth-admin-api

## DB-Migrationen

werden manuell gemacht.

### Für Entwicklung mittels Flayway

Zuerst dump ziehen:

```
mysqldump --databases authbv --dump-date --add-drop-database -u root -p  -h 172.21.0.2  > V15__authbv_complete_dump.sql
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