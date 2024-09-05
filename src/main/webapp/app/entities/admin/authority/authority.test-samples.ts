import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '05337fa2-7302-4601-9663-7109baad658c',
};

export const sampleWithPartialData: IAuthority = {
  name: '0744016d-5c5a-4290-990f-f40b378e5c5b',
};

export const sampleWithFullData: IAuthority = {
  name: 'c8f39162-7387-43e2-b862-646b231ba5b2',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
