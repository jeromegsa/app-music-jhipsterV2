import { ICategory } from 'app/entities/category/category.model';

export interface IAlbum {
  id: number;
  name?: string | null;
  tags?: string | null;
  description?: string | null;
  nbr_music?: number | null;
  author?: string | null;
  image_url?: string | null;
  album_category?: ICategory | null;
}

export type NewAlbum = Omit<IAlbum, 'id'> & { id: null };
