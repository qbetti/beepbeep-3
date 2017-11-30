package ca.uqac.lif.cep.tmf;

import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.ProcessorException;
import ca.uqac.lif.cep.SingleProcessor;

import java.util.Queue;

public class ToString extends SingleProcessor {


    public ToString() {
        super(1, 1);
    }

    @Override
    protected boolean compute(Object[] objects, Queue<Object[]> queue) throws ProcessorException {
        if(objects.length > 0)
            queue.add(new Object[]{objects[0].toString()});
        return true;
    }

    @Override
    public Processor duplicate() {
        return null;
    }
}
