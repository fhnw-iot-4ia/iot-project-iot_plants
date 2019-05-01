import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { PlantServiceService } from './plant-service.service'
import { Config } from './config'
import { ConnectedPlants } from './connected-plants'
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*' })
};

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  config: Config;
  threshold: number;
  measuredValue: number;
  url: string
  id: string

  newThreshold: number;

  angForm: FormGroup;

  constructor(private http: HttpClient, private plantService: PlantServiceService, private fb: FormBuilder) {
    this.showConfig();
    this.createForm();
  }

  showConfig() {
    this.plantService.getConfig()
      .subscribe((data: Config) => {
        this.config = { ...data }
        this.getFromServer(data.serverUrl)
      });
  }

  getFromServer(url: string) {
    this.url = url;
    this.http.get<ConnectedPlants[]>(url).subscribe((data: ConnectedPlants[]) => {
      const lastElement = data.length - 1;
      this.threshold = data[lastElement].threshold
      this.measuredValue = data[lastElement].measuredMoistureValue
      this.id = data[lastElement].id
    })
  }

  createForm() {
    this.angForm = this.fb.group({
      name: ['', Validators.required]
    });
  }
  submitted = false;

  onSubmit() {
    this.submitted = true;
    this.newThreshold = this.angForm.value.name
    this.storeToDb()
  }
  storeToDb() {
    let result = {
      'id': this.id,
      'threshold': this.newThreshold,
      'measuredValue': this.measuredValue
    }
    return this.http.put<ConnectedPlants>(this.url + '/' + this.id, result, httpOptions).subscribe(() => {
      location.reload()
    })
  }

  title = 'connected-plants-web';
}
