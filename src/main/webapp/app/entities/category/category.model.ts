export interface ICategory {
  id: number;
  category_name?: string | null;
}

export type NewCategory = Omit<ICategory, 'id'> & { id: null };
