import { API_BASE_URL } from '../config/api';
import type { NewsResponse, PagedResponse } from '../types/news';

/**
 * API error class for handling HTTP errors.
 */
export class ApiError extends Error {
  status: number;
  statusText: string;

  constructor(
    status: number,
    message: string,
    statusText: string
  ) {
    super(message);
    this.status = status;
    this.statusText = statusText;
    this.name = 'ApiError';
  }
}

/**
 * Fetch all news articles with pagination.
 * 
 * @param page - Page number (zero-based, default: 0)
 * @param size - Number of items per page (default: 10)
 * @returns Promise resolving to PagedResponse<NewsResponse>
 * @throws ApiError for 400 (Invalid request parameters) or network errors
 */
export async function fetchNews(
  page: number = 0,
  size: number = 10
): Promise<PagedResponse<NewsResponse>> {
  const url = new URL(`${API_BASE_URL}/news`);
  url.searchParams.append('page', page.toString());
  url.searchParams.append('size', size.toString());

  const response = await fetch(url.toString());

  if (!response.ok) {
    const errorMessage = await response.text().catch(() => response.statusText);
    throw new ApiError(response.status, errorMessage, response.statusText);
  }

  return response.json();
}

/**
 * Fetch a single news article by ID.
 * 
 * @param id - News article ID (e.g., 507f1f77bcf86cd799439011)
 * @returns Promise resolving to NewsResponse
 * @throws ApiError for 404 (News article not found) or network errors
 */
export async function fetchNewsById(id: string): Promise<NewsResponse> {
  const url = `${API_BASE_URL}/news/${id}`;

  const response = await fetch(url);

  if (!response.ok) {
    const errorMessage = await response.text().catch(() => response.statusText);
    throw new ApiError(response.status, errorMessage, response.statusText);
  }

  return response.json();
}
