export class LogDto {
    author: string;
    message: string;
    timestamp: string;
    verified: boolean;

    constructor(jsonObject) {
        this.author = jsonObject.author;
        this.message = jsonObject.message;
        this.timestamp = jsonObject.timestamp;
        this.verified = jsonObject.verified;
    }
}