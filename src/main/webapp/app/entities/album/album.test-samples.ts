import { IAlbum, NewAlbum } from './album.model';

export const sampleWithRequiredData: IAlbum = {
  id: 3664,
};

export const sampleWithPartialData: IAlbum = {
  id: 13271,
  name: 'sauf apparemment',
  tags: 'environ trancher aussitôt que',
  description: 'commissionnaire sitôt que démontrer',
  image_url: 'rédiger divinement',
};

export const sampleWithFullData: IAlbum = {
  id: 1459,
  name: 'si bien que de manière à ce que',
  tags: 'de peur de',
  description: 'rouler joliment',
  nbr_music: 21927,
  author: 'absolument moyennant environ',
  image_url: 'conseil municipal clientèle',
};

export const sampleWithNewData: NewAlbum = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
