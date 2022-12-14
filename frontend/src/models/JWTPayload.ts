export interface JWTPayload {
    exp: number;
    iat: number;
    iss: string;
    sub: string;
    role: string;
}

export class JWTPayloadData {
    expiresAt!: Date;
    issuedAt!: Date;
    issuer!: string;
    username!: string;
    role!: string;

    constructor(json?: JWTPayload) {
        if (json) {
            this.expiresAt = new Date(json.exp * 1000);
            this.issuedAt = new Date(json.iat * 1000);
            this.issuer = json.iss;
            this.username = json.sub;
            this.role = json.role;
        }
    }
}