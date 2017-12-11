package ca.uqac.lif.cep.tmf;

import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.Pullable;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * A {@link Splice} processor which outputs the index of
 * the current source in addition to the source's outputs.
 * I.e., if the source processors have <i>n</i> outputs, the
 * {@link ExtendedSplice} outputs them and the index of the
 * source on <i>n+1</i>th output.
 */
public class ExtendedSplice extends Splice
{
    /**
     * Creates a {@link ExtendedSplice} with
     * it source processors.
     * @param processors The source processors
     */
    @SuppressWarnings("unchecked")
    public ExtendedSplice(Processor... processors)
    {
        super(processors);
        m_outputArity = processors[0].getOutputArity() + 1;
        m_outputPullables = new Pullable[m_outputArity];
        m_outputQueues = new Queue[m_outputArity];
        for (int i = 0; i < m_outputArity; i++)
        {
            m_outputQueues[i] = new ArrayDeque<Object>();
        }
    }

    @Override
    protected boolean compute(Object[] inputs, Queue<Object[]> outputs)
    {
        Queue<Object[]> parentOutQueue = new ArrayDeque<Object[]>();
        if(super.compute(inputs, parentOutQueue))
        {
            Object[] parentOut = parentOutQueue.remove();
            Object[] out = new Object[m_outputArity];
            System.arraycopy(parentOut, 0, out, 0, parentOut.length);
            out[out.length-1] = m_processorIndex;
            outputs.add(out);
            return true;
        }
        return false;
    }
}
