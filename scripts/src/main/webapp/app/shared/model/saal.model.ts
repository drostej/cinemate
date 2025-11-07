export interface ISaal {
  id?: number;
  name?: string;
  kapazitaet?: number;
}

export class Saal implements ISaal {
  constructor(
    public id?: number,
    public name?: string,
    public kapazitaet?: number,
  ) {}
}
