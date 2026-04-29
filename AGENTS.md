# 🤖 Agents Workflow Guide

This document defines how coding agents (e.g., CodeX or similar tools) should operate in this repository.

---

## 🎯 Goal

Ensure a fast, smooth development flow without unnecessary interruptions.

Agents should prioritize:

* Continuous coding
* Minimal confirmation steps
* Clean commits
* Developer-controlled push

---

## 🚀 Core Workflow

### 1. Start Working Immediately

* Do **NOT** ask for confirmation like:

    * "Do you want me to proceed?"
    * "Should I implement this?"
* Assume the task is approved
* Start coding right away

---

### 2. Implementation Rules

* Follow existing project structure and conventions
* Keep the solution:

    * Simple
    * Clean
    * Maintainable
* Avoid over-engineering

---

### 3. Commit Policy (VERY IMPORTANT)

After completing a logical unit of work:

* ✅ **Always commit your changes**
* ❌ **NEVER push to remote**

```bash
git add .
git commit -m "<clear and concise message>"
```

#### Commit message guidelines:

* Use clear, short, meaningful messages
* Examples:

    * `add product variant entity`
    * `fix payment status mapping`
    * `refactor invoice service logic`
    * `update UI for supplier ledger`

---

### 4. No Push Rule 🚫

Agents are **NOT allowed** to push code.

```bash
# ❌ NEVER DO THIS
git push
```

Only the developer (human) will:

* Review changes
* Push to remote repository

---

### 5. Iteration Flow

For each task:

1. Implement solution
2. Ensure code compiles (if applicable)
3. Keep changes scoped and clean
4. Commit changes
5. Continue to next step (if any)

---

### 6. Handling Multi-Step Tasks

* Break work into smaller commits
* Do NOT wait until everything is done to commit
* Each commit should represent a meaningful step

---

### 7. When Requirements Are Unclear

* Make a **reasonable assumption**
* Proceed with implementation
* Do NOT block progress by asking questions

---

### 8. Code Quality Expectations

* Follow naming conventions
* Keep methods small and readable
* Avoid unnecessary complexity
* Prefer clarity over cleverness

---

## ⚙️ Environment Permissions

### Gradle Cache Access (IMPORTANT)

Agents are **allowed to use the global Gradle cache directory**:

```bash
~/.gradle
```

#### Rules:

* ✅ Allowed to read/write Gradle dependencies and cache
* ✅ Allowed to run Gradle commands that use global cache
* ❌ Do NOT restrict Gradle to sandbox-only cache

#### Purpose:

* Avoid re-downloading dependencies
* Improve build performance
* Ensure consistency with developer environment

---

## 🧠 Summary

* ✅ Code immediately
* ✅ Commit frequently
* ❌ Do NOT ask for confirmation
* ❌ Do NOT push
* ✅ Allowed to use `~/.gradle`
* ✅ Let developer handle final review & push

---

This workflow ensures:

* Faster development
* Less interruption
* Better control over final changes
