package dev.alexkzk.algo.neetcode150.binary_search;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class TimeBasedKeyValueStoreTest {
    @Test
    void example1() {
        TimeBasedKeyValueStore store = new TimeBasedKeyValueStore();
        store.set("foo", "bar", 1);
        assertEquals("bar", store.get("foo", 1));
        assertEquals("bar", store.get("foo", 3));
        store.set("foo", "bar2", 4);
        assertEquals("bar2", store.get("foo", 4));
        assertEquals("bar2", store.get("foo", 5));
    }

    @Test
    void example2() {
        TimeBasedKeyValueStore store = new TimeBasedKeyValueStore();
        store.set("love", "high", 10);
        store.set("love", "low", 20);
        assertEquals("", store.get("love", 5));
        assertEquals("high", store.get("love", 10));
        assertEquals("high", store.get("love", 15));
        assertEquals("low", store.get("love", 20));
    }

    @Test
    void edgeCase() {
        TimeBasedKeyValueStore store = new TimeBasedKeyValueStore();
        assertEquals("", store.get("missing", 1));
    }
}
