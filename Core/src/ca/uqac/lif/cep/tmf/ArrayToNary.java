package ca.uqac.lif.cep.tmf;

import ca.uqac.lif.cep.ProcessorException;
import ca.uqac.lif.cep.UniformProcessor;

import java.lang.reflect.Array;

/**
 * Takes an array of size n as 0th input and produces an n-sized output front
 * with its elements.I.e., it sends the elements of the array to n different
 * outputs, in the order of the array indexes (element at position i in the
 * input array will be send to the i-th output).
 *
 * @author Quentin Betti
 */
public class ArrayToNary extends UniformProcessor {

    /**
     * Constructs an {@link ArrayToNary} processor that takes arrays of
     * the given size as input.
     * @param arraySize the size of the input arrays
     */
    public ArrayToNary(int arraySize) {
        super(1, arraySize);
    }

    @Override
    protected boolean compute(Object[] inputs, Object[] outputs) throws ProcessorException {
        Object o = inputs[0];
        if(!o.getClass().isArray())
            throw new ProcessorException("Index 0 of input front must be an array");

        int length = Array.getLength(o);
        if(length != outputs.length)
        {
            throw new ProcessorException("Length of input array must equal the output arity");
        }
        for(int i = 0; i < length; i++)
        {
            outputs[i] = Array.get(o, i);
        }
        return true;
    }

    @Override
    public ArrayToNary duplicate() {
        return new ArrayToNary(getOutputArity());
    }
}
