export default class RegisterDto {
  email: string | null = null;
  name: string | null = null;
  password: string | null = null;

  constructor(obj?: Partial<RegisterDto>) {
    if (obj) {
      this.email = obj.email ?? null;
      this.name = obj.name ?? null;
      this.password = obj.password ?? null;
    }
  }
}
