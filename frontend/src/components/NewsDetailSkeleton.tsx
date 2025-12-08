function NewsDetailSkeleton() {
  return (
    <div>
      <div className="container mx-auto px-4 py-8">
        {/* Back button skeleton */}
        <div className="mb-6 animate-pulse">
          <div className="h-6 bg-gray-200 rounded w-40"></div>
        </div>

        <article className="bg-white rounded-lg shadow-sm overflow-hidden">
          <div className="px-8 py-8 animate-pulse">
            {/* Type badge skeleton */}
            <div className="mb-4">
              <div className="h-6 bg-gray-200 rounded-full w-24"></div>
            </div>

            {/* Title skeleton */}
            <div className="space-y-3 mb-6">
              <div className="h-8 bg-gray-200 rounded w-full"></div>
              <div className="h-8 bg-gray-200 rounded w-3/4"></div>
            </div>

            {/* Catchline skeleton */}
            <div className="space-y-2 mb-8">
              <div className="h-5 bg-gray-200 rounded w-full"></div>
              <div className="h-5 bg-gray-200 rounded w-5/6"></div>
            </div>

            {/* Content skeleton */}
            <div className="space-y-3">
              <div className="h-4 bg-gray-200 rounded w-full"></div>
              <div className="h-4 bg-gray-200 rounded w-full"></div>
              <div className="h-4 bg-gray-200 rounded w-4/5"></div>
              <div className="h-4 bg-gray-200 rounded w-full"></div>
              <div className="h-4 bg-gray-200 rounded w-3/4"></div>
              <div className="h-4 bg-gray-200 rounded w-full"></div>
              <div className="h-4 bg-gray-200 rounded w-5/6"></div>
            </div>
          </div>
        </article>
      </div>
    </div>
  );
}

export default NewsDetailSkeleton;
