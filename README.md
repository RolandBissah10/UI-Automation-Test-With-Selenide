# Selenide Swag Labs — UI Test Automation

Automated UI test suite for [Swag Labs (saucedemo.com)](https://www.saucedemo.com) built with **Selenide**, **JUnit 5**, and **Allure Reports**.

---

## Tech Stack

| Tool | Purpose |
|---|---|
| Java 17 | Language |
| Selenide 7.x | UI automation (Selenium wrapper) |
| JUnit 5 | Test framework |
| Allure | Test reporting |
| Maven | Build & dependency management |
| Docker | Isolated test execution |
| GitHub Actions / Jenkins | CI/CD |

---

## Project Structure

```
selenide-swag-labs/
├── src/
│   ├── main/java/com/swaglabs/
│   │   ├── config/         # TestConfig, ConfigManager
│   │   ├── pages/          # Page Object classes
│   │   └── utils/          # ScreenshotUtils, TestData
│   └── test/
│       ├── java/com/swaglab/
│       │   ├── base/       # BaseTest (Selenide + Allure setup)
│       │   ├── smoke/      # LoginTest, ProductsPageTest
│       │   └── regression/ # CartTest, CheckoutTest
│       └── resources/
│           ├── config.properties
│           ├── allure.properties
│           ├── logback-test.xml
│           └── junit-platform.properties
├── .github/workflows/
│   └── ui-tests.yml        # GitHub Actions pipeline
├── Dockerfile
├── docker-compose.yml
├── Jenkinsfile
└── pom.xml
```

---

## Page Objects

| Page | Class | Key Responsibilities |
|---|---|---|
| Login | `LoginPage` | Login, error validation |
| Products | `ProductsPage` | List, sorting, add to cart |
| Product Details | `ProductDetailsPage` | Details view, add to cart |
| Cart | `CartPage` | View cart, remove items, checkout |
| Checkout | `CheckoutPage` | Info form, overview, confirmation |

---

## Test Suites

### Smoke (`-Psmoke`)
- `LoginTest` — valid login, locked user, invalid creds, empty fields, logout
- `ProductsPageTest` — page load, sorting, product details, back navigation

### Regression (`-Pregression`)
- `CartTest` — add/remove single & multiple items, empty cart, persistence
- `CheckoutTest` — full flow, field validations, cancel, back to products

---

## Running Tests

### Prerequisites
- Java 17+
- Maven 3.9+
- Chrome or Firefox installed

### Run all tests
```bash
mvn test -Pall
```

### Run smoke suite only
```bash
mvn test -Psmoke
```

### Run regression suite only
```bash
mvn test -Pregression
```

### Run in headed mode (visible browser)
```bash
mvn test -Psmoke -Dheadless=false
```

### Run with Firefox
```bash
mvn test -Pall -Dbrowser=firefox
```

---

## Reports

### Generate & open Allure report
```bash
mvn allure:serve
```

### Generate static report
```bash
mvn allure:report
# Opens at: target/site/allure-maven-plugin/index.html
```

Screenshots on failures are automatically saved to `target/selenide-screenshots/` and attached to the Allure report.

---

## Docker

### Build and run all tests
```bash
docker-compose up tests-all
```

### Run smoke tests only
```bash
docker-compose up tests-smoke
```

### Run regression tests only
```bash
docker-compose up tests-regression
```

### View Allure report (after tests complete)
```bash
docker-compose up allure-report
# Navigate to http://localhost:5050
```

---

## CI/CD

### GitHub Actions
Tests run automatically on every push. The workflow:
1. **Smoke tests** — triggered on every push to any branch
2. **Regression tests** — triggered on PRs to `main`/`develop`
3. **Allure report** — published to GitHub Pages on merge to `main`

Manually trigger with custom suite/browser via **Actions → Run workflow**.

### Jenkins
Configure a Pipeline job pointing to the `Jenkinsfile`. Supports parameterised builds for suite, browser, and headless toggle.

---

## Configuration

Override any setting via system properties or `src/test/resources/config.properties`:

| Property | Default | Description |
|---|---|---|
| `base.url` | `https://www.saucedemo.com` | App URL |
| `browser` | `chrome` | Browser (`chrome`/`firefox`) |
| `headless` | `true` | Headless mode |
| `timeout` | `10000` | Element wait timeout (ms) |

---

## Test Users

| User | Username | Password |
|---|---|---|
| Standard | `standard_user` | `secret_sauce` |
| Locked | `locked_out_user` | `secret_sauce` |
| Problem | `problem_user` | `secret_sauce` |
