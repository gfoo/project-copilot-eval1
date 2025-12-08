/**
 * Example usage of the news API with TanStack Query.
 * 
 * This file demonstrates how to use the API service functions
 * with TanStack Query in React components.
 */

import { useQuery } from '@tanstack/react-query';
import { fetchNews, fetchNewsById } from '../services/newsApi';

/**
 * Hook to fetch paginated news articles.
 * 
 * @example
 * ```tsx
 * function NewsListComponent() {
 *   const { data, isLoading, error } = useNewsQuery(0, 10);
 * 
 *   if (isLoading) return <div>Loading...</div>;
 *   if (error) return <div>Error: {error.message}</div>;
 * 
 *   return (
 *     <div>
 *       {data.data.map(article => (
 *         <div key={article.id}>{article.title}</div>
 *       ))}
 *       <div>Page {data.page + 1} of {Math.ceil(data.total / data.size)}</div>
 *     </div>
 *   );
 * }
 * ```
 */
export function useNewsQuery(page = 0, size = 10) {
  return useQuery({
    queryKey: ['news', page, size],
    queryFn: () => fetchNews(page, size),
  });
}

/**
 * Hook to fetch a single news article by ID.
 * 
 * @example
 * ```tsx
 * function NewsDetailComponent({ id }: { id: string }) {
 *   const { data, isLoading, error } = useNewsDetailQuery(id);
 * 
 *   if (isLoading) return <div>Loading...</div>;
 *   if (error) return <div>Error: {error.message}</div>;
 * 
 *   return (
 *     <article>
 *       <h1>{data.title}</h1>
 *       <p>{data.catchLine}</p>
 *       <div>Type: {data.type}</div>
 *       <div>Language: {data.language}</div>
 *       <div>Status: {data.status}</div>
 *     </article>
 *   );
 * }
 * ```
 */
export function useNewsDetailQuery(id: string) {
  return useQuery({
    queryKey: ['news', id],
    queryFn: () => fetchNewsById(id),
    enabled: !!id, // Only run query if id is provided
  });
}
