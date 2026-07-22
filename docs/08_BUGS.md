# 🐛 HackForge Known Issues & Bug Log

## 1. Resolved Bugs & Fix Record

### BUG-001: Port 3000 Conflict & Stale Backend Process
- **Symptom**: `Web server failed to start. Port 3000 was already in use.`
- **Root Cause**: An orphaned Spring Boot process was holding port 3000 from a previous run.
- **Resolution**: Freed port 3000 and restarted `mvn spring-boot:run`.

### BUG-002: Circular Dependency on `PasswordEncoder`
- **Symptom**: `BeanCurrentlyInCreationException: Error creating bean with name 'passwordEncoder'`.
- **Root Cause**: `SecurityConfig` declared `@Bean PasswordEncoder` while depending on `OAuth2SuccessHandler`, which requested `PasswordEncoder` via constructor injection.
- **Resolution**: Instantiated `BCryptPasswordEncoder` directly inside `OAuth2SuccessHandler.java` to break the bean creation cycle.

### BUG-003: Missing Repository Methods (`existsByUsernameIgnoreCase`)
- **Symptom**: `AuthServiceImpl.java` failed compilation with `cannot find symbol: method existsByUsernameIgnoreCase`.
- **Root Cause**: `UserRepository.java` interface lacked declarations for ignore-case query methods.
- **Resolution**: Added `existsByUsernameIgnoreCase` and `existsByEmailIgnoreCase` to `UserRepository.java`.

### BUG-004: User Profile Dropdown Menu Overlapping Dashboard
- **Symptom**: Profile menu dropdown clipped under dashboard cards.
- **Root Cause**: Insufficient `z-index` and relative container positioning.
- **Resolution**: Updated `Header.jsx` dropdown styling to `z-index: 9999`, `position: absolute`, and `top: calc(100% + 8px)` with `box-shadow: 0 12px 32px rgba(0,0,0,0.6)`.

---

## 2. Prevention Guidelines
- Always verify `UserRepository` method signatures match `AuthServiceImpl` callers before building.
- Instantiate helper utilities directly inside handlers to avoid circular bean references with `SecurityConfig`.
