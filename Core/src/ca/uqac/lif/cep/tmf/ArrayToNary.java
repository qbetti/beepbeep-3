package ca.uqac.lif.cep.tmf;

import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.ProcessorException;
import ca.uqac.lif.cep.UniformProcessor;

import java.lang.reflect.Array;

public class ArrayToNary extends UniformProcessor {


    public ArrayToNary(int out_arity) {
        super(1, out_arity);
    }

    @Override
    protected boolean compute(Object[] inputs, Object[] outputs) throws ProcessorException {
        Object o = inputs[0];
        if(!o.getClass().isArray())
            return false;

        Class classOfArray = o.getClass().getComponentType();

        if(classOfArray.isPrimitive()) {
            int length = Array.getLength(o);
            if(length != outputs.length)
                return false;

            for(int i = 0; i < length; i++) {
                Object obj = Array.get(o, i);
                outputs[i] = obj;
            }
        }
        else {
            Object[] arrayInputs = (Object[]) inputs[0];
            if(arrayInputs.length != outputs.length)
                return false;

            for(int i = 0; i < arrayInputs.length; i++)
                outputs[i] = arrayInputs[i];
        }

        return true;
    }


    @Override
    public Processor duplicate() {
        return new ArrayToNary(getOutputArity());
    }
}
