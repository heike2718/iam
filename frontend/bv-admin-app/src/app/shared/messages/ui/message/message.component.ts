import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MessageService } from '@bv-admin-app/shared/messages/api';
import { MatIconModule } from '@angular/material/icon';


@Component({
  selector: 'bv-admin-message',
  standalone: true,
  imports: [CommonModule, MatIconModule],
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.scss'],
})
export class MessageComponent {

  messageService = inject(MessageService); 

  close(): void {
    this.messageService.clear();
  }
}
