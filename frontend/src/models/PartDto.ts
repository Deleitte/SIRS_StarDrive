export class PartDto {
    ref: number;
    name: string;
    price: number;
    quantity: number;

    constructor(partial: Partial<PartDto>) {
        this.ref = partial.ref ?? 0;
        this.name = partial.name ?? '';
        this.price = partial.price ?? 0;
        this.quantity = partial.quantity ?? 0;
    }
}