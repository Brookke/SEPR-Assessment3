package org.teamfarce.mirch;

import java.util.function.IntUnaryOperator;
import java.util.function.ToIntFunction;
import java.util.function.IntFunction;
import java.util.Optional;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class WeightedSelection {
    /**
     * Construct the class with a random number function.
     * <p>
     * This function should provide out a random value from 0 to x - 1. This will allow this
     * object to make random decisions and thus a random selection.
     * </p>
     *
     * @param randomFunc The random number provider.
     */
    public WeightedSelection(IntUnaryOperator randomFunc) {
        randomGenerator = randomFunc;
    }

    /**
     * Construct the class with a random number generator.
     * <p>
     * Construct the selector with the specified random object.
     * </p>
     *
     * @param random The random object.
     */
    public WeightedSelection(Random random) {
        this(max -> random.nextInt(max));
    }

    // This is common across the `selectWeightedObject`s. Gets the selection index with the given
    // collection which can use `weightFunction` to extract the weight of the given object.
    private <T> int getSelection(Collection<T> objects, ToIntFunction<T> weightFunction) {
        int selectionWeightSum = objects.stream().mapToInt(weightFunction).sum();
        return randomGenerator.applyAsInt(selectionWeightSum);
    }

    /**
     * Selects an object from the list based on its weight.
     * <p>
     * This method determines the weight given to a specific object by using the provided weight
     * function. Once a specific object has been selected, it is extracted by using the extractor;
     * this is used to provided some other way to get the object.
     * </p>
     *
     * @param objects The list of objects to consider
     * @param weightFunction The function which supplies the weight of an object
     * @param extractor The extraction method
     */
    public <T> Optional<T> selectWeightedObject(
        List<T> objects, ToIntFunction<T> weightFunction, IntFunction<T> extractor
    ) {
        if (objects.size() == 0) {
            return Optional.empty();
        }

        int selection = getSelection(objects, weightFunction);

        for (int i = 0; i < objects.size(); ++i) {
            selection -= weightFunction.applyAsInt(objects.get(i));
            if (selection < 0) {
                return Optional.of(extractor.apply(i));
            }
        }

        return Optional.empty();
    }

    /**
     * Selects an object from the list based on its weight.
     * <p>
     * This method determines the weight given to a specific object by using the provided weight
     * function.
     * </p>
     *
     * @param objects The list of objects to consider
     * @param weightFunction The function which supplies the weight of an object
     */
    public <T> Optional<T> selectWeightedObject(
        Collection<T> objects,
        ToIntFunction<T> weightFunction
    ) {
        if (objects.size() == 0) {
            return Optional.empty();
        }

        int selection = getSelection(objects, weightFunction);

        for (T object: objects) {
            selection -= weightFunction.applyAsInt(object);
            if (selection < 0) {
                return Optional.of(object);
            }
        }

        return Optional.empty();
    }

    private IntUnaryOperator randomGenerator;
}
