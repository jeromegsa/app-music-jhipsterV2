import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMusic } from '../music.model';
import { MusicService } from '../service/music.service';

const musicResolve = (route: ActivatedRouteSnapshot): Observable<null | IMusic> => {
  const id = route.params.id;
  if (id) {
    return inject(MusicService)
      .find(id)
      .pipe(
        mergeMap((music: HttpResponse<IMusic>) => {
          if (music.body) {
            return of(music.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default musicResolve;
