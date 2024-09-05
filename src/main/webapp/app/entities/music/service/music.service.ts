import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMusic, NewMusic } from '../music.model';

export type PartialUpdateMusic = Partial<IMusic> & Pick<IMusic, 'id'>;

type RestOf<T extends IMusic | NewMusic> = Omit<T, 'created_At' | 'updated_At'> & {
  created_At?: string | null;
  updated_At?: string | null;
};

export type RestMusic = RestOf<IMusic>;

export type NewRestMusic = RestOf<NewMusic>;

export type PartialUpdateRestMusic = RestOf<PartialUpdateMusic>;

export type EntityResponseType = HttpResponse<IMusic>;
export type EntityArrayResponseType = HttpResponse<IMusic[]>;

@Injectable({ providedIn: 'root' })
export class MusicService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/music');

  create(music: NewMusic): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(music);
    return this.http.post<RestMusic>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(music: IMusic): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(music);
    return this.http
      .put<RestMusic>(`${this.resourceUrl}/${this.getMusicIdentifier(music)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(music: PartialUpdateMusic): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(music);
    return this.http
      .patch<RestMusic>(`${this.resourceUrl}/${this.getMusicIdentifier(music)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMusic>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMusic[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMusicIdentifier(music: Pick<IMusic, 'id'>): number {
    return music.id;
  }

  compareMusic(o1: Pick<IMusic, 'id'> | null, o2: Pick<IMusic, 'id'> | null): boolean {
    return o1 && o2 ? this.getMusicIdentifier(o1) === this.getMusicIdentifier(o2) : o1 === o2;
  }

  addMusicToCollectionIfMissing<Type extends Pick<IMusic, 'id'>>(
    musicCollection: Type[],
    ...musicToCheck: (Type | null | undefined)[]
  ): Type[] {
    const music: Type[] = musicToCheck.filter(isPresent);
    if (music.length > 0) {
      const musicCollectionIdentifiers = musicCollection.map(musicItem => this.getMusicIdentifier(musicItem));
      const musicToAdd = music.filter(musicItem => {
        const musicIdentifier = this.getMusicIdentifier(musicItem);
        if (musicCollectionIdentifiers.includes(musicIdentifier)) {
          return false;
        }
        musicCollectionIdentifiers.push(musicIdentifier);
        return true;
      });
      return [...musicToAdd, ...musicCollection];
    }
    return musicCollection;
  }

  protected convertDateFromClient<T extends IMusic | NewMusic | PartialUpdateMusic>(music: T): RestOf<T> {
    return {
      ...music,
      created_At: music.created_At?.toJSON() ?? null,
      updated_At: music.updated_At?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMusic: RestMusic): IMusic {
    return {
      ...restMusic,
      created_At: restMusic.created_At ? dayjs(restMusic.created_At) : undefined,
      updated_At: restMusic.updated_At ? dayjs(restMusic.updated_At) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMusic>): HttpResponse<IMusic> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMusic[]>): HttpResponse<IMusic[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
