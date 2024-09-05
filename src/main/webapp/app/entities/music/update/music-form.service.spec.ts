import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../music.test-samples';

import { MusicFormService } from './music-form.service';

describe('Music Form Service', () => {
  let service: MusicFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MusicFormService);
  });

  describe('Service methods', () => {
    describe('createMusicFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMusicFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            duration: expect.any(Object),
            paroles: expect.any(Object),
            created_At: expect.any(Object),
            updated_At: expect.any(Object),
            album: expect.any(Object),
          }),
        );
      });

      it('passing IMusic should create a new form with FormGroup', () => {
        const formGroup = service.createMusicFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            duration: expect.any(Object),
            paroles: expect.any(Object),
            created_At: expect.any(Object),
            updated_At: expect.any(Object),
            album: expect.any(Object),
          }),
        );
      });
    });

    describe('getMusic', () => {
      it('should return NewMusic for default Music initial value', () => {
        const formGroup = service.createMusicFormGroup(sampleWithNewData);

        const music = service.getMusic(formGroup) as any;

        expect(music).toMatchObject(sampleWithNewData);
      });

      it('should return NewMusic for empty Music initial value', () => {
        const formGroup = service.createMusicFormGroup();

        const music = service.getMusic(formGroup) as any;

        expect(music).toMatchObject({});
      });

      it('should return IMusic', () => {
        const formGroup = service.createMusicFormGroup(sampleWithRequiredData);

        const music = service.getMusic(formGroup) as any;

        expect(music).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMusic should not enable id FormControl', () => {
        const formGroup = service.createMusicFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMusic should disable id FormControl', () => {
        const formGroup = service.createMusicFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
