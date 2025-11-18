#!/bin/bash
set -euo pipefail

# =====================
# CONFIG
# =====================
JDL_FILE="seeder.jdl"
CACHE_DIR=".jhipster-cache"
VUE_CACHE_DIR=".vue-cache"
BACKEND_DIR="../backend"
FRONTEND_DIR="../frontend"
TEMPLATE_DIR="$(dirname "$0")/template"
API_URL="http://localhost:8080"

# Cross-platform sed -i
sed_inplace() {
  if [[ "$OSTYPE" == "darwin"* ]]; then
    sed -i '' "$@"
  else
    sed -i "$@"
  fi
}

echo "🚀 Generating JHipster monolith in cache directory..."

rm -rf "$CACHE_DIR"
mkdir -p "$CACHE_DIR"
cp "$JDL_FILE" "$CACHE_DIR/"
cd "$CACHE_DIR"

# =====================
# JHIPSTER GENERATE
# =====================
jhipster import-jdl "$JDL_FILE" --force --no-insight --skip-install

cd ..
rm -rf "$BACKEND_DIR" "$FRONTEND_DIR"
mkdir -p "$BACKEND_DIR/src/main" "$BACKEND_DIR/src/test" "$FRONTEND_DIR"

cd "$CACHE_DIR"

# =====================
# BACKEND EXTRACTION
# =====================
echo "📦 Moving backend (Java + Gradle) files..."

mv gradlew gradlew.bat gradle settings.gradle build.gradle buildSrc ../$BACKEND_DIR/ || true
mv src/main/java ../$BACKEND_DIR/src/main/ || true
mv src/main/resources ../$BACKEND_DIR/src/main/ || true
mv src/test ../$BACKEND_DIR/src/ || true

echo "🧩 Copying configuration files..."
find . -type f \( -name "*.properties" -o -name "*.yml" -o -name "*.yaml" \) \
  -exec cp --parents {} ../$BACKEND_DIR/ \; || true

# =====================
# REMOVE FRONTEND BUILD FROM BACKEND
# =====================
echo "🛠 Removing JHipster frontend build logic from backend..."

# Remove unnecessary Gradle scripts
rm -f ../$BACKEND_DIR/gradle/webpack.gradle
rm -f ../$BACKEND_DIR/gradle/webapp.gradle
rm -f ../$BACKEND_DIR/src/main/webapp || true

# Patch build.gradle to remove Node plugin, webpack, NpmTask, etc.
sed_inplace '/node-gradle/d' ../$BACKEND_DIR/build.gradle
sed_inplace '/webpack.gradle/d' ../$BACKEND_DIR/build.gradle
sed_inplace '/webapp.gradle/d' ../$BACKEND_DIR/build.gradle
sed_inplace '/NpmTask/d' ../$BACKEND_DIR/build.gradle
sed_inplace '/npmSetup/d' ../$BACKEND_DIR/build.gradle
sed_inplace '/npmInstall/d' ../$BACKEND_DIR/build.gradle
sed_inplace '/webapp/d' ../$BACKEND_DIR/build.gradle

# =====================
# APPLY BACKEND TEMPLATE
# =====================
echo "🧩 Applying backend template overrides..."
cp -f "$TEMPLATE_DIR/backend/"* ../$BACKEND_DIR/ 2>/dev/null || true

# =====================
# FRONTEND CREATION (NON-INTERACTIVE!)
# =====================
echo "🎨 Creating Vue 3 + Vite app (non-interactive)..."

cd ..
rm -rf "$VUE_CACHE_DIR"
mkdir -p "$VUE_CACHE_DIR"
cd "$VUE_CACHE_DIR"

echo "⏳ Running create-vue non-interactively..."
npx --yes create-vue@latest "$PWD" --skip-prompts --default --typescript --skip-git

echo "📦 Moving Vue app to frontend folder..."
rm -rf "../frontend"
mkdir -p "../frontend"
cp -R . "../frontend"

echo "🧩 Applying Vue template overrides..."
cp -f "$TEMPLATE_DIR/vue/"* ../frontend/ 2>/dev/null || true

# Cleanup template cache
cd ..
rm -rf "$VUE_CACHE_DIR"

# =====================
# CLEANUP
# =====================
echo "🧹 Removing JHipster cache..."
rm -rf "$CACHE_DIR"

echo "✅ Split complete!"
echo
echo "🧩 Project structure:"
echo " - $BACKEND_DIR → Spring Boot backend"
echo " - $FRONTEND_DIR → Vue 3 + Vite frontend"
echo
echo "💡 Start commands:"
echo "  Backend: cd $BACKEND_DIR && ./gradlew bootRun"
echo "  Frontend: cd $FRONTEND_DIR && npm install && npm run dev"
echo
echo "🌐 Proxy ready: /api/* → $API_URL"
