export interface JWTPayload {
    exp: number;
    iat: number;
    iss: string;
    sub: string;
    scope: string;
    [n: string]: boolean;
}

export class JWTPayloadData {
    expiresAt!: Date;
    issuedAt!: Date;
    issuer!: string;
    username!: string;
    scope!: string;
    twoFactor!: boolean;

    constructor(json?: JWTPayload) {
        if (json) {
            this.expiresAt = new Date(json.exp * 1000);
            this.issuedAt = new Date(json.iat * 1000);
            this.issuer = json.iss;
            this.username = json.sub;
            this.scope = json.scope;
            this.twoFactor = json["2FA"];
        }
    }
}