import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAlbum } from 'app/entities/album/album.model';
import { AlbumService } from 'app/entities/album/service/album.service';
import { IMusic } from '../music.model';
import { MusicService } from '../service/music.service';
import { MusicFormGroup, MusicFormService } from './music-form.service';

@Component({
  standalone: true,
  selector: 'jhi-music-update',
  templateUrl: './music-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MusicUpdateComponent implements OnInit {
  isSaving = false;
  music: IMusic | null = null;

  albumsSharedCollection: IAlbum[] = [];

  protected musicService = inject(MusicService);
  protected musicFormService = inject(MusicFormService);
  protected albumService = inject(AlbumService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MusicFormGroup = this.musicFormService.createMusicFormGroup();

  compareAlbum = (o1: IAlbum | null, o2: IAlbum | null): boolean => this.albumService.compareAlbum(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ music }) => {
      this.music = music;
      if (music) {
        this.updateForm(music);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const music = this.musicFormService.getMusic(this.editForm);
    if (music.id !== null) {
      this.subscribeToSaveResponse(this.musicService.update(music));
    } else {
      this.subscribeToSaveResponse(this.musicService.create(music));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMusic>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(music: IMusic): void {
    this.music = music;
    this.musicFormService.resetForm(this.editForm, music);

    this.albumsSharedCollection = this.albumService.addAlbumToCollectionIfMissing<IAlbum>(this.albumsSharedCollection, music.album);
  }

  protected loadRelationshipsOptions(): void {
    this.albumService
      .query()
      .pipe(map((res: HttpResponse<IAlbum[]>) => res.body ?? []))
      .pipe(map((albums: IAlbum[]) => this.albumService.addAlbumToCollectionIfMissing<IAlbum>(albums, this.music?.album)))
      .subscribe((albums: IAlbum[]) => (this.albumsSharedCollection = albums));
  }
}
