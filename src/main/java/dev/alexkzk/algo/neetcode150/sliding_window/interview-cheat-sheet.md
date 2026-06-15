
# Interviewer Cheat Sheet — Longest Substring Without Repeating Characters

**Source:** [LeetCode 3](https://leetcode.com/problems/longest-substring-without-repeating-characters/)  
**Difficulty:** Medium | **Topic:** Sliding Window / Two Pointers + Hash Map  
**Target role:** Software Engineer / Senior Software Engineer

---

## Problem to Read Aloud

> Given a string `input_string`, find the length of the **longest substring** without repeating characters.
>
> A **substring** is a contiguous non-empty sequence of characters within a string.

**Examples to give (in order):**

```
Example 1 (the walkthrough):
  Input: input_string = "abcabcbb"
  Output: 3
  Explanation: The answer is "abc", with the length of 3.

Example 2 (all repeats):
  Input: input_string = "bbbbb"
  Output: 1
  Explanation: The answer is "b", with the length of 1.

Example 3 (substring in middle / fast solvers):
  Input: input_string = "pwwkew"
  Output: 3
  Explanation: The answer is "wke", with the length of 3. Note that the answer must be a substring, "pwke" is a subsequence and not a substring.
```

---

## Clarifying Questions — What a Good Candidate Asks

| Question | Why it matters |
|---|---|
| What is the character set? Are there spaces, special characters, or numbers? | A larger character set (ASCII vs lowercase English) affects size optimization, though the hash map approach works universally. |
| Is the input string case-sensitive? | Confirms whether 'a' and 'A' are duplicates; LeetCode assumes they are distinct. |
| What should we return if the input string is empty? | Establishes the baseline edge case (0). |
| What are the length constraints of the string? | Helps evaluate if brute-force approaches will hit time limits on large inputs (e.g., length up to 50,000). |

**Red flag:** Starts writing a nested loop brute-force solution without checking if a linear time complexity approach is expected, or tries to solve it using a set without keeping track of indices.

---

## Expected Solution Approach — Sliding Window with Hash Map

**The key insight:** We can find the longest substring in a single pass using a sliding window. By keeping track of the last seen index of each character, we can instantly jump the left boundary of our window to the right of the duplicate character when a collision occurs, rather than sliding the boundary incrementally.

### What we build
A dictionary `last_seen_index` that maps each character to its most recently encountered index in the string. We maintain two pointers, `left_pointer` and `right_pointer`, representing the boundaries of our current sliding window. As `right_pointer` moves from left to right, we update the map and expand the window. If the current character is already in `last_seen_index` and its recorded index is within the current window, we update `left_pointer` to `last_seen_index[current_char] + 1` to exclude the duplicate. The final answer is the maximum difference of `right_pointer - left_pointer + 1` observed during the traversal.

### Pseudocode

```
length_of_longest_substring(input_string):
  last_seen_index = empty map
  longest_length = 0
  left_pointer = 0
  
  for right_pointer from 0 to length of input_string - 1:
    current_char = input_string[right_pointer]
    if current_char in last_seen_index:
      # Jump left_pointer to exclude the previous occurrence of current_char,
      # but only if the previous occurrence is inside the current window.
      left_pointer = max(left_pointer, last_seen_index[current_char] + 1)
      
    last_seen_index[current_char] = right_pointer
    longest_length = max(longest_length, right_pointer - left_pointer + 1)
    
  return longest_length
```

### Reference implementation (Python)

```python
def length_of_longest_substring(input_string: str) -> int:
    last_seen_index: dict[str, int] = {}
    longest_length = 0
    left_pointer = 0

    for right_pointer in range(len(input_string)):
        current_char = input_string[right_pointer]
        if current_char in last_seen_index:
            left_pointer = max(left_pointer, last_seen_index[current_char] + 1)

        last_seen_index[current_char] = right_pointer
        longest_length = max(longest_length, right_pointer - left_pointer + 1)

    return longest_length
```

### Complexity

| Variant | Time Complexity | Space Complexity |
|---|---|---|
| Sliding Window with Hash Map | O(N) where N is the length of `input_string` | O(min(N, M)) where M is the size of the alphabet |

---

## Timing Guide

| Phase | Expected time | What you're watching |
|---|---|---|
| Clarification | 3–5 min | Do they verify the character set and case sensitivity? |
| Approach discussion | 5–8 min | Do they explain why brute force is O(N^2) or O(N^3) and propose a sliding window? |
| Coding | 10–15 min | Do they correctly implement the condition to prevent moving `left_pointer` backward? |
| Tracing | 3–5 min | Do they trace an example where a duplicate character occurs outside the current window? |
| Edge cases + complexity | 3–5 min | Empty string, single character, all repeating characters. |

**Total target:** ~30–40 min

---

## Escalating Hints (give one at a time, wait 2–3 min between)

1. "If you were to search for the longest substring without repeats by checking every possible substring, how would that work and what would be its time complexity?"
    - **Expected:** The candidate describes checking all O(N^2) substrings and verifying uniqueness in O(N), leading to O(N^3) time complexity.
2. "How can we avoid re-evaluating the entire substring when we move the end of the substring to the right?"
    - **Expected:** The candidate suggests a sliding window where we expand the right boundary and adjust the left boundary based on duplicate characters.
3. "When we find a duplicate character, how do we know where the left boundary of our window should move to?"
    - **Expected:** We move the left pointer to one index past the previous occurrence of that duplicate character to make the window valid again.
4. "How can we find the last seen index of a character in constant time?"
    - **Expected:** We can store the mapping from character to its most recent index in a hash map.
5. "If we find a duplicate character, but its last seen index is smaller than the current left boundary of our sliding window, what does that mean?"
    - **Expected:** It means the duplicate lies outside the current sliding window, so we should ignore it and not move the left pointer backward.

---

## Green / Yellow / Red Flags

### Green (strong signal)
- Proposes sliding window with O(1) index lookups right away.
- Proactively handles the edge case of not moving the left pointer backward (using `max(left_pointer, ...)`).
- Traces the window boundaries and map states accurately.
- Uses descriptive variable names like `left_pointer`, `right_pointer`, `last_seen_index`.

### Yellow (salvageable)
- Proposes a sliding window using a set where they remove characters one by one (this is functional but runs in O(N) worst-case amortized, but they might write it with a while loop to delete characters from the left one-by-one).
- Forgets to handle the case where a duplicate character's index is smaller than `left_pointer`, leading to incorrect results on inputs like `"abba"`.
- Uses abbreviated names like `i`, `j`, `l`, `r`, `seen`, but corrects them when asked.

### Red (significant gap)
- Unable to optimize beyond the brute-force O(N^2) or O(N^3) solutions.
- Cannot explain the difference between a substring and a subsequence.
- Repeatedly fails to update the hash map or pointer coordinates correctly during dry-runs.

---

## Common Bugs to Watch For

- **Moving the left pointer backward:** In `"abba"`, when the second `'a'` is processed at index 3, `last_seen_index['a']` is 0. If the candidate sets `left_pointer = last_seen_index['a'] + 1` blindly, `left_pointer` goes from index 2 back to index 1, producing an incorrect length.
- **Off-by-one in length calculation:** Calculating the substring length as `right_pointer - left_pointer` instead of `right_pointer - left_pointer + 1`.
- **Forgetting to update the last seen index:** Forgetting to overwrite `last_seen_index[current_char] = right_pointer` on every iteration.

---

## Follow-Up Questions

---

### Easy — "How would you modify the solution if characters are treated case-insensitively?"

> In the base solution, characters are looked up directly by their raw representation. That works because case-sensitive characters are distinct by default in Python. Here, that assumption breaks because 'a' and 'A' should be treated as duplicates. So instead, we convert the character to lowercase before any lookup or storage. The rest of the sliding window logic stays the same.

```diff
 def length_of_longest_substring(input_string: str) -> int:
     last_seen_index: dict[str, int] = {}
     longest_length = 0
     left_pointer = 0
 
     for right_pointer in range(len(input_string)):
-        current_char = input_string[right_pointer]
+        current_char = input_string[right_pointer].lower()
         if current_char in last_seen_index:
             left_pointer = max(left_pointer, last_seen_index[current_char] + 1)
```

**Complexity:** Time complexity remains O(N). Space complexity remains O(min(N, alphabet_size)).  
**What to watch candidates get wrong:** Converting the entire string using `input_string.lower()` before processing, which uses O(N) extra space, rather than converting character-by-character.

---

### Medium — "How would you solve the problem if we are allowed at most `max_repeats` duplicates of any character?"

> In the base solution, we track the last seen index of each character to jump the left pointer. That works because at most 1 occurrence of each character is allowed. Here, that assumption breaks because a character can appear up to max_repeats times before the window becomes invalid. So instead, we track the frequency counts of characters in the current window using a map, and increment the left pointer until the frequency of the current character is at most max_repeats. The sliding window skeleton stays the same.

```diff
-def length_of_longest_substring(input_string: str) -> int:
-    last_seen_index: dict[str, int] = {}
+def length_of_longest_substring_with_duplicates(input_string: str, max_repeats: int) -> int:
+    char_frequency: dict[str, int] = {}
     longest_length = 0
     left_pointer = 0
 
     for right_pointer in range(len(input_string)):
         current_char = input_string[right_pointer]
-        if current_char in last_seen_index:
-            left_pointer = max(left_pointer, last_seen_index[current_char] + 1)
-
-        last_seen_index[current_char] = right_pointer
+        char_frequency[current_char] = char_frequency.get(current_char, 0) + 1
+        while char_frequency[current_char] > max_repeats:
+            left_char = input_string[left_pointer]
+            char_frequency[left_char] -= 1
+            left_pointer += 1
         longest_length = max(longest_length, right_pointer - left_pointer + 1)
```

**Complexity:** Time complexity is O(N) because the left pointer and right pointer each traverse the string at most once. Space complexity is O(min(N, alphabet_size)) to store character frequencies.  
**What to watch candidates get wrong:** Trying to keep lists of indices for each character and doing index arithmetic instead of using a simple frequency-shrinking sliding window.

---

### Hard — "What if the input is a continuous stream of characters and we want to find the length of the longest substring without repeating characters in the last `window_capacity` elements?"

> In the base solution, we assume the entire input is accessible in memory. That works because the string is provided as a static array. Here, that assumption breaks because the input is a stream and we can only store the last window_capacity characters. So instead, we maintain a queue representing the active window of size window_capacity alongside a set of characters currently in the queue, popping from the queue's front and removing from the set whenever the current character is a duplicate or the window exceeds capacity. The maximum length tracker stays the same.

```diff
+from collections import deque
+
+class StreamLongestSubstring:
+    def __init__(self, window_capacity: int) -> None:
+        self._window_capacity = window_capacity
+        self._current_window = deque()
+        self._seen_characters = set()
+        self._longest_length = 0
+
+    def process_character(self, current_char: str) -> int:
+        while current_char in self._seen_characters:
+            removed_char = self._current_window.popleft()
+            self._seen_characters.remove(removed_char)
+        
+        self._current_window.append(current_char)
+        self._seen_characters.add(current_char)
+        
+        if len(self._current_window) > self._window_capacity:
+            removed_char = self._current_window.popleft()
+            self._seen_characters.remove(removed_char)
+            
+        self._longest_length = max(self._longest_length, len(self._current_window))
+        return self._longest_length
```

**Complexity:** Time complexity is O(1) amortized per processed character. Space complexity is O(window_capacity) to store the stream buffer and set.  
**What to watch candidates get wrong:** Forgetting to remove popped characters from the set, or storing all stream history in memory, which violates the streaming constraints.

---

## Scoring Rubric

<details>
<summary><strong>Approach clarity</strong> — Did they reach the right algorithm independently?</summary>

| Score | Signal |
|---|---|
| 5 | Immediately identifies sliding window with O(1) index lookups; explains the intuition clearly before writing code. |
| 4 | Proposes sliding window but initially with a set and one-by-one removals; quickly sees how to optimize using a hash map. |
| 3 | Needs conceptual suggestions about "avoiding redundant work" or "storing previous indices" before proposing sliding window. |
| 2 | Needs hint 3 or 4 to understand sliding window structure and dictionary mapping. |
| 1 | Unable to design a sliding window approach even after all hints. |

</details>

<details>
<summary><strong>Correctness</strong> — Does the solution handle all cases including edge cases?</summary>

| Score | Signal |
|---|---|
| 5 | Code is correct on the first attempt, including empty string, single character, and the "abba" duplicate index edge case. |
| 4 | Code has minor logic bugs but the candidate catches and corrects them during a manual trace. |
| 3 | Core logic is correct, but requires interviewer prompt to find and fix the backward-moving left pointer bug. |
| 2 | Code misses multiple boundary cases and struggles with sliding window indexing. |
| 1 | The solution remains fundamentally broken and doesn't pass basic test cases. |

</details>

<details>
<summary><strong>Code quality</strong> — Clean helper methods, correct pointer order, full variable names?</summary>

| Score | Signal |
|---|---|
| 5 | Writes clean, readable code with descriptive variable names (e.g., `left_pointer`, `right_pointer`, `last_seen_index`). |
| 4 | Uses minor abbreviations (e.g., `left`, `right`, `seen`) but code remains legible. |
| 3 | Code is somewhat cluttered with multiple unnecessary variables, but logic is readable. |
| 2 | Hard to follow; variable names are extremely cryptic (e.g., `i`, `j`, `s`, `l`, `r`). |
| 1 | Total spaghetti; names are misleading, and spacing/structure is very poor. |

</details>

<details>
<summary><strong>Edge case handling</strong> — Empty input, single character, all repeating, duplicates outside window?</summary>

| Score | Signal |
|---|---|
| 5 | Proactively identifies and implements correctness checks for empty string, case sensitivity, and duplicate outside window. |
| 4 | Accounts for edge cases during walk-through and adds necessary bounds. |
| 3 | Needs prompts like "What happens if the string is empty?" or "What happens on 'abba'?" to fix edge cases. |
| 2 | Fails to handle empty string or duplicates outside the window, even when prompted. |
| 1 | Ignores boundary conditions completely. |

</details>

<details>
<summary><strong>Complexity analysis</strong> — Correct answer with explanation of why?</summary>

| Score | Signal |
|---|---|
| 5 | Correctly identifies O(N) time and O(min(N, alphabet_size)) space, explaining the bounding limits of the hash map. |
| 4 | Correctly identifies time/space but cannot explain the O(min(N, alphabet_size)) space bound. |
| 3 | Correctly identifies time as O(N) but thinks space is O(N) without referring to the limited character alphabet. |
| 2 | Incorrectly identifies time complexity as O(N^2) or thinks space is O(1) under all conditions. |
| 1 | Cannot evaluate complexity or gives random guesses. |

</details>

### Hints penalty

Subtract from the overall impression (not from individual scores):  
0 hints used → strongest signal · 1–2 → normal · 3–4 → yellow · 5 → red

---

## Feedback Template

```
Problem: Longest Substring Without Repeating Characters (Sliding Window)

Approach clarity:     /5
Correctness:          /5
Code quality:         /5
Edge case handling:   /5
Complexity analysis:  /5

Hints used: ___ / 5

Overall: STRONG HIRE / HIRE / NO HIRE

Notes:
```

---

## Candidate Templates

Runnable starter files — share one with the candidate at the start of the session:

- [`longest-substring-without-repeating-characters-template.py`](longest-substring-without-repeating-characters-template.py) — Python

Each has method stubs and a built-in test suite grouped into 3 levels. Run the file at any point to check which levels pass.
