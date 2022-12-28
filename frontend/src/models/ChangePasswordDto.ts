export class ChangePasswordDto {
  oldPassword: string;
  newPassword: string;

  constructor(obj: Partial<ChangePasswordDto>) {
    this.oldPassword = obj.oldPassword ?? "";
    this.newPassword = obj.newPassword ?? "";
  }
}
