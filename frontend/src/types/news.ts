/**
 * News article response from the API.
 * Matches the OpenAPI NewsResponse schema.
 */
export interface NewsResponse {
  id: string;
  title: string;
  type: string;
  catchLine: string;
  language: string;
  status: string;
}

/**
 * Generic paginated response wrapper.
 * Matches the OpenAPI PagedResponse schema.
 */
export interface PagedResponse<T> {
  data: T[];
  page: number;    // int32, zero-based
  size: number;    // int32
  total: number;   // int64
}
