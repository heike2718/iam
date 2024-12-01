# Nächste Schritte

## 2024-Q4

bump authprovider to Angular 18 / 19

Muss minimalinvasiv für die Clients erfolgen

### Benutzerprofil

Momentan ersetzt auf dem branch profil-api den profil-server, profil-app wird nur innen runderneuert.

Daher ändert sich an den Links in der mkv-app und in auth-app nichts

=> nach Abschluss der Arbeiten muss dann nur der reverse-proxy-Eintrag für profil-server durch profil-api ersetzt werden. Theoretisch kann profil-api dann die profil-app hosten, so dass das deployment vereinfacht sird und die profil-app aus /var/www/html verschwinden kann.

### Identity

Hier darf sich am Verhalten gegenüber den anderen Anwendungen __nichts__ ändern, weil sich einige aktuell nicht mehr releasen lassen und es zu viele wären. Siehe [Umbenennungen base-URLs](./umbenennungen-base-urls.md)

D.h. auth-app bleibt auth-app, authprovider bleibt authprovider. Deployment bleibt hybrid? Also backend => server mit Quarkus-authprovider und Frontend statische site unter /var/www/html

## 2025-Q1 und Q2

Benutzerverwaltung kann umgebaut werden siehe [Umbenennung Base-URL](./umbenennungen-base-urls.md)

Checklistenapp muss komplett erneuert werden. Dies wäre ein show-Stopper für die Umbenennungen von authprovider.




