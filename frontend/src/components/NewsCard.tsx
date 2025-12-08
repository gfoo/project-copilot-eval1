import { Link } from "react-router-dom";
import type { NewsResponse } from "../types/news";

interface NewsCardProps {
  news: NewsResponse;
}

export default function NewsCard({ news }: NewsCardProps) {
  return (
    <article className="bg-white border border-gray-200 rounded-lg hover:shadow-md transition-shadow duration-200 h-full flex flex-col">
      <div className="p-5 flex-1 flex flex-col">
        {/* Type badge */}
        <div className="mb-3">
          <span className="inline-block px-2 py-1 text-xs font-medium text-blue-700 bg-blue-50 rounded">
            {news.type}
          </span>
        </div>

        {/* Title */}
        <h3 className="text-lg font-semibold text-gray-900 mb-3 leading-tight">
          <Link
            to={`/news/${news.id}`}
            className="hover:text-blue-600 transition-colors duration-200"
          >
            {news.title}
          </Link>
        </h3>

        {/* Catch line */}
        <p className="text-gray-600 mb-4 line-clamp-3 text-sm leading-relaxed flex-1">
          {news.catchLine}
        </p>

        {/* Footer with publication date and language */}
        <div className="flex items-center justify-between text-xs text-gray-500 pt-2 border-t border-gray-100 mt-auto">
          <span className="capitalize">{news.language}</span>
          <div className="flex items-center space-x-2">
            <span
              className={`px-2 py-1 rounded-full text-xs ${
                news.status === "published"
                  ? "bg-green-50 text-green-600 border border-green-200"
                  : "bg-orange-50 text-orange-600 border border-orange-200"
              }`}
            >
              {news.status}
            </span>
          </div>
        </div>
      </div>
    </article>
  );
}
