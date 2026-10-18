# Spec: `TasksListBindings.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `setDueDateLabel(textView: TextView, task: Task?): Unit`

**Intent**: Display the task's D-day label in the supplied text view.

**Pre-conditions**
- `textView` is attached to a usable Android context.

**Post-conditions**
- Success cases
  - A completed task makes `CLEAR` visible. `[Integration]`
    - Tip: Given a fresh empty `TextView` and task `id="done-1"`, `dueDate=2026-10-20`, `isCompleted=true` / When `setDueDateLabel` is called / Then text is `CLEAR` and visibility is `VISIBLE`.
  - An active task with no due date has no label. `[Integration]`
    - Tip: Given a fresh empty `TextView` and task `id="active-1"`, `dueDate=null`, `isCompleted=false` / When `setDueDateLabel` is called / Then text is `""`.
- Error cases
  - None.
- Edge cases
  - A null task has no label. `[Integration]`
    - Tip: Given a fresh empty `TextView` / When `setDueDateLabel` is called with `task=null` / Then text is `""`.

**Undefined behavior** (optional)
- Behavior when the same view is rebound to a different task is unspecified in this draft.
