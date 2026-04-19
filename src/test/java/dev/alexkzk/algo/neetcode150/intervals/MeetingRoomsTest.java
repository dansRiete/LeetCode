package dev.alexkzk.algo.neetcode150.intervals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class MeetingRoomsTest {
    private final MeetingRooms sol = new MeetingRooms();

    @Test
    void example1() {
        assertTrue(sol.canAttendMeetings(new int[][]{{0,30},{35,50},{60,90}}));
    }

    @Test
    void example2() {
        assertFalse(sol.canAttendMeetings(new int[][]{{0,30},{5,10},{15,20}}));
    }

    @Test
    void edgeCase() {
        assertTrue(sol.canAttendMeetings(new int[][]{}));
    }
}
