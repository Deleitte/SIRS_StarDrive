export class RegisterResponseDto {
    token!: string;
    totpSecret!: string;

    constructor(json?: RegisterResponseDto) {
        if (json) {
            this.token = json.token;
            this.totpSecret = json.totpSecret;
        }
    }
}
