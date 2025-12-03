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
    <div>
      <input
        type="text"
        placeholder="Enter your name (optional)"
        value={inputName}
        onChange={(e) => setInputName(e.target.value)}
      />
      <button onClick={() => setName(inputName)}>Update Message</button>
      {isLoading && <p>Loading...</p>}
      {error && <p>Error: {error.message}</p>}
      {data && <p>{data}</p>}
    </div>
  );
}

export default App;
