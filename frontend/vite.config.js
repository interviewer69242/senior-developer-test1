import { defineConfig } from 'vite';
export default defineConfig({server:{proxy:{'/trades':'http://localhost:8081'}}});
