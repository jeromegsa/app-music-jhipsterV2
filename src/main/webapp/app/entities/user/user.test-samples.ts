import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 26877,
  login: '@1t',
};

export const sampleWithPartialData: IUser = {
  id: 30239,
  login: 'IN',
};

export const sampleWithFullData: IUser = {
  id: 3562,
  login: 'zH7',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
