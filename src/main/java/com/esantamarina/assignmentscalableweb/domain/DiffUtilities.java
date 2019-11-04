package com.esantamarina.assignmentscalableweb.domain;

import javax.validation.constraints.NotNull;
import java.util.Arrays;

public class DiffUtilities {

    public static final String LEFT_SIDE = "Left";
    public static final String RIGHT_SIDE = "Right";

    /**
       Counts how many bytes are different given 2 byte arrays.
       Validate that left and right are not null
     */
    public static int countDifferentBytes(@NotNull byte[] left, @NotNull byte[] right) {
        if (Arrays.equals(left, right)) {
            return 0;
        }
        int shorter = left.length < right.length ? left.length : right.length;
        int longer = left.length > right.length ? left.length : right.length;

        int diffs = 0;
        for (int i = 0; i < shorter; i++) {
            if (left[i] != right[i]) {
                diffs++;
            }
        }
        return diffs + (longer - shorter);
    }
}
