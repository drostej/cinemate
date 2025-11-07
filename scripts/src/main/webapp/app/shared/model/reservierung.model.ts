import { type ISitzplatz } from '@/shared/model/sitzplatz.model';
import { type IVorfuehrung } from '@/shared/model/vorfuehrung.model';

import { type ReservierungsStatus } from '@/shared/model/enumerations/reservierungs-status.model';
export interface IReservierung {
  id?: number;
  kundeName?: string;
  status?: keyof typeof ReservierungsStatus;
  plaetzes?: ISitzplatz[] | null;
  vorfuehrung?: IVorfuehrung | null;
}

export class Reservierung implements IReservierung {
  constructor(
    public id?: number,
    public kundeName?: string,
    public status?: keyof typeof ReservierungsStatus,
    public plaetzes?: ISitzplatz[] | null,
    public vorfuehrung?: IVorfuehrung | null,
  ) {}
}
