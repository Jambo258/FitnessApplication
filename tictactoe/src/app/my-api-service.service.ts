import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

interface HealthData {
  height: Number,
  weight: Number,
  target_Calories: Number,
  target_Steps: Number
}

interface TrainingData {
  daily_Calories: Number;
  daily_Steps: Number;
}

@Injectable({
  providedIn: 'root',
})
export class MyApiServiceService {
  private backendUrlUsers = 'http://localhost:8080/api/users';
  private backendUrlHealthStatus = 'http://localhost:8080/api/health';
  private backendUrlTraining = 'http://localhost:8080/api/training';

  healthData: HealthData = {
    height: 0,
    weight: 0,
    target_Calories: 0,
    target_Steps: 0,
  };

  trainingData: TrainingData = {
    daily_Calories: 0,
    daily_Steps: 0,
  };

  constructor(private http: HttpClient) {}

  registerUser(userData: any): Observable<any> {
    const url = `${this.backendUrlUsers}/register`;
    return this.http.post(url, userData);
  }

  loginUser(userData: any): Observable<any> {
    const url = `${this.backendUrlUsers}/login`;
    return this.http.post(url, userData);
  }

  getUserData(userId: string) {
    const token = localStorage.getItem('token');
    const tokenstr = token;

    const url = `${this.backendUrlUsers}/id/${userId}`;
    return this.http.get<any>(url);
  }

  UpdateUser(userId: string, userData: any): Observable<any> {
    const url = `${this.backendUrlUsers}/id/${userId}/update`;
    return this.http.put(url, userData);
  }

  DeleteUser(userId: string): Observable<any> {
    const url = `${this.backendUrlUsers}/id/${userId}/delete`;
    return this.http.delete(url);
  }

  getAllUserData() {
    const url = `${this.backendUrlUsers}/getAll`;
    return this.http.get<any>(url);
  }

  getAllUserDataCombined() {
    const url = `${this.backendUrlUsers}/getAllUsersData`;
    return this.http.get<any>(url);
  }

  addUserHealth(userId: string, userHealthData: any): Observable<any> {
    const url = `${this.backendUrlHealthStatus}/${userId}/addstatus`;
    return this.http.post(url, userHealthData);
  }

  getUserHealthData(userId: string) {
    const url = `${this.backendUrlHealthStatus}/${userId}/status`;
    return this.http.get<any>(url);
  }

  getAllUsersHealthData() {
    const url = `${this.backendUrlHealthStatus}/getAllStatuses`;
    return this.http.get<any>(url);
  }

  addUserTrainingDay(userId: string, userTrainingData: any): Observable<any> {
    const url = `${this.backendUrlTraining}/${userId}/addtraining`;
    return this.http.post(url, userTrainingData);
  }

  getUserTrainingDayData(userId: string) {
    const url = `${this.backendUrlTraining}/${userId}/trainingdays`;
    return this.http.get<any>(url);
  }

  getAllUsersTrainingDays() {
    const url = `${this.backendUrlTraining}/getAllTrainingDays`;
    return this.http.get<any>(url);
  }

  deleteUsersAllTrainingDays(userId: string): Observable<any> {
    const url = `${this.backendUrlTraining}/${userId}/deleteall`;
    return this.http.delete(url);
  }

  deleteUserHealthData(userId: string): Observable<any> {
    const url = `${this.backendUrlHealthStatus}/${userId}/status/delete`;
    return this.http.delete(url);
  }
}

