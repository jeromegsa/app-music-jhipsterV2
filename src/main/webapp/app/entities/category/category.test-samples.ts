import { ICategory, NewCategory } from './category.model';

export const sampleWithRequiredData: ICategory = {
  id: 31734,
};

export const sampleWithPartialData: ICategory = {
  id: 20620,
};

export const sampleWithFullData: ICategory = {
  id: 1187,
  category_name: 'au-dedans de rose rudement',
};

export const sampleWithNewData: NewCategory = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
