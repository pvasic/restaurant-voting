package ru.pvasic.restaurantvoting.util;

import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
@Getter
public class GenUtil {
    public static int genInt() {
        return (int) System.currentTimeMillis();
    }
}
