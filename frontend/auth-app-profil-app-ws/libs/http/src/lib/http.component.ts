import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'auth-http',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './http.component.html',
  styleUrl: './http.component.scss',
})
export class HttpComponent {}
