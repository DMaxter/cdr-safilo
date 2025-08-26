import { Status } from "@models/dto/RequestStatus";

export default class SearchViewFilter {
  id: number[];
  status: Status[];
  client: number[];
  commercial: string[];
  creationDate: Date[];
}
