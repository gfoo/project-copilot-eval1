import { useQuery } from "@tanstack/react-query";
import { fetchNews } from "../services/newsApi";
import NewsCard from "../components/NewsCard";

function HomePage() {
  const { data, isLoading, error } = useQuery({
    queryKey: ["news", 0], // page 0 for initial load
    queryFn: () => fetchNews(0, 12), // Fetch 12 items for nice grid layout
  });

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="container mx-auto px-4 py-8">
        {/* Header */}
        <header className="mb-8">
          <h1 className="text-4xl font-bold text-gray-900 mb-2">
            L'actualité de l'Unil
          </h1>
          <p className="text-gray-600">
            Découvrez les dernières nouvelles et actualités
          </p>
        </header>

        {/* Loading State */}
        {isLoading && (
          <div className="flex justify-center items-center py-12">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
            <span className="ml-3 text-gray-600">Chargement des actualités...</span>
          </div>
        )}

        {/* Error State */}
        {error && (
          <div className="bg-red-50 border border-red-200 rounded-lg p-4 mb-6">
            <div className="flex">
              <div className="flex-shrink-0">
                <svg className="h-5 w-5 text-red-400" viewBox="0 0 20 20" fill="currentColor">
                  <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clipRule="evenodd" />
                </svg>
              </div>
              <div className="ml-3">
                <h3 className="text-sm font-medium text-red-800">
                  Erreur lors du chargement
                </h3>
                <p className="mt-2 text-sm text-red-700">
                  {error instanceof Error ? error.message : "Une erreur s'est produite"}
                </p>
              </div>
            </div>
          </div>
        )}

        {/* News Grid */}
        {data && (
          <>
            {data.data.length === 0 ? (
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
                {/* Responsive Grid: 1 col mobile, 2 cols tablet, 3+ cols desktop */}
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6 mb-8">
                  {data.data.map((news) => (
                    <NewsCard key={news.id} news={news} />
                  ))}
                </div>

                {/* Pagination Info */}
                <div className="text-center text-sm text-gray-600">
                  Page {data.page + 1} • {data.data.length} sur {data.total} articles
                </div>
              </>
            )}
          </>
        )}
      </div>
    </div>
  );
}

export default HomePage;
