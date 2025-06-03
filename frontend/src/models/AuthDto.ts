export default class AuthDto {
  email: string | null = null;
  password: string | null = null;

  constructor(obj?: Partial<AuthDto>) {
    if (obj) {
      this.email = obj.email ?? null;
      this.password = obj.password ?? null;
    }
  }
}
