import dayjs from 'dayjs/esm';
import { IAlbum } from 'app/entities/album/album.model';

export interface IMusic {
  id: number;
  title?: string | null;
  duration?: number | null;
  paroles?: string | null;
  created_At?: dayjs.Dayjs | null;
  updated_At?: dayjs.Dayjs | null;
  album?: IAlbum | null;
}

export type NewMusic = Omit<IMusic, 'id'> & { id: null };
