# Testing Practice Report

**Student:** 2024-11521 Hoonil Sohn  
**Forked repository:** https://github.com/hisohn1006/SWPP-2026-Testing-Practice  
**Tested code:** `origin/feature/due-date` (`7e091a7`), with only specs and tests added on top. Production code was not changed.

## Spec refinements

### `DueDateUtils.kt` — `dueDateLabel`

The AI draft described the normal labels and tested dates within one month, but it did not make **total elapsed calendar days** an enforceable oracle for a full-month span. Its `2026-10-19` to `2026-10-22` example passed even with the actual defect. The reviewed spec states exact expected labels for completed tasks with and without a due date, missing due dates, today, yesterday, and tomorrow. It adds neighboring dates on both sides of month, leap-day, and year boundaries. Most critically, it specifies `2026-10-19` to `2026-11-18`, `2026-11-19`, and `2026-11-20` as `D-30`, `D-31`, and `D-32`, plus the reverse `D+30`, `D+31`, and `D+32`. These values prevent a test from accepting only the day component of a `Period` as the total day count.

### `TasksListBindings.kt` — `setDueDateLabel`

The AI draft checked the text on a fresh view but left rebinding behavior undefined and did not require an empty label to hide the view. A reused row can retain old text, so fresh-view tests alone cannot catch the stale-label bug. The reviewed spec requires each call to replace both text and visibility. It adds completed tasks with and without a due date, a fresh active task with no due date, a null task, and two rebinding cases that start from visible `CLEAR` and expect `""` plus `View.GONE`. The null and missing-date cases are valid empty bindings, not errors.

## Test results and actual bugs

The tests were run against the intentionally buggy feature code. Expected failing tests were retained as evidence; production code was not fixed.

| Command | Result | Defect evidence |
| --- | --- | --- |
| `./gradlew testDebugUnitTest --no-daemon` | 14 tests, 2 failed | `dueDateLabel_fullMonthBoundaryFuture_returnsTotalDays`: expected `[D-30, D-31, D-32]`, actual `[D-30, D-Day, D-1]`. `dueDateLabel_fullMonthBoundaryPast_returnsTotalDays`: expected `[D+30, D+31, D+32]`, actual `[D+30, D-Day, D+1]`. |
| `./gradlew connectedDebugAndroidTest --no-daemon` on connected `Medium_Phone` emulator | 7 tests, 4 failed | `setDueDateLabel_reboundActiveWithoutDate_clearsAndHides` and `setDueDateLabel_reboundNull_clearsAndHides` expected `""`, actual `"CLEAR"`. The fresh active-without-date and fresh-null cases expected `View.GONE` (8), actual `View.VISIBLE` (0). |

The unit failures correspond to the PR's first bug: `Period.between(today, dueDate).days` uses only the day component and drops full months. The initial unit suite passed because its cross-month examples spanned only a few days; the full-month boundary refinement exposed the defect. The integration rebinding failures correspond to the second bug: `setDueDateLabel` updates the TextView only for nonempty labels, so `CLEAR` survives when a completed task with no due date becomes active. The extra fresh-view failures show that empty labels are also left visible.

These are local Gradle and emulator results. A pull-request CI run was not used as evidence for this report.
