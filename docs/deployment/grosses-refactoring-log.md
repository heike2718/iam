# Komplette Umstrukturierung des authproviders

Ziel: alle Quarkus-Anwendungen serven die SPA selbst. Daher wird kein hashing in den Angular-Anwendungen mehr benötigt.

Betroffen sind

+ authprovider: Name passt. die app wird umbenannt in authprovider
+ bv-admin-app / auth-admin-api: werden umbenannt in bv-admin, Pfade in den Rest-Clients müssen geändert werden: benötigen ein api/
+ benutzerprofil ist bereits umbenannt, aber es sind die Pfade in den RestClients zu ändern: benötigen ein api/
  
__Minikänguru:__

mk-gateway: Rest-Clients müssen geändert werden. Login-,-Signup- und Links in Mails müssen geändert werden

mkbiza ist nicht betroffen, da es per se keine authentifizierung benötigt.


__Mathe-jung-alt:__

Rest-Clients müssen geändert werden. Login-,-Signup- und Links in Mails müssen geändert werden. 

Angular: link zu benutzerprofil ändern.

__Checklistenapp:__

wie bei Minikänguru, falls das S2S ohnehin noch nicht geändert wurde.

## Plan

### Schritt 1

Umbenennung auth-app im nx-Monorepo
SPARoute-Filter
Links in benutzerprofil ändern
Links in auth-admin-api ändern

Testen, ob das alles funktioniert

### Schritt 2

Links in mk-gateway und mkv-app ändern
Links in mja-api ändern
Links in checklistenapp ändern

Testen, ob alles lokal funktioniert

### Schritt 3

deployment auf docker (komplett)
deployment auf x300 (komplett)

Im Schritt x300: akribisch Änderungslog an den Konfigurationen führen


### Schritt 4

Gleichzeitiges deployment von authprovider, benutzerprofil, mk-gateway. mkv-app, mja und auth-admin-api in PROD.
