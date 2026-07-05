import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// Frontend runs on its own port, independent of the Main and REST microservice apps.
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      // Forwarded to the Main Spring Boot application during development.
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
});
