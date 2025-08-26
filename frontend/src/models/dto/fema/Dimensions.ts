export default class Dimensions {
  height: double = 0;
  width: double = 0;
  length: double = 0;

  constructor(obj?: Partial<Dimensions>) {
    if (obj) {
      this.height = obj.height ?? 0;
      this.width = obj.width ?? 0;
      this.length = obj.length ?? 0;
    }
  }
}
