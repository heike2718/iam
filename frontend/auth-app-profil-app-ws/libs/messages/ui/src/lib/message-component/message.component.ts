import { Component, inject, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MessageService } from '@ap-ws/messages/api';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'auth-common-message',
  standalone: true,
  imports: [CommonModule, MatIconModule],
  templateUrl: './message.component.html',
  styleUrl: './message.component.scss',
})
export class MessageComponent {
  
  messageService = inject(MessageService);

  close(): void {
    this.messageService.clearMessage();
  }
}
