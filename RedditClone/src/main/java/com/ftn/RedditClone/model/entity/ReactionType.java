package com.ftn.RedditClone.model.entity;

import com.ftn.RedditClone.exceptions.SpringRedditException;

import java.util.Arrays;

public enum ReactionType {
    UPVOTE (1),
    DOWNVOTE(-1),
    ;
    private int direction;

    ReactionType(int direction) {
    }

    public static ReactionType lookup(Integer direction) {
        return Arrays.stream(ReactionType.values())
                .filter(value -> value.getDirection().equals(direction))
                .findAny()
                .orElseThrow(() -> new SpringRedditException("Vote not found"));
    }

    public Integer getDirection() {
        return direction;
    }
}
