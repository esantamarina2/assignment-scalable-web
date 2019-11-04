package com.esantamarina.assignmentscalableweb.service;

import com.esantamarina.assignmentscalableweb.domain.DiffUtilities;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DiffUtilitiesTest {

    /**
      Scenario: Compare two similar arrays
      Expected: There is no difference
   */
    @Test
    public void compares_equal_parts() {
        byte[] left = new byte[]{0, 1};
        byte[] right = new byte[]{0, 1};

        int diff = DiffUtilities.countDifferentBytes(left, right);

        assertThat(diff, is(0));
    }

    /**
      Scenario: Compare two arrays with same length but different bytes
      Expected: There are 2 bytes of difference
    */
    @Test
    public void compares_with_different_content() {
        byte[] left = new byte[]{0, 1, 0, 1, 1, 1};
        byte[] right = new byte[]{0, 1, 0, 0, 1, 0};

        int diff = DiffUtilities.countDifferentBytes(left, right);

        assertThat(diff, is(2));
    }

    /**
     Scenario: Compare two arrays with left part is longer that right
     Expected: There are 3 bytes of difference
   */
    @Test
    public void compares_where_left_is_longer() {
        byte[] left = new byte[]{0, 1, 0, 1, 1};
        byte[] right = new byte[]{0, 1};

        int diff = DiffUtilities.countDifferentBytes(left, right);

        assertThat(diff, is(3));
    }

    /**
      Scenario: Compare two arrays with right part is longer that left
      Expected: There are 2 bytes of difference
   */
    @Test
    public void compares_where_right_is_longer() {
        byte[] left = new byte[]{0, 1, 0};
        byte[] right = new byte[]{0, 1, 1, 1};

        int diff = DiffUtilities.countDifferentBytes(left, right);

        assertThat(diff, is(2));
    }

    /**
      Scenario: Compare with an empty part
      Expected: Difference is the length of not empty part
   */
    @Test
    public void compares_where_one_is_empty() {
        byte[] left = new byte[]{};
        byte[] right = new byte[]{0, 1, 1, 1};

        int diff = DiffUtilities.countDifferentBytes(left, right);

        assertThat(diff, is(4));
    }

    /**
      Scenario: Compare two empty arrays
      Expected: There is no difference
   */
    @Test
    public void it_compares_two_empty_arrays() {
        byte[] left = new byte[]{};
        byte[] right = new byte[]{};

        int diff = DiffUtilities.countDifferentBytes(left, right);

        assertThat(diff, is(0));
    }
}