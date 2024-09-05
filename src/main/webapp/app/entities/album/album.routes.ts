import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AlbumResolve from './route/album-routing-resolve.service';

const albumRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/album.component').then(m => m.AlbumComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/album-detail.component').then(m => m.AlbumDetailComponent),
    resolve: {
      album: AlbumResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/album-update.component').then(m => m.AlbumUpdateComponent),
    resolve: {
      album: AlbumResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/album-update.component').then(m => m.AlbumUpdateComponent),
    resolve: {
      album: AlbumResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default albumRoute;
