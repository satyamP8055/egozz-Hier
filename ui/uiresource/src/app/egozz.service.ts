import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class EgozzService {

  constructor() { }

  public host:string="http://localhost:8055";

}
