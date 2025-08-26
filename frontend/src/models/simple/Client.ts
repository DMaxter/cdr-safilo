export default class Client {
  id: number;
  name: string;

  constructor(obj: Partial<Client>) {
    this.id = obj.id;
    this.name = obj.name;
  }
}
