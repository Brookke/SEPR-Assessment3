package org.teamfarce.mirch;

import java.lang.Math;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.teamfarce.mirch.WeightedSelection;

public class WeightedSelection_Test {
    @Test
    public void test1() {
        // Tests for selection of the middle element where everything has an equal weight.
        WeightedSelection ws = new WeightedSelection(x -> Math.floorDiv(x, 2));
        ArrayList<Integer> objects = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            objects.add(i);
        }
        Optional<Integer> result = ws.selectWeightedObject(objects, x -> 1);
        assertEquals((int)result.get(), 5);
    }

    @Test
    public void test2() {
        // Test for where all of the elements are weighted to zero except for one.
        WeightedSelection ws = new WeightedSelection(x -> Math.floorDiv(x, 2));
        ArrayList<Integer> objects = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            objects.add(i);
        }
        Optional<Integer> result = ws.selectWeightedObject(objects, x -> {
            if (x == 2) {
                return 1;
            } else {
                return 0;
            }
        });
        assertEquals((int)result.get(), 2);
    }

    @Test
    public void test3() {
        // For the array [0, 1, 2, 3] we should get an array of [1, 2, 2, 3, 3, 3] where each
        // element appears "weight" number of times; the weight being themselves.
        ArrayList<Integer> objects = new ArrayList<>();
        ArrayList<Integer> resultModel = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            objects.add(i);
            for (int j = 0; j < i; ++j) {
                resultModel.add(i);
            }
        }

        // Check that we have our expected result.
        for (int i = 0; i < resultModel.size(); ++i) {
            // First the value is finalised so that it may be captured by the lambda. This
            // effectively creates an WeightedSelection object which selects the "i"th element of
            // what of the expanded resultModel is.
            final int k = i;
            WeightedSelection ws = new WeightedSelection(x -> k);

            // Check this subresult.
            Optional<Integer> result = ws.selectWeightedObject(objects, x -> x);
            assertEquals((int)result.get(), (int)resultModel.get(i));
        }
    }

    @Test
    public void test4() {
        // Test that a zero element array returns empty.
        ArrayList<Integer> objects = new ArrayList<>();
        WeightedSelection ws = new WeightedSelection(new Random());
        Optional<Integer> result = ws.selectWeightedObject(objects, x -> x);
        assert(!result.isPresent());
    }

    @Test
    public void test5() {
        // Test that a total weight of 0 returns empty.
        ArrayList<Integer> objects = new ArrayList<>();
        WeightedSelection ws = new WeightedSelection(new Random());
        Optional<Integer> result = ws.selectWeightedObject(objects, x -> 0);
        assert(!result.isPresent());
    }
}
