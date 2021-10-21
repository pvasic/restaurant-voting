package ru.pvasic.restaurantvoting.util;

import lombok.experimental.UtilityClass;

import java.time.LocalTime;

@UtilityClass
public class VoteUtil {
    public static final LocalTime DEFAULT_VOTE_TIME = LocalTime.of(11, 00);
}