/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        // UNIL exact colors from their website
        'unil-blue': {
          DEFAULT: '#4aa3df', // Exact UNIL blue
          dark: '#357abd',
          light: '#e8f4fd',
        },
        'unil-category': {
          blue: '#4aa3df',
          green: '#28a745',
          orange: '#fd7e14',
          purple: '#6f42c1',
        },
      },
      fontFamily: {
        sans: ['system-ui', 'Segoe UI', 'Roboto', 'Helvetica Neue', 'Arial', 'sans-serif'],
      },
    },
  },
  plugins: [],
}
