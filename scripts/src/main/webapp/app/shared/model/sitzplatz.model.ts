import { type ISaal } from '@/shared/model/saal.model';
import { type IReservierung } from '@/shared/model/reservierung.model';

export interface ISitzplatz {
  id?: number;
  reihe?: string;
  nummer?: number;
  saal?: ISaal | null;
  reservierungens?: IReservierung[] | null;
}

export class Sitzplatz implements ISitzplatz {
  constructor(
    public id?: number,
    public reihe?: string,
    public nummer?: number,
    public saal?: ISaal | null,
    public reservierungens?: IReservierung[] | null,
  ) {}
}
