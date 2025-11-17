#!/bin/bash
set -e

# === CONFIGURATION ===
JDL_FILE="seeder.jdl"
CACHE_DIR=".jhipster-cache"
VUE_CACHE_DIR=".create-vue-cache"
BACKEND_DIR="../backend"
FRONTEND_DIR="../frontend"
API_URL="http://localhost:8080"

# === Determine absolute script path (fix for template path issue) ===
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TEMPLATE_DIR="$SCRIPT_DIR/template"

echo "🚀 Generating JHipster monolith in temporary cache..."
rm -rf "$SCRIPT_DIR/$CACHE_DIR" "$SCRIPT_DIR/$VUE_CACHE_DIR"
mkdir -p "$SCRIPT_DIR/$CACHE_DIR"
cp "$SCRIPT_DIR/$JDL_FILE" "$SCRIPT_DIR/$CACHE_DIR/"
cd "$SCRIPT_DIR/$CACHE_DIR"

# Generate JHipster project (skip install for speed)
jhipster import-jdl "$JDL_FILE" --force --no-insight --skip-install

echo "📁 Creating clean project structure..."
cd "$SCRIPT_DIR"
rm -rf "$BACKEND_DIR" "$FRONTEND_DIR"
mkdir -p "$BACKEND_DIR" "$FRONTEND_DIR"

cd "$SCRIPT_DIR/$CACHE_DIR"

echo "📦 Moving backend (Java + Gradle) files..."
mv gradlew gradlew.bat gradle settings.gradle build.gradle buildSrc "$SCRIPT_DIR/$BACKEND_DIR/" 2>/dev/null || true
mv gradle.properties "$SCRIPT_DIR/$BACKEND_DIR/" 2>/dev/null || true
mv .gradle "$SCRIPT_DIR/$BACKEND_DIR/" 2>/dev/null || true
mv src/main/java "$SCRIPT_DIR/$BACKEND_DIR/src/main/" 2>/dev/null || true
mv src/main/resources "$SCRIPT_DIR/$BACKEND_DIR/src/main/" 2>/dev/null || true
mv src/test "$SCRIPT_DIR/$BACKEND_DIR/src/" 2>/dev/null || true

# === Sonar properties handling ===
echo "🔍 Checking for sonar-project.properties..."
if [ -f "$SCRIPT_DIR/$CACHE_DIR/sonar-project.properties" ]; then
  echo "📋 Copying sonar-project.properties..."
  cp "$SCRIPT_DIR/$CACHE_DIR/sonar-project.properties" "$SCRIPT_DIR/$BACKEND_DIR/"
else
  echo "⚠️ No sonar-project.properties found, creating dummy file..."
  echo "# Dummy Sonar config to satisfy Gradle plugin" > "$SCRIPT_DIR/$BACKEND_DIR/sonar-project.properties"
fi

# === Create fresh Vue app ===
echo "🎨 Creating fresh Vue 3 + Vite app (fully non-interactive)..."
cd "$SCRIPT_DIR"
rm -rf "$VUE_CACHE_DIR"
mkdir -p "$VUE_CACHE_DIR"

echo "⏳ Running create-vue non-interactively in $VUE_CACHE_DIR..."
npx --yes create-vue@latest "$SCRIPT_DIR/$VUE_CACHE_DIR" --default --typescript

echo "📦 Moving generated Vue app into $FRONTEND_DIR..."
cd "$FRONTEND_DIR"
shopt -s dotglob
mv "$SCRIPT_DIR/$VUE_CACHE_DIR"/* .
shopt -u dotglob

# Remove unwanted git init
rm -rf .git 2>/dev/null || true

echo "📦 Installing Vue dependencies..."
npm install >/dev/null 2>&1 || echo "⚠️ npm install had minor issues (check manually if needed)."

# === Apply template overrides ===
echo "🧩 Applying clean template overrides..."
if [ ! -d "$TEMPLATE_DIR" ]; then
  echo "❌ ERROR: Template directory not found: $TEMPLATE_DIR"
  exit 1
fi

cp "$TEMPLATE_DIR/index.html" ./index.html
cp "$TEMPLATE_DIR/main.ts" ./src/main.ts
cp "$TEMPLATE_DIR/App.vue" ./src/App.vue
cp "$TEMPLATE_DIR/router.ts" ./src/router.ts
cp "$TEMPLATE_DIR/vite.config.js" ./vite.config.js

# === Copy selected JHipster frontend modules ===
echo "📦 Copying selected JHipster frontend modules..."
mkdir -p src/jhipster
cp -r "$SCRIPT_DIR/$CACHE_DIR/src/main/webapp/app/entities" src/jhipster/entities 2>/dev/null || echo "⚠️ No entities found"
cp -r "$SCRIPT_DIR/$CACHE_DIR/src/main/webapp/app/shared" src/jhipster/shared 2>/dev/null || true
cp -r "$SCRIPT_DIR/$CACHE_DIR/src/main/webapp/app/core" src/jhipster/core 2>/dev/null || true

# === Update proxy ===
echo "⚙️ Updating API proxy..."
sed -i.bak "s|__API_URL__|$API_URL|g" vite.config.js && rm vite.config.js.bak

# === Cleanup ===
echo "🧹 Cleaning temporary caches..."
cd "$SCRIPT_DIR"
rm -rf "$CACHE_DIR" "$VUE_CACHE_DIR"

echo
echo "✅ Split complete! Independent Vue + Vite frontend ready."
echo
echo "🧩 Project structure:"
echo "  - $BACKEND_DIR/: Spring Boot + Gradle backend"
echo "  - $FRONTEND_DIR/: Fresh Vue 3 + Vite frontend (TypeScript)"
echo
echo "💡 Usage:"
echo "  1️⃣ Start backend:"
echo "     cd $BACKEND_DIR && ./gradlew bootRun"
echo
echo "  2️⃣ Start frontend:"
echo "     cd $FRONTEND_DIR && npm run dev"
echo
echo "  3️⃣ Open browser:"
echo "     👉 http://localhost:9000"
echo
echo "🌐 Proxy ready: /api/* → $API_URL"
