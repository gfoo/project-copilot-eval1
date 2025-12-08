import { Link } from "react-router-dom";
import type { NewsResponse } from "../types/news";

interface NewsCardProps {
  news: NewsResponse;
}

export default function NewsCard({ news }: NewsCardProps) {
  return (
    <div className="bg-white border border-gray-200 rounded-lg shadow-sm hover:shadow-md transition-shadow duration-200">
      <div className="p-6">
        {/* Type badge */}
        <div className="mb-3">
          <span className="inline-block px-2 py-1 text-xs font-medium text-blue-600 bg-blue-50 rounded-full">
            {news.type}
          </span>
        </div>

        {/* Title */}
        <h2 className="text-xl font-semibold text-gray-900 mb-3 hover:text-blue-600 transition-colors">
          <Link to={`/news/${news.id}`} className="block">
            {news.title}
          </Link>
        </h2>

        {/* Catch line */}
        <p className="text-gray-600 mb-4 line-clamp-3">
          {news.catchLine}
        </p>

        {/* Footer with language and status */}
        <div className="flex items-center justify-between text-sm text-gray-500">
          <span>Language: {news.language}</span>
          <span className={`px-2 py-1 rounded-full text-xs ${
            news.status === 'published' 
              ? 'bg-green-100 text-green-700' 
              : 'bg-yellow-100 text-yellow-700'
          }`}>
            {news.status}
          </span>
        </div>
      </div>
    </div>
  );
}