import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMusic, NewMusic } from '../music.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMusic for edit and NewMusicFormGroupInput for create.
 */
type MusicFormGroupInput = IMusic | PartialWithRequiredKeyOf<NewMusic>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMusic | NewMusic> = Omit<T, 'created_At' | 'updated_At'> & {
  created_At?: string | null;
  updated_At?: string | null;
};

type MusicFormRawValue = FormValueOf<IMusic>;

type NewMusicFormRawValue = FormValueOf<NewMusic>;

type MusicFormDefaults = Pick<NewMusic, 'id' | 'created_At' | 'updated_At'>;

type MusicFormGroupContent = {
  id: FormControl<MusicFormRawValue['id'] | NewMusic['id']>;
  title: FormControl<MusicFormRawValue['title']>;
  duration: FormControl<MusicFormRawValue['duration']>;
  paroles: FormControl<MusicFormRawValue['paroles']>;
  created_At: FormControl<MusicFormRawValue['created_At']>;
  updated_At: FormControl<MusicFormRawValue['updated_At']>;
  album: FormControl<MusicFormRawValue['album']>;
};

export type MusicFormGroup = FormGroup<MusicFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MusicFormService {
  createMusicFormGroup(music: MusicFormGroupInput = { id: null }): MusicFormGroup {
    const musicRawValue = this.convertMusicToMusicRawValue({
      ...this.getFormDefaults(),
      ...music,
    });
    return new FormGroup<MusicFormGroupContent>({
      id: new FormControl(
        { value: musicRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(musicRawValue.title),
      duration: new FormControl(musicRawValue.duration),
      paroles: new FormControl(musicRawValue.paroles),
      created_At: new FormControl(musicRawValue.created_At),
      updated_At: new FormControl(musicRawValue.updated_At),
      album: new FormControl(musicRawValue.album),
    });
  }

  getMusic(form: MusicFormGroup): IMusic | NewMusic {
    return this.convertMusicRawValueToMusic(form.getRawValue() as MusicFormRawValue | NewMusicFormRawValue);
  }

  resetForm(form: MusicFormGroup, music: MusicFormGroupInput): void {
    const musicRawValue = this.convertMusicToMusicRawValue({ ...this.getFormDefaults(), ...music });
    form.reset(
      {
        ...musicRawValue,
        id: { value: musicRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MusicFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      created_At: currentTime,
      updated_At: currentTime,
    };
  }

  private convertMusicRawValueToMusic(rawMusic: MusicFormRawValue | NewMusicFormRawValue): IMusic | NewMusic {
    return {
      ...rawMusic,
      created_At: dayjs(rawMusic.created_At, DATE_TIME_FORMAT),
      updated_At: dayjs(rawMusic.updated_At, DATE_TIME_FORMAT),
    };
  }

  private convertMusicToMusicRawValue(
    music: IMusic | (Partial<NewMusic> & MusicFormDefaults),
  ): MusicFormRawValue | PartialWithRequiredKeyOf<NewMusicFormRawValue> {
    return {
      ...music,
      created_At: music.created_At ? music.created_At.format(DATE_TIME_FORMAT) : undefined,
      updated_At: music.updated_At ? music.updated_At.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
