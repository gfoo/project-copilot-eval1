import { useInfiniteQuery } from "@tanstack/react-query";
import { useEffect, useRef, useCallback } from "react";
import { Link } from "react-router-dom";
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

      {/* Main Content Header */}
      <header className="bg-white border-b border-gray-200">
        <div className="max-w-6xl mx-auto px-4 py-8">
          <h1 className="text-3xl font-bold text-gray-900 mb-6">
            L'actualité de l'Unil
          </h1>
          <div className="flex items-center gap-4 mb-6">
            <button className="px-4 py-2 border-2 border-gray-900 rounded-full text-sm font-semibold hover:bg-gray-900 hover:text-white transition-all">
              Toutes les actualités
            </button>
            <button className="px-4 py-2 text-sm font-medium text-gray-600 hover:text-gray-900 flex items-center">
              Facultés
              <svg
                className="w-4 h-4 ml-1"
                fill="currentColor"
                viewBox="0 0 20 20"
              >
                <path
                  fillRule="evenodd"
                  d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
                  clipRule="evenodd"
                />
              </svg>
            </button>
          </div>
        </div>
      </header>

      {/* Main content */}
      <main className="max-w-6xl mx-auto px-4 py-6">
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
                {/* UNIL-style mixed layout */}
                <div className="space-y-8">
                  {/* Featured Article with Image */}
                  {allNews.slice(0, 1).map((news) => (
                    <div
                      key={news.id}
                      className="grid grid-cols-1 lg:grid-cols-2 gap-8 mb-8"
                    >
                      <div className="relative">
                        <img
                          src="https://images.unsplash.com/photo-1523240795612-9a054b0db644?w=500&h=300&fit=crop"
                          alt={news.title}
                          className="w-full h-64 object-cover rounded"
                        />
                      </div>
                      <div>
                        <h2 className="text-2xl font-bold text-gray-900 mb-4 leading-tight">
                          <Link
                            to={`/news/${news.id}`}
                            className="hover:text-unil-blue"
                          >
                            {news.title}
                          </Link>
                        </h2>
                        <p className="text-gray-600 text-base mb-4 leading-relaxed">
                          {news.catchLine}
                        </p>
                        <div className="text-sm text-gray-500 mb-3">
                          Publié le{" "}
                          {new Date().toLocaleDateString("fr-FR", {
                            day: "numeric",
                            month: "long",
                            year: "numeric",
                          })}
                        </div>
                        <div className="flex flex-wrap gap-2">
                          <span className="px-3 py-1 bg-unil-blue text-white text-xs font-medium rounded">
                            {news.type}
                          </span>
                          <span className="px-3 py-1 bg-green-100 text-green-800 text-xs font-medium rounded">
                            Recherche
                          </span>
                        </div>
                      </div>
                    </div>
                  ))}

                  {/* Grid of smaller articles */}
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                    {allNews.slice(1).map((news, index) => {
                      const isLast = index === allNews.length - 2;
                      return (
                        <article
                          key={news.id}
                          ref={isLast ? lastNewsElementRef : undefined}
                          className="space-y-4"
                        >
                          <h3 className="text-xl font-bold text-gray-900 leading-tight">
                            <Link
                              to={`/news/${news.id}`}
                              className="hover:text-unil-blue"
                            >
                              {news.title}
                            </Link>
                          </h3>
                          <p className="text-gray-600 text-sm leading-relaxed">
                            {news.catchLine}
                          </p>
                          <div className="text-xs text-gray-500 mb-2">
                            Publié le{" "}
                            {new Date().toLocaleDateString("fr-FR", {
                              day: "numeric",
                              month: "long",
                              year: "numeric",
                            })}
                          </div>
                          <div className="flex flex-wrap gap-2">
                            <span className="px-2 py-1 bg-unil-blue text-white text-xs font-medium rounded">
                              {news.type}
                            </span>
                          </div>
                        </article>
                      );
                    })}
                  </div>
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
