import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ComponentComponent } from './component/component.component';
import { FormsModule } from '@angular/forms';
import { LeaderboardComponent } from './leaderboard/leaderboard.component';
import { LoginComponent } from './login/login.component';
import { RegistrationComponent } from './registration/registration.component';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MyApiServiceService } from './my-api-service.service';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatInputModule } from '@angular/material/input';
import { JwtModule } from '@auth0/angular-jwt';
import { AuthenticationService } from './authentication.service';
import { MatListModule } from '@angular/material/list';
import { AdminpageComponent } from './adminpage/adminpage.component';
import { ProfilepageComponent } from './profilepage/profilepage.component';
import { AuthInterceptor } from './auth-interceptor.service';
import { MatTableModule } from '@angular/material/table';
import { DeleteDialogComponent } from './delete-dialog/delete-dialog.component';
import { MatDialogModule } from '@angular/material/dialog';
import { UpdateDialogComponent } from './update-dialog/update-dialog.component';
import { MatSelectModule } from '@angular/material/select';
import { NgChartsModule } from 'ng2-charts';
import { ChartComponent } from './chart/chart.component';
import { HealthDataComponent } from './health-data/health-data.component';
import { DailyTrainingComponent } from './daily-training/daily-training.component';
import { CompletedTrainingComponent } from './completed-training/completed-training.component';

export function tokenGetter() {
  return localStorage.getItem('token');
}

@NgModule({
  declarations: [
    AppComponent,
    ComponentComponent,
    LeaderboardComponent,
    LoginComponent,
    RegistrationComponent,
    AdminpageComponent,
    ProfilepageComponent,
    DeleteDialogComponent,
    UpdateDialogComponent,
    ChartComponent,
    HealthDataComponent,
    DailyTrainingComponent,
    CompletedTrainingComponent,
  ],
  imports: [
    BrowserModule,
    MatListModule,
    NgChartsModule,
    MatDialogModule,
    AppRoutingModule,
    FormsModule,
    MatButtonModule,
    MatIconModule,
    MatToolbarModule,
    MatCardModule,
    MatFormFieldModule,
    HttpClientModule,
    ReactiveFormsModule,
    MatInputModule,
    MatSelectModule,
    MatTableModule,
    BrowserAnimationsModule,
    JwtModule.forRoot({
      config: {
        tokenGetter,
        allowedDomains: ['localhost:8080'],

      },
    }),
  ],
  providers: [
    MyApiServiceService,
    AuthenticationService,

    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
