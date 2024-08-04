# Authprovider für lokale Entwicklung

Läuft als docker-container.

Daher: bei neuer Version: neues image bauen.

## mvn package

Dann quarkus-app nach

(für lokal)
```
/media/veracrypt1/ansible/docker/authprovider/server
```
(für server)
```
/media/veracrypt1/ansible/vserver/quarkus-deployments
```

kopieren

## docker images build

```
cd /media/veracrypt1/ansible/docker/authprovider

docker image build -t heik2718/auth-mariadb ./database
docker image build -t heik2718/authprovider ./server
docker image build -t heik2718/profil-server ./profil-server
docker image build -t heike2718/authprovider-nginx ./nginx
```

## network
docker network create -d bridge auth-network

## Database

docker image build -t heik2718/auth-mariadb .

docker container run -v /home/heike/docker-volumes/auth-database:/var/lib/mysql --name auth-database --network auth-network -e MYSQL_ROOT_PASSWORD_FILE=/run/secrets/mysql-root-pwd -e MYSQL_DATABASE_FILE=/run/secrets/mysql-database -e MYSQL_USER_FILE=/run/secrets/mysql-user -e MYSQL_PASSWORD_FILE=/run/secrets/mysql-user-pwd -d --rm heik2718/auth-mariadb

docker container exec -it  auth-database bash

anderen container im selben network laufen lassen
docker container run -it --network auth-network alpine


Anzeigen der variables, mit dem der server mit dem mariadb image läuft:
docker container run -it --rm mariadb:10 mysqld --verbose --help

## Server
docker image build -t heik2718/authprovider .

docker container run -v /home/heike/docker-volumes/authprovider/dev:/usr/local/bin/authprovider/config -e TZ=Europe/Berlin --name authprovider --network auth-network -p 9000:9000 -d --rm heik2718/authprovider

docker container run -v /home/heike/docker-volumes/authprovider/dev:/usr/local/bin/authprovider/config -e TZ=Europe/Berlin --name authprovider --network auth-network -p 9000:9000 -it --rm heik2718/authprovider

## Checkliste
1) image heik2718/auth-mariadb bauen
2) image heik2718/authprovider bauen
3) db-container starten und dump + insert-Script laufen lassen (landet im gemounteten VOLUME)
4) server-container starten
5) testen
6) container entfernen
7) docker-compose

#########

## Smoketest

[scr,cli]
----
curl -X 'GET' \
  'http://localhost:9020/auth-admin-api/version' \
  -H 'accept: */*'
----

## Nach Spiegelung auf heikeqs CLIENTS.REDIRECT-URLs anpassen 

[script,sql]
----
UPDATE CLIENTS SET REDIRECT_URLS = 'heikeqs/checklistenapp,heikeqs/checklistenapp/listen', BASE_URL = 'http://heikeqs/checklistenapp' WHERE NAME = 'Checklisten';

UPDATE CLIENTS SET REDIRECT_URLS = 'heikeqs/profil-app,heikeqs/profil-app/home,heikeqs/profil-app/profil', BASE_URL = 'http://heikeqs/profil-app' WHERE NAME = 'ProfilApp';

UPDATE CLIENTS SET REDIRECT_URLS = 'heikeqs/mk-admin-app', BASE_URL = 'http://heikeqs/mk-admin-app' WHERE NAME = 'MinikänguruAdmin';

UPDATE CLIENTS SET REDIRECT_URLS = 'heikeqs/mkv-app,heikeqs/mkv-app/dashboard', BASE_URL = 'http://heikeqs/mkv-app' WHERE NAME = 'MinikänguruApp';

UPDATE CLIENTS SET REDIRECT_URLS = 'heikeqs/mja-app', BASE_URL = 'http://heikeqs/mja-app/' WHERE NAME = 'Mathe-jung-alt-App';

UPDATE CLIENTS SET REDIRECT_URLS = 'heikeqs/mja-admin ', BASE_URL = 'http://heikeqs/mja-admin' WHERE NAME = 'Mja-Admin';

UPDATE CLIENTS SET REDIRECT_URLS = 'heikeqs/bv-admin-app', BASE_URL = 'http://heikeqs/bv-admin-app/' WHERE NAME = 'BV Admin';

```

