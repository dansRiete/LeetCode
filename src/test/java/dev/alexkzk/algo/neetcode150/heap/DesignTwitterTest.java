package dev.alexkzk.algo.neetcode150.heap;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class DesignTwitterTest {
    @Test
    void example1() {
        DesignTwitter twitter = new DesignTwitter();
        twitter.postTweet(1, 5);
        assertEquals(List.of(5), twitter.getNewsFeed(1));
        twitter.follow(1, 2);
        twitter.postTweet(2, 6);
        assertEquals(List.of(6, 5), twitter.getNewsFeed(1));
        twitter.unfollow(1, 2);
        assertEquals(List.of(5), twitter.getNewsFeed(1));
    }

    @Test
    void example2() {
        DesignTwitter twitter = new DesignTwitter();
        twitter.postTweet(1, 1);
        twitter.follow(2, 1);
        assertEquals(List.of(1), twitter.getNewsFeed(2));
    }

    @Test
    void edgeCase() {
        DesignTwitter twitter = new DesignTwitter();
        assertEquals(List.of(), twitter.getNewsFeed(1));
    }
}
