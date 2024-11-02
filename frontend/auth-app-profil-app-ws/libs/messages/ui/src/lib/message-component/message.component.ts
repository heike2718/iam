import { Component, inject, Inject, OnDestroy, OnInit } from '@angular/core';
import { AsyncPipe, CommonModule } from '@angular/common';
import { MessageService } from '@ap-ws/messages/api';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'auth-common-message',
  standalone: true,
  imports: [CommonModule, MatIconModule, AsyncPipe],
  templateUrl: './message.component.html',
  styleUrl: './message.component.scss',
})
export class MessageComponent {

  messageService = inject(MessageService);

  close(): void {
    this.messageService.clearMessage();
  }


}
