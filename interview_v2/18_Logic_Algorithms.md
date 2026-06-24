# Question 1: Four people need to cross a bridge with one flashlight. They take 1, 2, 5, and 10 minutes respectively. The bridge holds 2 at a time. How do they cross in 17 minutes?

## Answer
This is a classic logic and optimization puzzle. The key to minimizing the total time is to ensure that the two slowest people cross together, so their slower times are not counted twice. Additionally, we must use the fastest people to return the flashlight.

Let's refer to the four people by their crossing times: $P_1$, $P_2$, $P_5$, and $P_{10}$.

Here is the step-by-step strategy to achieve the 17-minute crossing:

1. **$P_1$ and $P_2$ cross the bridge:**
   - Time taken: $\max(1, 2) = 2$ minutes.
   - Total time elapsed: $2$ minutes.
   - Now on the other side: $P_1$, $P_2$.

2. **$P_1$ returns with the flashlight:**
   - Time taken: $1$ minute.
   - Total time elapsed: $2 + 1 = 3$ minutes.
   - Now on the other side: $P_2$.

3. **$P_5$ and $P_{10}$ cross the bridge:**
   - Time taken: $\max(5, 10) = 10$ minutes.
   - Total time elapsed: $3 + 10 = 13$ minutes.
   - Now on the other side: $P_2$, $P_5$, $P_{10}$.
   - *Note: This is the crucial step. By sending the two slowest people together, we only pay the 10-minute penalty once.*

4. **$P_2$ returns with the flashlight:**
   - Time taken: $2$ minutes.
   - Total time elapsed: $13 + 2 = 15$ minutes.
   - Now on the other side: $P_5$, $P_{10}$.

5. **$P_1$ and $P_2$ cross the bridge together one last time:**
   - Time taken: $\max(1, 2) = 2$ minutes.
   - Total time elapsed: $15 + 2 = 17$ minutes.

**Result:**
All four people have crossed the bridge, and the total time taken is exactly **17 minutes**.
