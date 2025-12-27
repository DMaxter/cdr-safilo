export class Finishing {
  id: number = 0;
  name: string = "";
  obsolete: boolean = false;

  constructor(obj?: Finishing) {
    if (obj) {
      this.id = obj.id || null;
      this.name = obj.name || "";
      this.obsolete = obj.obsolete || null;
    }
  }
}
