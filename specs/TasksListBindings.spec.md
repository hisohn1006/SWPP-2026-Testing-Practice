# Spec: `TasksListBindings.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `setDueDateLabel(textView: TextView, task: Task?): Unit`

**Intent**: Bind the current task's D-day label to the text view. Each call replaces any previous label and visibility state; a task with no label leaves the view empty and hidden.

**Pre-conditions**
- `textView` is a valid Android `TextView`; its current text and visibility may reflect an earlier binding.
- The device's current local date supplies `today` to `dueDateLabel`; exact date-dependent formats are independently checked by its unit spec.

**Post-conditions**
- Success cases
  - A completed task `id="done-1"`, `dueDate=2026-10-20`, `isCompleted=true` sets text to `CLEAR` and visibility to `VISIBLE`, regardless of the device date. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_completedWithDate_showsClear`
  - A completed task `id="done-2"`, `dueDate=null`, `isCompleted=true` also sets text to `CLEAR` and visibility to `VISIBLE`. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_completedWithoutDate_showsClear`
  - An active task `id="active-1"`, `dueDate=null`, `isCompleted=false` sets text to `""` and visibility to `GONE`, even when the view previously showed `CLEAR`. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_reboundActiveWithoutDate_clearsAndHides`
  - A null task sets text to `""` and visibility to `GONE`, even when the view previously showed `CLEAR`. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_reboundNull_clearsAndHides`
- Error cases
  - None; null `task` is a valid empty binding.
- Edge cases
  - A fresh view bound to active task `id="active-1"`, `dueDate=null`, `isCompleted=false` has text `""` and visibility `GONE`. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_freshActiveWithoutDate_hidesEmptyLabel`
  - A fresh view bound to null task has text `""` and visibility `GONE`. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_freshNull_hidesEmptyLabel`

**Undefined behavior** (optional)
- Passing a null `textView` is outside the non-null Kotlin signature.
