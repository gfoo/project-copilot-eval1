import { useParams, Link } from "react-router-dom";

function NewsDetailPage() {
  const { id } = useParams<{ id: string }>();

  return (
    <div className="container mx-auto p-4">
      <Link to="/" className="text-primary-600 hover:text-primary-800 mb-4 inline-block">
        ← Back to Home
      </Link>
      <article className="mt-4">
        <h1 className="text-4xl font-bold mb-4">News Article {id}</h1>
        <div className="prose max-w-none">
          <p className="text-gray-700 mb-4">
            This is the detail page for news article with ID: <strong>{id}</strong>
          </p>
          <p className="text-gray-600">
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod
            tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam,
            quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo
            consequat.
          </p>
        </div>
      </article>
    </div>
  );
}

export default NewsDetailPage;
