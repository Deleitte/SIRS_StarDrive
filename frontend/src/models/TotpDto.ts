export class TotpDto {
  guess: number;

  constructor(guess: number) {
    this.guess = guess;
  }
}
