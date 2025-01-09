import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MessageService } from '@bv-admin/shared/messages/api';
import { MatIconModule } from '@angular/material/icon';
import { debounceTime, Subscription, tap } from 'rxjs';


@Component({
  selector: 'bv-admin-message',
  standalone: true,
  imports: [CommonModule, MatIconModule],
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.scss'],
})
export class MessageComponent implements OnInit, OnDestroy {

  messageService = inject(MessageService); 
  
  #messageSubscription: Subscription = new Subscription();

  ngOnInit(): void {
      this.#messageSubscription = this.messageService.message$.pipe(
        debounceTime(3000),
        tap((message) => {
          if (message && message.level === 'INFO') {
            this.close();
          }
        })
        
      ).subscribe();
  }

  ngOnDestroy(): void {
      this.#messageSubscription.unsubscribe();
  }

  close(): void {
    this.messageService.clear();
  }
}
