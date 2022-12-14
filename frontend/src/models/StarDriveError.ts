export class StarDriveError extends Error {
  __proto__ = Error;
  public code?: number;

  constructor(message: string, code?: number) {
    super(message);
    if (code) this.code = code;
    Object.setPrototypeOf(this, StarDriveError.prototype);
  }
}
