export class Country {
  code: string = "";
  name: string = "";

  constructor(obj?: Country) {
    if (obj) {
      this.code = obj.code || "";
      this.name = obj.name || "";
    }
  }
}
