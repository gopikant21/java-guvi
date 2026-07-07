import {Component, inject, OnInit} from '@angular/core';
import { FormsModule } from '@angular/forms';
import {HttpClient} from '@angular/common/http';

type person = {
  fname: string;
  lname: string;
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class App implements OnInit{

ngOnInit() {
  this.getAll();
}

  httpClient:HttpClient=inject(HttpClient);

  protected title = "first-app";
  protected person: person = {fname: "Gopi", lname: "Kant"};
  protected names: string[] = ["hello", "bhramha", "Vishnu","Mahesh"];
  protected newName: string = "";
  protected employee: person[] = [{fname: "Gopi", lname: "Kant"}, {fname: "Bhramha", lname: "Vishnu"}];

  removeName(name: string):void {
    this.names = this.names.filter(n=>n!==name);
  }

  addName(){
    if(this.newName.trim()!=""){
      this.names.push(this.newName);
      this.newName = "";
    }
  }

  editName(index: number): void {
    const trimmedName = this.newName.trim();
    if (trimmedName !== "") {
      this.names[index] = trimmedName;
      this.newName = "";
    }
  }

  getAll(){
    this.httpClient.get<EmployeeDTO[]>('http://localhost:8080/employees')
      .subscribe({
        next:(data)=>{
          console.log(data);
          this.names=data.map((item: EmployeeDTO)=>item.name)
        },
        error: (err)=>{
          console.log(err);
        }
      })

  }

}
