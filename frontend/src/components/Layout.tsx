import { ReactNode } from "react";
import Header from "./Header";

interface LayoutProps {
  children: ReactNode;
}

export default function Layout({ children }: LayoutProps) {
  return (
    <div className="min-h-screen bg-gray-50">
      <Header />
      <main>
        {children}
      </main>
      <footer className="bg-white border-t border-gray-200 mt-16">
        <div className="container mx-auto px-4 py-8">
          <div className="text-center">
            <p className="text-sm text-gray-500">
              © 2025 Université de Lausanne. Tous droits réservés.
            </p>
            <div className="mt-4 flex justify-center space-x-6">
              <a href="#" className="text-xs text-gray-400 hover:text-gray-600 transition-colors">
                Contact
              </a>
              <a href="#" className="text-xs text-gray-400 hover:text-gray-600 transition-colors">
                Protection des données
              </a>
              <a href="#" className="text-xs text-gray-400 hover:text-gray-600 transition-colors">
                Informations légales
              </a>
            </div>
          </div>
        </div>
      </footer>
    </div>
  );
}