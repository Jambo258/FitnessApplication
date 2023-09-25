import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LeaderboardComponent } from './leaderboard/leaderboard.component';
import { ComponentComponent } from './component/component.component';
import { RegistrationComponent } from './registration/registration.component';
import { LoginComponent } from './login/login.component';
import { AdminpageComponent } from './adminpage/adminpage.component';
import { ProfilepageComponent } from './profilepage/profilepage.component';
import { AdminGuard } from './auth-guard.service';
import { RoleGuardService } from './role-guard.service';
import { ChartComponent } from './chart/chart.component';
import { HealthDataComponent } from './health-data/health-data.component';
import { DailyTrainingComponent } from './daily-training/daily-training.component';

const routes: Routes = [
  { path: 'home', component: ComponentComponent },
  {
    path: 'leaderboard',
    component: LeaderboardComponent,
    canActivate: [RoleGuardService],
  },
  { path: 'register', component: RegistrationComponent },
  { path: 'login', component: LoginComponent },
  {
    path: 'adminpage',
    component: AdminpageComponent,
    canActivate: [AdminGuard],
  },
  {
    path: 'profilepage',
    component: ProfilepageComponent,
    canActivate: [RoleGuardService],
  },
  { path: 'chart', component: ChartComponent, canActivate: [RoleGuardService] },
  {
    path: 'healthdata',
    component: HealthDataComponent,
    canActivate: [RoleGuardService],
  },
  { path: 'dailytraining', component: DailyTrainingComponent, canActivate: [RoleGuardService] },
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: '**', redirectTo: '/home' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule],
})
export class AppRoutingModule {}
