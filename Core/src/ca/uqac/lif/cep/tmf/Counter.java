package ca.uqac.lif.cep.tmf;

import ca.uqac.lif.cep.ProcessorException;
import ca.uqac.lif.cep.UniformProcessor;

/**
 * A simple counter that outputs the number of input front it has processed so far.
 */
public class Counter extends UniformProcessor {

    /**
     * The variable that will keep the current value of the counter
     */
    protected long m_counterValue;

    /**
     * Constructs a simple counter
     */
    public Counter()
    {
        super(1, 1);
        m_counterValue = 0;
    }

    @Override
    protected boolean compute(Object[] inputs, Object[] outputs) throws ProcessorException
    {
        outputs[0] = ++m_counterValue;
        return true;
    }

    @Override
    public void reset()
    {
        m_counterValue = 0;
    }

    @Override
    public Counter duplicate()
    {
        return new Counter();
    }
}
