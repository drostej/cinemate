import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

const API_URL = '__API_URL__'

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 9000,
    proxy: {
      '/api': {
        target: API_URL,
        changeOrigin: true,
        secure: false,
      },
    },
  },
  resolve: {
    alias: {
      '@': '/src',
    },
  },
})
