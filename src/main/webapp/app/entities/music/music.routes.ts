import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MusicResolve from './route/music-routing-resolve.service';

const musicRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/music.component').then(m => m.MusicComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/music-detail.component').then(m => m.MusicDetailComponent),
    resolve: {
      music: MusicResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/music-update.component').then(m => m.MusicUpdateComponent),
    resolve: {
      music: MusicResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/music-update.component').then(m => m.MusicUpdateComponent),
    resolve: {
      music: MusicResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default musicRoute;
