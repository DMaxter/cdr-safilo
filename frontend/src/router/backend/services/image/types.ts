export class Image {
  id: number = 0;
  obsolete: boolean = false;
  link: string = "";

  constructor(obj?: Image) {
    if (obj) {
      this.id = obj.id || 0;
      this.obsolete = obj.obsolete || false;
      this.link = obj.link || "";
    }
  }
}
