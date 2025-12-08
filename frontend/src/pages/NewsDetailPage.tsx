import { useParams, Link } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import { fetchNewsById, ApiError } from "../services/newsApi";
import NewsDetailSkeleton from "../components/NewsDetailSkeleton";

function NewsDetailPage() {
  const { id } = useParams<{ id: string }>();

  const {
    data: news,
    isLoading,
    error,
  } = useQuery({
    queryKey: ["news", id],
    queryFn: () => fetchNewsById(id!),
    enabled: !!id, // Only run query if id exists
  });

  // Loading state
  if (isLoading) {
    return <NewsDetailSkeleton />;
  }

  // Error state
  if (error) {
    const is404 = error instanceof ApiError && error.status === 404;

    return (
      <div>
        <div className="container mx-auto px-4 py-8">
          <Link
            to="/"
            className="inline-flex items-center text-blue-600 hover:text-blue-800 mb-6 transition-colors"
          >
            <svg
              className="h-4 w-4 mr-2"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M10 19l-7-7m0 0l7-7m-7 7h18"
              />
            </svg>
            Retour aux actualités
          </Link>

          <div className="bg-white rounded-xl shadow-lg p-12 text-center">
            <div className="mx-auto flex items-center justify-center h-20 w-20 rounded-full bg-red-50 border-4 border-red-100 mb-6">
              {is404 ? (
                <svg
                  className="h-10 w-10 text-red-500"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M9.172 16.172a4 4 0 015.656 0M9 12h6m-6-4h6m2 5.291A7.962 7.962 0 0112 15c-2.485 0-4.735.959-6.398 2.53l-.793.793a1 1 0 00-.293.707V21a1 1 0 001 1h12a1 1 0 001-1v-2.07a1 1 0 00-.293-.707l-.793-.793A7.962 7.962 0 0112 15z"
                  />
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.863-.833-2.634 0L5.268 18.5c-.77.833.192 2.5 1.732 2.5z"
                  />
                </svg>
              ) : (
                <svg
                  className="h-10 w-10 text-red-500"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
                  />
                </svg>
              )}
            </div>
            <h1 className="text-3xl font-bold text-gray-900 mb-4">
              {is404 ? "404 - Article introuvable" : "Erreur de chargement"}
            </h1>
            <p className="text-lg text-gray-600 mb-6 max-w-md mx-auto">
              {is404
                ? "L'article que vous cherchez n'existe pas ou a été supprimé de nos archives."
                : "Une erreur s'est produite lors du chargement de l'article. Veuillez réessayer plus tard."}
            </p>
            {error instanceof ApiError && (
              <div className="bg-red-50 border border-red-200 rounded-lg p-4 mb-8 max-w-sm mx-auto">
                <p className="text-sm text-red-700 font-medium">
                  Erreur {error.status}: {error.message}
                </p>
              </div>
            )}
            <div className="flex flex-col sm:flex-row gap-4 justify-center">
              <Link
                to="/"
                className="inline-flex items-center px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors font-medium"
              >
                <svg
                  className="h-5 w-5 mr-2"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M3 7v10a2 2 0 002 2h14a2 2 0 002-2V9a2 2 0 00-2-2H5a2 2 0 00-2-2V7zm0 0V5a2 2 0 012-2h6l2 2h6a2 2 0 012 2v2M7 13h10M7 17h4"
                  />
                </svg>
                Voir toutes les actualités
              </Link>
              {!is404 && (
                <button
                  onClick={() => window.location.reload()}
                  className="inline-flex items-center px-6 py-3 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition-colors font-medium"
                >
                  <svg
                    className="h-5 w-5 mr-2"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth={2}
                      d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"
                    />
                  </svg>
                  Réessayer
                </button>
              )}
            </div>
          </div>
        </div>
      </div>
    );
  }

  // Success state
  return (
    <div>
      <div className="container mx-auto px-4 py-8">
        <Link
          to="/"
          className="inline-flex items-center text-blue-600 hover:text-blue-800 mb-6 transition-colors"
        >
          <svg
            className="h-4 w-4 mr-2"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={2}
              d="M10 19l-7-7m0 0l7-7m-7 7h18"
            />
          </svg>
          Retour aux actualités
        </Link>

        <article className="bg-white rounded-xl shadow-lg overflow-hidden">
          <div className="px-8 lg:px-12 py-10">
            {/* Type badge */}
            <div className="mb-6">
              <span className="inline-block px-4 py-2 text-sm font-semibold text-blue-700 bg-blue-100 rounded-full border border-blue-200">
                {news?.type}
              </span>
            </div>

            {/* Title */}
            <h1 className="text-4xl lg:text-5xl font-bold text-gray-900 mb-8 leading-tight">
              {news?.title}
            </h1>

            {/* Meta information */}
            <div className="flex flex-wrap items-center gap-6 mb-10 p-4 bg-gray-50 rounded-lg">
              <div className="flex items-center text-sm text-gray-600">
                <svg
                  className="h-5 w-5 mr-2 text-gray-400"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M3 5h12M9 3v2m1.048 9.5A18.022 18.022 0 016.412 9m6.088 9h7M11 21l5-10 5 10M12.751 5C11.783 10.77 8.07 15.61 3 18.129"
                  />
                </svg>
                <span className="font-medium">Langue:</span>
                <span className="ml-1 capitalize">{news?.language}</span>
              </div>
              <div className="flex items-center text-sm text-gray-600">
                <div
                  className={`h-3 w-3 rounded-full mr-2 ${
                    news?.status === "published"
                      ? "bg-green-500"
                      : "bg-yellow-500"
                  }`}
                ></div>
                <span className="font-medium">Statut:</span>
                <span className="ml-1 capitalize">{news?.status}</span>
              </div>
            </div>

            {/* Content */}
            <div className="prose prose-xl max-w-none">
              <div className="text-xl lg:text-2xl text-gray-700 leading-relaxed font-light border-l-4 border-blue-500 pl-6 py-2 bg-blue-50 rounded-r-lg">
                {news?.catchLine}
              </div>
            </div>

            {/* Article ID for reference */}
            <div className="mt-12 pt-6 border-t border-gray-200">
              <p className="text-xs text-gray-400">Article ID: {news?.id}</p>
            </div>
          </div>
        </article>
      </div>
    </div>
  );
}

export default NewsDetailPage;
