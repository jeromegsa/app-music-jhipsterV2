import dayjs from 'dayjs/esm';

import { IMusic, NewMusic } from './music.model';

export const sampleWithRequiredData: IMusic = {
  id: 31473,
};

export const sampleWithPartialData: IMusic = {
  id: 29644,
  paroles: 'souvent police sus',
  created_At: dayjs('2024-09-04T19:08'),
  updated_At: dayjs('2024-09-05T01:34'),
};

export const sampleWithFullData: IMusic = {
  id: 12433,
  title: 'soit',
  duration: 25663,
  paroles: 'sans sitôt compenser',
  created_At: dayjs('2024-09-04T14:30'),
  updated_At: dayjs('2024-09-04T19:47'),
};

export const sampleWithNewData: NewMusic = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
