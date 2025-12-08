import { Link } from "react-router-dom";
import type { NewsResponse } from "../types/news";

interface NewsCardProps {
  news: NewsResponse;
}

export default function NewsCard({ news }: NewsCardProps) {
  return (
    <article className="pb-4 mb-4 border-b border-gray-200 last:border-b-0">
      {/* Title */}
      <h3 className="text-base font-bold text-gray-900 leading-tight mb-2">
        <Link
          to={`/news/${news.id}`}
          className="hover:text-unil-blue transition-colors duration-200"
        >
          {news.title}
        </Link>
      </h3>

      {/* Catch line */}
      <p className="text-gray-700 text-sm leading-relaxed mb-2">
        {news.catchLine}
      </p>

      {/* Publication info */}
      <div className="text-xs text-gray-500 mb-2">
        Publié le{" "}
        {new Date().toLocaleDateString("fr-FR", {
          day: "numeric",
          month: "long",
          year: "numeric",
        })}
      </div>

      {/* Tags */}
      <div className="flex flex-wrap gap-2">
        <span className="px-2 py-1 bg-unil-blue text-white text-xs font-medium rounded">
          {news.type}
        </span>
        <span
          className={`px-2 py-1 rounded text-xs ${
            news.status === "published"
              ? "bg-green-50 text-green-600 border border-green-200"
              : "bg-orange-50 text-orange-600 border border-orange-200"
          }`}
        >
          {news.status}
        </span>
      </div>
    </article>
  );
}
