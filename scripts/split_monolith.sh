#!/bin/bash
set -euo pipefail

# === Configuration ===
JDL_FILE="seeder.jdl"
CACHE_DIR=".jhipster-cache"
VUE_CACHE_DIR=".vue-cache"
BACKEND_DIR="../backend"
FRONTEND_DIR="../frontend"
TEMPLATE_DIR="$(dirname "$0")/template"
API_URL="http://localhost:8080"

echo "🚀 Generating JHipster project inside cache directory..."
rm -rf "$CACHE_DIR"
mkdir -p "$CACHE_DIR"
cp "$JDL_FILE" "$CACHE_DIR/"
cd "$CACHE_DIR"

# === Generate JHipster project ===
jhipster import-jdl "$JDL_FILE" --force --no-insight --skip-install

echo "📁 Preparing clean project structure..."
cd ..
rm -rf "$BACKEND_DIR" "$FRONTEND_DIR"
mkdir -p "$BACKEND_DIR/src/main" "$BACKEND_DIR/src/test" "$FRONTEND_DIR"

cd "$CACHE_DIR"

# === Backend extraction ===
echo "📦 Moving backend (Java + Gradle) files..."
mv gradlew gradlew.bat gradle settings.gradle build.gradle buildSrc ../$BACKEND_DIR/ 2>/dev/null || true
mv src/main/java ../$BACKEND_DIR/src/main/ 2>/dev/null || true
mv src/main/resources ../$BACKEND_DIR/src/main/ 2>/dev/null || true
mv src/test ../$BACKEND_DIR/src/ 2>/dev/null || true

echo "🧩 Copying properties and YAML configs..."
find . -type f \( -name "*.properties" -o -name "*.yml" -o -name "*.yaml" \) -exec cp --parents {} ../$BACKEND_DIR/ \; 2>/dev/null || true

# === Apply backend build template ===
echo "🧩 Applying backend build template..."
cp -f "$TEMPLATE_DIR/backend/"* ../$BACKEND_DIR/ 2>/dev/null || true

# === Vue frontend creation ===
echo "🎨 Creating fresh Vue 3 + Vite app..."
rm -rf "../$VUE_CACHE_DIR"
mkdir -p "../$VUE_CACHE_DIR"
cd "../$VUE_CACHE_DIR"

echo "⏳ Running create-vue non-interactively..."
npx --yes create-vue@latest . -- --default --typescript --skip-git

echo "📦 Moving generated Vue app into ../frontend..."
rm -rf "../frontend" && mkdir -p "../frontend"
cp -R . ../frontend/

echo "🧩 Applying Vue template overrides..."
cp -f "$TEMPLATE_DIR/vue/"* ../frontend/ 2>/dev/null || true

cd ..
rm -rf "$VUE_CACHE_DIR"

# === Cleanup ===
echo "🧹 Cleaning up cache..."
rm -rf "$CACHE_DIR"

echo "✅ Split complete!"
echo
echo "🧩 Project structure:"
echo "  - $BACKEND_DIR/: Spring Boot backend (run with './gradlew bootRun')"
echo "  - $FRONTEND_DIR/: Vue 3 + Vite frontend (run with 'npm run dev')"
echo
echo "💡 Usage:"
echo "  1️⃣ cd $BACKEND_DIR && ./gradlew bootRun"
echo "  2️⃣ cd $FRONTEND_DIR && npm install && npm run dev"
echo
echo "🌐 Proxy ready: /api/* → $API_URL"
