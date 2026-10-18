# Spec: `DueDateUtils.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `dueDateLabel(dueDate: LocalDate?, today: LocalDate, isCompleted: Boolean): String`

**Intent**: Format a task's due date for the task list. Completed tasks show `CLEAR`.

**Pre-conditions**
- `today` is a valid calendar date.

**Post-conditions**
- Success cases
  - A completed task returns `CLEAR` regardless of due date. `[Unit]`
    - Tip: Given `dueDate=2026-10-20`, `today=2026-10-19`, `isCompleted=true` / When `dueDateLabel` is called / Then it returns `CLEAR`.
  - An active task without a due date returns the empty string. `[Unit]`
    - Tip: Given `dueDate=null`, `today=2026-10-19`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns `""`.
  - An active task due today returns `D-Day`. `[Unit]`
    - Tip: Given `dueDate=2026-10-19`, `today=2026-10-19`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns `D-Day`.
  - An active task due later or earlier in the same month shows the day distance. `[Unit]`
    - Tip: Given `dueDate=2026-10-22`, `today=2026-10-19`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns `D-3`.
- Error cases
  - None.
- Edge cases
  - One day after today uses `D-1`. `[Unit]`
    - Tip: Given `today=2026-10-19`, `dueDate=2026-10-20`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns `D-1`.
  - One day before today uses `D+1`. `[Unit]`
    - Tip: Given `today=2026-10-19`, `dueDate=2026-10-18`, `isCompleted=false` / When `dueDateLabel` is called / Then it returns `D+1`.

**Undefined behavior** (optional)
- None.
