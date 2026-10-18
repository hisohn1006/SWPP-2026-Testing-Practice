# Spec: `DueDateUtils.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `dueDateLabel(dueDate: LocalDate?, today: LocalDate, isCompleted: Boolean): String`

**Intent**: Return `CLEAR` for completed tasks, an empty label for active tasks without a due date, or an exact calendar-day D-day label for active tasks with a due date. The count is the total days between the two dates, including across month, year, and leap-day boundaries.

**Pre-conditions**
- `today` is a valid `LocalDate`; `dueDate` is either a valid `LocalDate` or null.

**Post-conditions**
- Success cases
  - Completion overrides a non-null due date: `2026-10-20` against `2026-10-19` returns `CLEAR`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_completedWithDate_returnsClear`
  - Completion also overrides a missing due date: `dueDate=null` against `2026-10-19` returns `CLEAR`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_completedWithoutDate_returnsClear`
  - An active task with `dueDate=null` against `2026-10-19` returns `""`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_activeWithoutDate_returnsEmpty`
  - For an active task, `2026-10-22` against `2026-10-19` returns `D-3`; `2026-10-16` returns `D+3`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_threeDaysWithinMonth_returnsExactLabels`
- Error cases
  - None; all nullable due-date values are valid inputs.
- Edge cases
  - At the zero-day boundary, `dueDate=2026-10-19`, `today=2026-10-19`, active returns `D-Day`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_dueToday_returnsDDay`
  - One day after that boundary, `dueDate=2026-10-20`, `today=2026-10-19`, active returns `D-1`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_dueTomorrow_returnsDMinus1`
  - One day before that boundary, `dueDate=2026-10-18`, `today=2026-10-19`, active returns `D+1`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_overdueYesterday_returnsDPlus1`
  - Across the month boundary, `today=2026-10-31` with due dates `2026-10-30`, `2026-10-31`, and `2026-11-01` returns `D+1`, `D-Day`, and `D-1` respectively. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_monthBoundaryNeighbors_returnsExactLabels`
  - Further across that boundary, `today=2026-10-30`, `dueDate=2026-11-02` returns `D-3`, while `today=2026-11-02`, `dueDate=2026-10-30` returns `D+3`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_monthSpanningThreeDays_returnsExactLabels`
  - At and immediately around a full calendar month, `today=2026-10-19` with due dates `2026-11-18`, `2026-11-19`, and `2026-11-20` returns `D-30`, `D-31`, and `D-32` respectively. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_fullMonthBoundaryFuture_returnsTotalDays`
  - In the reverse direction, due date `2026-10-19` with `today=2026-11-18`, `2026-11-19`, and `2026-11-20` returns `D+30`, `D+31`, and `D+32` respectively. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_fullMonthBoundaryPast_returnsTotalDays`
  - Across leap day, `today=2028-02-29` with due dates `2028-02-28`, `2028-02-29`, and `2028-03-01` returns `D+1`, `D-Day`, and `D-1` respectively; `2028-02-28` to `2028-03-01` returns `D-2`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_leapDayBoundary_returnsExactLabels`
  - Across the year boundary, `today=2026-12-31` with due dates `2026-12-30`, `2026-12-31`, and `2027-01-01` returns `D+1`, `D-Day`, and `D-1` respectively. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_yearBoundaryNeighbors_returnsExactLabels`

**Undefined behavior** (optional)
- None.
