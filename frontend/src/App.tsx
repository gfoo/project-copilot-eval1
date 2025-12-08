import { useState } from "react";
import { useQuery } from "@tanstack/react-query";

async function fetchHello(name: string): Promise<string> {
  const url = name
    ? `http://localhost:8080/hello?name=${encodeURIComponent(name)}`
    : "http://localhost:8080/hello";
  const response = await fetch(url);
  if (!response.ok) {
    throw new Error("Network response was not ok");
  }
  return response.text();
}

function App() {
  const [inputName, setInputName] = useState("");
  const [name, setName] = useState("");

  const { data, isLoading, error } = useQuery({
    queryKey: ["hello", name],
    queryFn: () => fetchHello(name),
  });

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-900 flex items-center justify-center p-4">
      <div className="max-w-md w-full space-y-4 bg-white dark:bg-gray-800 p-6 rounded-lg shadow-lg">
        <h1 className="text-2xl font-bold text-gray-900 dark:text-white text-center">
          Hello App
        </h1>
        <input
          type="text"
          placeholder="Enter your name (optional)"
          value={inputName}
          onChange={(e) => setInputName(e.target.value)}
          className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent dark:bg-gray-700 dark:text-white"
        />
        <button 
          onClick={() => setName(inputName)}
          className="w-full bg-primary-600 hover:bg-primary-700 text-white font-medium py-2 px-4 rounded-lg transition-colors duration-200"
        >
          Update Message
        </button>
        {isLoading && <p className="text-gray-600 dark:text-gray-400">Loading...</p>}
        {error && <p className="text-red-600 dark:text-red-400">Error: {error.message}</p>}
        {data && <p className="text-green-600 dark:text-green-400 font-medium">{data}</p>}
      </div>
    </div>
  );
}

export default App;
