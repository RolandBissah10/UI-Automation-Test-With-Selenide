# User-Based Test Suites

This directory contains comprehensive test suites for each user type available in the Sauce Labs demo application. Each test class focuses on testing scenarios specific to a particular user account.

## Test Organization

The tests are organized by user role/behavior following the **Page Object Model (POM)** with **User-Based Test Scenarios** pattern.

### Directory Structure
```
src/test/java/com/swaglab/
├── base/               # Base test class with setup/teardown
├── smoke/              # Smoke tests (existing - general tests)
├── regression/         # Regression tests (existing - workflow tests)
└── users/              # NEW: User-specific test suites
    ├── StandardUserTest
    ├── LockedOutUserTest
    ├── ProblemUserTest
    ├── PerformanceGlitchUserTest
    ├── ErrorUserTest
    └── VisualUserTest
```

## User Test Suites

### 1. **StandardUserTest** ✅ Happy Path
- **User:** `standard_user`
- **Description:** Full access to all features with normal behavior
- **Tests:** 11 test cases
- **Scenarios:**
  - Successful login
  - View all products
  - Add/remove items from cart
  - Product sorting
  - Checkout flow
  - Logout

**Tags:** `user-standard`, `smoke`

---

### 2. **LockedOutUserTest** 🔒 Authentication Failure
- **User:** `locked_out_user`
- **Description:** Account locked - cannot access application
- **Tests:** 8 test cases
- **Scenarios:**
  - Login attempt fails with specific error message
  - Error icon display
  - Retry behavior
  - Error clearing
  - Cannot bypass with different credentials

**Tags:** `user-locked-out`, `authentication`

---

### 3. **ProblemUserTest** ⚠️ Data Integrity Issues
- **User:** `problem_user`
- **Description:** Can login but encounters broken images and display issues
- **Tests:** 10 test cases
- **Scenarios:**
  - Successful login
  - Broken product images
  - Product details with broken images
  - Can still add items to cart
  - Can complete checkout despite issues
  - Product sorting works
  - Prices still accessible

**Tags:** `user-problem`, `data-integrity`

---

### 4. **PerformanceGlitchUserTest** 🐌 Slow Performance
- **User:** `performance_glitch_user`
- **Description:** Pages load slowly but functionality works
- **Tests:** 10 test cases
- **Scenarios:**
  - Successful login (with delays)
  - All products load (slowly)
  - Add to cart works
  - Cart page loads slowly
  - Product details load slowly
  - Checkout flow works
  - Multiple sequential actions
  - Backward navigation

**Tags:** `user-performance`, `performance`

**Note:** Uses extended timeout (30 seconds) to accommodate delays.

---

### 5. **ErrorUserTest** ❌ UI Errors
- **User:** `error_user`
- **Description:** Can login but experiences UI errors during checkout
- **Tests:** 10 test cases
- **Scenarios:**
  - Successful login
  - View products normally
  - Add to cart works
  - Experiences error on checkout overview
  - Error messages display
  - Cannot complete checkout
  - Can go back and retry
  - Product operations work normally

**Tags:** `user-error`, `error-handling`

---

### 6. **VisualUserTest** 🎨 Visual Rendering
- **User:** `visual_user`
- **Description:** Visual rendering differences in UI elements
- **Tests:** 12 test cases
- **Scenarios:**
  - Successful login
  - All visual elements present
  - Product page layout correct
  - Cart badge displays correctly
  - Checkout labels display correctly
  - Confirmation page layout
  - Menu displays correctly
  - Sort dropdown visible
  - Footer displays correctly

**Tags:** `user-visual`, `visual-regression`

---

## Running the Tests

### Run all user tests
```bash
mvn clean test -Dgroups="user-standard or user-locked-out or user-problem or user-performance or user-error or user-visual"
```

### Run specific user tests
```bash
# Standard user tests
mvn clean test -Dgroups="user-standard"

# Locked out user tests
mvn clean test -Dgroups="user-locked-out"

# Problem user tests
mvn clean test -Dgroups="user-problem"

# Performance tests
mvn clean test -Dgroups="user-performance"

# Error handling tests
mvn clean test -Dgroups="user-error"

# Visual regression tests
mvn clean test -Dgroups="user-visual"
```

### Run by severity
```bash
# Critical tests only
mvn clean test -Dgroups="user-standard and smoke" -Dseverity="BLOCKER,CRITICAL"
```

### Run smoke tests (existing + new)
```bash
mvn clean test -Dgroups="smoke"
```

---

## Test Statistics

| User | Total Tests | Scenarios | Status |
|------|------------|-----------|--------|
| Standard User | 11 | Happy path, full flow | ✅ Complete |
| Locked Out User | 8 | Auth failures, error handling | ✅ Complete |
| Problem User | 10 | Data integrity, display issues | ✅ Complete |
| Performance | 10 | Slow loads, timeouts | ✅ Complete |
| Error User | 10 | UI errors, checkout failures | ✅ Complete |
| Visual User | 12 | UI rendering, visual elements | ✅ Complete |
| **Total** | **61** | **Comprehensive coverage** | ✅ |

---

## Best Practices Implemented

1. **Separation of Concerns**
   - Each user has its own test class
   - Tests focus on user-specific scenarios
   - No mixing of unrelated test cases

2. **Clear Test Organization**
   - Package structure: `com.swaglab.users`
   - Descriptive class names
   - Meaningful test method names with `@DisplayName`

3. **Proper Tagging**
   - Each test has appropriate `@Tag` annotations
   - Easy to filter and run specific test groups
   - Facilitates CI/CD integration

4. **Allure Reporting**
   - `@Epic`, `@Feature`, `@Story` annotations
   - `@Severity` levels for prioritization
   - Screenshots and page source on failures

5. **Configuration Management**
   - All users defined centrally in `TestConfig.java`
   - Configuration loaded from `config.properties`
   - No hardcoded values

6. **Reusable Base Class**
   - `BaseTest` handles common setup/teardown
   - Browser configuration centralized
   - Allure listener integration

7. **Page Object Model**
   - All interactions through page objects
   - No direct Selenide calls in tests
   - Easy to maintain and update

---

## Important Notes

### Timeout Configuration
- Standard timeout: 10 seconds
- Performance glitch user: 30 seconds (extended for slow loads)

### User Credentials
All users use the same password: `secret_sauce`

| Username | Password |
|----------|----------|
| standard_user | secret_sauce |
| locked_out_user | secret_sauce |
| problem_user | secret_sauce |
| performance_glitch_user | secret_sauce |
| error_user | secret_sauce |
| visual_user | secret_sauce |

---

## Reporting

After running tests:
```bash
# Generate Allure report
mvn allure:report

# Open Allure report
mvn allure:serve
```

The Allure report will show:
- Test results grouped by Epic (User Scenarios)
- Feature breakdown for each user
- Story details for specific scenarios
- Severity levels for prioritization
- Screenshots for failed tests

---

## Future Enhancements

1. **Parameterized Tests:** Use `@ParameterizedTest` for data-driven scenarios
2. **Test Data Builders:** Create builders for common test objects
3. **Custom Assertions:** Develop fluent assertions for better readability
4. **Performance Metrics:** Add execution time measurements
5. **Visual Regression:** Integrate visual comparison tools
6. **API Integration:** Add backend validation alongside UI tests
