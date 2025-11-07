import { type ISaal } from '@/shared/model/saal.model';

export interface IVorfuehrung {
  id?: number;
  filmTitel?: string;
  datumZeit?: Date;
  saal?: ISaal | null;
}

export class Vorfuehrung implements IVorfuehrung {
  constructor(
    public id?: number,
    public filmTitel?: string,
    public datumZeit?: Date,
    public saal?: ISaal | null,
  ) {}
}
