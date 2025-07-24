import { Component, inject, OnInit } from '@angular/core';
import { BenutzerprofilConfiguration } from '@benutzerprofil/configuration';
import { ShellComponent } from './shell/shell.component';
import { AuthFacade } from '@benutzerprofil/auth/api';
import { MatDialog } from '@angular/material/dialog';
import { InfoDialogComponent } from '@ap-ws/common-ui';
import { isLocalStorageAvailable } from '@benutzerprofil/local-storage';

@Component({
  standalone: true,
  imports: [
    ShellComponent
  ],
  selector: 'benutzerprofil-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent implements OnInit {

  configuration = inject(BenutzerprofilConfiguration);
  imageSourceLogo = this.configuration.assetsPath + '/mja_logo_2.svg';
  localStorageWarningDialog = inject(MatDialog);

  #localStorageMessage = 'Ihr Browser kann aktuell keine Daten speichern.' +
    'Dadurch kann es passieren, dass Sie beim Neuladen der Seite (z.B. mit F5) oder beim Benutzen der Zurück-Taste im Browser automatisch abgemeldet werden.' +
    'Bitte prüfen Sie Ihre Browser-Einstellungen und stellen Sie sicher, dass das Speichern von Cookies und Website-Daten erlaubt ist.' +
    'Falls das Problem weiterhin besteht, versuchen Sie es mit einem anderen Browser.' +
    'Ohne Neuladen oder Benutzen der Zurücktaste können Sie die Anwendung wie gewohnt nutzen.';


  #authService = inject(AuthFacade)

  ngOnInit(): void {

    if (!isLocalStorageAvailable()) {
      this.#showDialog('Warnhinweis', this.#localStorageMessage);
    }

    this.#authService.initClearOrRestoreSession();
  }

  #showDialog(title: string, message: string) {

    const dialogRef = this.localStorageWarningDialog.open(InfoDialogComponent, {
      width: '500px',
      disableClose: true,
      data: {
        title: title,
        text: message
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        // ignore result, just close the dialog
      }
    });
  }
}
