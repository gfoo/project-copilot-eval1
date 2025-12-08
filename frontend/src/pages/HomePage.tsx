import { Link } from "react-router-dom";

function HomePage() {
  return (
    <div className="container mx-auto p-4">
      <h1 className="text-3xl font-bold mb-6">News Listing</h1>
      <div className="space-y-4">
        <div className="border p-4 rounded-lg">
          <h2 className="text-xl font-semibold mb-2">
            <Link to="/news/1" className="text-primary-600 hover:text-primary-800">
              Sample News Article 1
            </Link>
          </h2>
          <p className="text-gray-600">This is a sample news article description.</p>
        </div>
        <div className="border p-4 rounded-lg">
          <h2 className="text-xl font-semibold mb-2">
            <Link to="/news/2" className="text-primary-600 hover:text-primary-800">
              Sample News Article 2
            </Link>
          </h2>
          <p className="text-gray-600">This is another sample news article description.</p>
        </div>
      </div>
    </div>
  );
}

export default HomePage;
