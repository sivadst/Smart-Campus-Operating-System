# Contributing to Smart Campus OS

Thank you for your interest in contributing! This guide will help you get started.

## Development Setup

1. **Fork** the repository
2. **Clone** your fork: `git clone https://github.com/<your-username>/Smart-Campus-Operating-System.git`
3. **Install** Java 21 JDK
4. **Run** the project: `./mvnw spring-boot:run`

## Code Standards

- Follow Java 21 conventions
- Use Lombok annotations to reduce boilerplate
- All entities must extend `Auditable`
- All DTOs must use Jakarta Bean Validation
- All controllers must have Swagger annotations (`@Tag`, `@Operation`)
- All protected endpoints must have `@PreAuthorize`

## Commit Messages

We use [Conventional Commits](https://www.conventionalcommits.org/):

```
feat: add new feature
fix: fix a bug
docs: update documentation
test: add or update tests
refactor: restructure code
chore: maintenance tasks
```

## Pull Request Process

1. Create a feature branch from `main`
2. Write tests for new functionality
3. Ensure all existing tests pass: `./mvnw test`
4. Update documentation if needed
5. Submit PR with a clear description

## Reporting Issues

Use GitHub Issues with the appropriate template (Bug Report or Feature Request).

## Code of Conduct

Be respectful, inclusive, and constructive in all interactions.
