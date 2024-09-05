import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IAlbum } from '../album.model';

@Component({
  standalone: true,
  selector: 'jhi-album-detail',
  templateUrl: './album-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AlbumDetailComponent {
  album = input<IAlbum | null>(null);

  previousState(): void {
    window.history.back();
  }
}
