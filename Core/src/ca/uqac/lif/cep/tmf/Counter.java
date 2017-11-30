package ca.uqac.lif.cep.tmf;

import ca.uqac.lif.cep.ProcessorException;
import ca.uqac.lif.cep.UniformProcessor;

public class Counter extends UniformProcessor {

    /**
     * The variable that will keep the current value of the counter
     */
    protected long m_counterValue;


    public Counter()
    {
        super(1, 1);
        m_counterValue = 0;
    }


    @Override
    protected boolean compute(Object[] inputs, Object[] outputs) throws ProcessorException {
        m_counterValue++;
        outputs[0] = m_counterValue;

        return true;
    }


    @Override
    public Counter duplicate()
    {
        return new Counter();
    }


    @Override
    public void reset()
    {
        m_counterValue = 0;
    }
}
