function NewsCardSkeleton() {
  return (
    <div className="bg-white rounded-lg shadow-sm p-6 animate-pulse">
      {/* Type badge skeleton */}
      <div className="h-6 bg-gray-200 rounded-full w-20 mb-4"></div>

      {/* Title skeleton */}
      <div className="space-y-2 mb-4">
        <div className="h-6 bg-gray-200 rounded w-full"></div>
        <div className="h-6 bg-gray-200 rounded w-3/4"></div>
      </div>

      {/* Catch line skeleton */}
      <div className="space-y-2 mb-6">
        <div className="h-4 bg-gray-200 rounded w-full"></div>
        <div className="h-4 bg-gray-200 rounded w-5/6"></div>
        <div className="h-4 bg-gray-200 rounded w-2/3"></div>
      </div>

      {/* Bottom section skeleton */}
      <div className="flex justify-between items-center">
        <div className="h-4 bg-gray-200 rounded w-16"></div>
        <div className="h-4 bg-gray-200 rounded w-12"></div>
      </div>
    </div>
  );
}

function NewsGridSkeleton({ count = 12 }: { count?: number }) {
  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
      {Array.from({ length: count }, (_, index) => (
        <NewsCardSkeleton key={index} />
      ))}
    </div>
  );
}

export { NewsCardSkeleton, NewsGridSkeleton };
