#!/bin/bash
set -e

echo "🚀 Generating JHipster monolith with Vue frontend..."
jhipster import-jdl seeder.jdl --force --no-insight --skip-install

echo "📁 Creating project structure: backend + frontend"
mkdir -p ../cinebuddy-backend/src/main ../cinebuddy-backend/src/test ../cinebuddy-frontend

echo "📦 Moving backend files..."
shopt -s extglob
# Move everything except src/ and build/ to backend root
mv !(src|build) ../cinebuddy-backend/

# Move Java + resources to backend/src/main
mv src/main/!(webapp) ../cinebuddy-backend/src/main/ || true

# Move test code
[ -d src/test ] && mv src/test ../cinebuddy-backend/src/ || true
shopt -u extglob

echo "🎨 Extracting Vue frontend..."
mv src/main/webapp ../cinebuddy-frontend/

cd ../cinebuddy-frontend

# Initialize npm dependencies for the Vue frontend
echo "📦 Installing Vue dependencies..."
npm install

echo "✅ Done!"
echo
echo "🧩 Project structure created:"
echo "  - cinebuddy-backend/: Spring Boot + Gradle (run with './gradlew bootRun')"
echo "  - cinebuddy-frontend/: Vue app (run with 'npm run serve')"
echo
echo "💡 Tips:"
echo "  1️⃣ Start the backend:"
echo "     cd ../cinebuddy-backend && ./gradlew bootRun"
echo "  2️⃣ Start the frontend:"
echo "     cd ../cinebuddy-frontend && npm run serve"
echo "  3️⃣ Access the app at:"
echo "     👉 http://localhost:9000"

