import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IMusic } from '../music.model';

@Component({
  standalone: true,
  selector: 'jhi-music-detail',
  templateUrl: './music-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MusicDetailComponent {
  music = input<IMusic | null>(null);

  previousState(): void {
    window.history.back();
  }
}
