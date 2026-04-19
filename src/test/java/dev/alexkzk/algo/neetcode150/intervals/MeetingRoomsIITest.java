package dev.alexkzk.algo.neetcode150.intervals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class MeetingRoomsIITest {
    private final MeetingRoomsII sol = new MeetingRoomsII();

    @Test
    void example1() {
        assertEquals(2, sol.minMeetingRooms(new int[][]{{0,30},{5,10},{15,20}}));
    }

    @Test
    void example2() {
        assertEquals(1, sol.minMeetingRooms(new int[][]{{7,10},{2,4}}));
    }

    @Test
    void edgeCase() {
        assertEquals(0, sol.minMeetingRooms(new int[][]{}));
    }
}
