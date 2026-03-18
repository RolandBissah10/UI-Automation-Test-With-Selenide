# Stage 1: Build and run tests
FROM maven:3.9.6-eclipse-temurin-17-alpine AS test-runner

# Install Chrome and dependencies
RUN apk add --no-cache \
    chromium \
    chromium-chromedriver \
    nss \
    freetype \
    harfbuzz \
    ttf-freefont \
    && rm -rf /var/cache/apk/*

# Set Chrome environment variables
ENV CHROME_BIN=/usr/bin/chromium-browser \
    CHROME_PATH=/usr/lib/chromium/ \
    CHROMIUM_FLAGS="--disable-gpu --no-sandbox --disable-dev-shm-usage"

# Set working directory
WORKDIR /app

# Copy project files
COPY pom.xml .
COPY src ./src

# Download dependencies (cached layer)
RUN mvn dependency:go-offline -B

# Default command: run all tests headless
CMD ["mvn", "test", \
     "-Dbrowser=chrome", \
     "-Dheadless=true", \
     "-Dwebdriver.chrome.driver=/usr/bin/chromedriver", \
     "-B", "--no-transfer-progress"]
