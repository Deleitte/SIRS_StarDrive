export class NewPartDto {
    ref: number;
    name: string;
    price: number;

    constructor(partial: Partial<NewPartDto>) {
        this.ref = partial.ref ?? 0;
        this.name = partial.name ?? '';
        this.price = partial.price ?? 0;
    }
}