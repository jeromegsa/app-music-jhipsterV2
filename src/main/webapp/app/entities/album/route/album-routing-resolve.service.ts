import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAlbum } from '../album.model';
import { AlbumService } from '../service/album.service';

const albumResolve = (route: ActivatedRouteSnapshot): Observable<null | IAlbum> => {
  const id = route.params.id;
  if (id) {
    return inject(AlbumService)
      .find(id)
      .pipe(
        mergeMap((album: HttpResponse<IAlbum>) => {
          if (album.body) {
            return of(album.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default albumResolve;
