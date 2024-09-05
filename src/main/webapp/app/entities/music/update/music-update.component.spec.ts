import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IAlbum } from 'app/entities/album/album.model';
import { AlbumService } from 'app/entities/album/service/album.service';
import { MusicService } from '../service/music.service';
import { IMusic } from '../music.model';
import { MusicFormService } from './music-form.service';

import { MusicUpdateComponent } from './music-update.component';

describe('Music Management Update Component', () => {
  let comp: MusicUpdateComponent;
  let fixture: ComponentFixture<MusicUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let musicFormService: MusicFormService;
  let musicService: MusicService;
  let albumService: AlbumService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MusicUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(MusicUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MusicUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    musicFormService = TestBed.inject(MusicFormService);
    musicService = TestBed.inject(MusicService);
    albumService = TestBed.inject(AlbumService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Album query and add missing value', () => {
      const music: IMusic = { id: 456 };
      const album: IAlbum = { id: 1897 };
      music.album = album;

      const albumCollection: IAlbum[] = [{ id: 19777 }];
      jest.spyOn(albumService, 'query').mockReturnValue(of(new HttpResponse({ body: albumCollection })));
      const additionalAlbums = [album];
      const expectedCollection: IAlbum[] = [...additionalAlbums, ...albumCollection];
      jest.spyOn(albumService, 'addAlbumToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ music });
      comp.ngOnInit();

      expect(albumService.query).toHaveBeenCalled();
      expect(albumService.addAlbumToCollectionIfMissing).toHaveBeenCalledWith(
        albumCollection,
        ...additionalAlbums.map(expect.objectContaining),
      );
      expect(comp.albumsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const music: IMusic = { id: 456 };
      const album: IAlbum = { id: 26658 };
      music.album = album;

      activatedRoute.data = of({ music });
      comp.ngOnInit();

      expect(comp.albumsSharedCollection).toContain(album);
      expect(comp.music).toEqual(music);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMusic>>();
      const music = { id: 123 };
      jest.spyOn(musicFormService, 'getMusic').mockReturnValue(music);
      jest.spyOn(musicService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ music });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: music }));
      saveSubject.complete();

      // THEN
      expect(musicFormService.getMusic).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(musicService.update).toHaveBeenCalledWith(expect.objectContaining(music));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMusic>>();
      const music = { id: 123 };
      jest.spyOn(musicFormService, 'getMusic').mockReturnValue({ id: null });
      jest.spyOn(musicService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ music: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: music }));
      saveSubject.complete();

      // THEN
      expect(musicFormService.getMusic).toHaveBeenCalled();
      expect(musicService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMusic>>();
      const music = { id: 123 };
      jest.spyOn(musicService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ music });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(musicService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAlbum', () => {
      it('Should forward to albumService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(albumService, 'compareAlbum');
        comp.compareAlbum(entity, entity2);
        expect(albumService.compareAlbum).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
