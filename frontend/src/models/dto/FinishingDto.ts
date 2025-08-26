export default class FinishingDto {
  id: number;
  name: string;
  obsolete: boolean;

  constructor(obj: FinishingDto) {
    this.id = obj.id;
    this.name = obj.name;
    this.obsolete = obj.obsolete;
  }
}
