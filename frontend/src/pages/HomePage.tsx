import { useInfiniteQuery } from "@tanstack/react-query";
import { useEffect, useRef, useCallback } from "react";
import { fetchNews } from "../services/newsApi";
import NewsCard from "../components/NewsCard";
import { NewsGridSkeleton } from "../components/NewsCardSkeleton";

function HomePage() {
  const {
    data,
    fetchNextPage,
    hasNextPage,
    isFetchingNextPage,
    isLoading,
    error,
  } = useInfiniteQuery({
    queryKey: ["news"],
    queryFn: ({ pageParam = 0 }) => fetchNews(pageParam, 12),
    getNextPageParam: (lastPage) => {
      const hasMore = (lastPage.page + 1) * lastPage.size < lastPage.total;
      return hasMore ? lastPage.page + 1 : undefined;
    },
    initialPageParam: 0,
  });

  // Infinite scroll implementation using Intersection Observer
  const observerRef = useRef<IntersectionObserver | null>(null);
  const lastNewsElementRef = useCallback(
    (node: HTMLDivElement) => {
      if (isLoading) return;
      if (observerRef.current) observerRef.current.disconnect();

      observerRef.current = new IntersectionObserver((entries) => {
        if (entries[0].isIntersecting && hasNextPage && !isFetchingNextPage) {
          fetchNextPage();
        }
      });

      if (node) observerRef.current.observe(node);
    },
    [isLoading, hasNextPage, isFetchingNextPage, fetchNextPage]
  );

  // Cleanup observer on unmount
  useEffect(() => {
    return () => {
      if (observerRef.current) {
        observerRef.current.disconnect();
      }
    };
  }, []);

  // Flatten all pages into a single array
  const allNews = data?.pages.flatMap((page) => page.data) ?? [];
  const totalItems = data?.pages[0]?.total ?? 0;

  return (
    <div className="min-h-screen bg-gray-50">
      {/* UNIL Blue Top Header with Logo */}
      <div className="bg-blue-500 py-3 px-4">
        <div className="max-w-7xl mx-auto flex items-center justify-between">
          <div className="flex items-center">
            <div className="bg-white text-blue-500 px-3 py-1 rounded font-bold text-lg">
              Unil.
            </div>
          </div>
          <div className="flex items-center space-x-4 text-white">
            <button className="p-1">
              <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                <path
                  fillRule="evenodd"
                  d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z"
                  clipRule="evenodd"
                />
              </svg>
            </button>
            <button className="p-1">
              <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                <path
                  fillRule="evenodd"
                  d="M3 5a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zM3 10a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zM3 15a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1z"
                  clipRule="evenodd"
                />
              </svg>
            </button>
          </div>
        </div>
      </div>

      {/* Main Content Header - White background with black text */}
      <header className="bg-white border-b border-gray-200">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-3xl font-semibold text-gray-900 mb-1">
                L'actualité de l'Unil
              </h1>
              <p className="text-gray-600 text-sm">Université de Lausanne</p>
            </div>
            <div className="text-right">
              <p className="text-sm text-gray-500">Toutes les actualités</p>
            </div>
          </div>
        </div>
      </header>

      {/* Main content */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Loading State */}
        {isLoading && <NewsGridSkeleton count={12} />}

        {/* Error State */}
        {error && (
          <div className="bg-red-50 border border-red-200 rounded-lg p-6 mb-8 text-center">
            <div className="flex flex-col items-center">
              <div className="flex-shrink-0 mb-4">
                <svg
                  className="h-12 w-12 text-red-400"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                >
                  <circle cx="12" cy="12" r="10"></circle>
                  <line x1="15" y1="9" x2="9" y2="15"></line>
                  <line x1="9" y1="9" x2="15" y2="15"></line>
                </svg>
              </div>
              <h3 className="text-lg font-semibold text-gray-900 mb-2">
                Erreur lors du chargement
              </h3>
              <p className="text-gray-700 mb-4">
                {error instanceof Error
                  ? error.message
                  : "Une erreur s'est produite lors du chargement des actualités"}
              </p>
              <button
                onClick={() => window.location.reload()}
                className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
              >
                Réessayer
              </button>
            </div>
          </div>
        )}

        {/* News Grid */}
        {data && (
          <>
            {allNews.length === 0 ? (
              <div className="text-center py-12">
                <h3 className="text-lg font-medium text-gray-900 mb-2">
                  Aucune actualité disponible
                </h3>
                <p className="text-gray-600">
                  Revenez plus tard pour découvrir les dernières nouvelles.
                </p>
              </div>
            ) : (
              <>
                {/* Enhanced Responsive Grid with better spacing and hover effects */}
                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6 lg:gap-8 mb-12">
                  {allNews.map((news, index) => {
                    // Add ref to the last element for infinite scroll detection
                    if (index === allNews.length - 1) {
                      return (
                        <div
                          key={news.id}
                          ref={lastNewsElementRef}
                          className="transform transition-transform duration-200 hover:scale-[1.02]"
                        >
                          <NewsCard news={news} />
                        </div>
                      );
                    }
                    return (
                      <div
                        key={news.id}
                        className="transform transition-transform duration-200 hover:scale-[1.02]"
                      >
                        <NewsCard news={news} />
                      </div>
                    );
                  })}
                </div>
                {/* Loading more indicator */}
                {isFetchingNextPage && (
                  <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6 lg:gap-8 mt-8">
                    {Array.from({ length: 4 }, (_, index) => (
                      <div key={index} className="animate-pulse">
                        <div className="bg-white rounded-lg shadow-sm p-6">
                          <div className="h-6 bg-gray-200 rounded-full w-20 mb-4"></div>
                          <div className="space-y-2 mb-4">
                            <div className="h-6 bg-gray-200 rounded w-full"></div>
                            <div className="h-6 bg-gray-200 rounded w-3/4"></div>
                          </div>
                          <div className="space-y-2 mb-6">
                            <div className="h-4 bg-gray-200 rounded w-full"></div>
                            <div className="h-4 bg-gray-200 rounded w-2/3"></div>
                          </div>
                        </div>
                      </div>
                    ))}
                  </div>
                )}{" "}
                {/* End of data indicator */}
                {!hasNextPage && allNews.length > 0 && (
                  <div className="text-center py-8">
                    <div className="inline-flex items-center px-4 py-2 bg-gray-100 rounded-lg">
                      <svg
                        className="h-4 w-4 text-gray-500 mr-2"
                        fill="none"
                        stroke="currentColor"
                        viewBox="0 0 24 24"
                      >
                        <path
                          strokeLinecap="round"
                          strokeLinejoin="round"
                          strokeWidth={2}
                          d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"
                        />
                      </svg>
                      <span className="text-sm text-gray-600">
                        Toutes les actualités ont été chargées
                      </span>
                    </div>
                  </div>
                )}
                {/* Stats */}
                <div className="text-center text-sm text-gray-500 mt-6">
                  {allNews.length} sur {totalItems} articles affichés
                </div>
              </>
            )}
          </>
        )}
      </main>
    </div>
  );
}

export default HomePage;
