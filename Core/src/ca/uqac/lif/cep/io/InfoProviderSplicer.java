package ca.uqac.lif.cep.io;

import ca.uqac.lif.cep.Context;
import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.Pullable;
import ca.uqac.lif.cep.tmf.Source;

import java.util.Queue;

/**
 * Joins multiple traces as a single one. The splicer is given
 * multiple input traces. It feeds the events of the first one
 * until it does not yield any new event. Then it starts feeding
 * events of the second one, and so on.
 * <p>
 * Currently, the splicer is implemented as a 0:n processor, and
 * the processors it is given must have an input arity of 0.
 *
 * @author Sylvain HallÃ©
 *
 */
public class InfoProviderSplicer extends Source
{


    /**
     * The list of processors to splice together
     */
    protected SourceInfoProvider[] m_processors;

    /**
     * The index of the processor we are currently reading from
     */
    protected int m_processorIndex;

    /**
     * The pullables of the processor we are currently reading from
     */
    protected Pullable[] m_pullables;

    /**
     *
     * @param processors
     */
    public InfoProviderSplicer(SourceInfoProvider... processors)
    {
        super(processors[0].getOutputArity() + 1);
        m_processors = processors;
        m_processorIndex = 0;
        m_pullables = new Pullable[processors[0].getOutputArity()];
        setPullablesTo(0);
    }

    protected void setPullablesTo(int index)
    {
        Processor p = m_processors[index];
        for (int i = 0; i < m_pullables.length; i++)
        {
            m_pullables[i] = p.getPullableOutput(i);
        }
    }

    @Override
    public void reset()
    {
        m_processorIndex = 0;
        for (Processor p : m_processors)
        {
            p.reset();
        }
        setPullablesTo(0);
    }

    @Override
    public void setContext(Context context)
    {
        super.setContext(context);
        for (Processor p : m_processors)
        {
            p.setContext(context);
        }
    }

    @Override
    public void setContext(String key, Object value)
    {
        super.setContext(key, value);
        for (Processor p : m_processors)
        {
            p.setContext(key, value);
        }
    }


    @Override
    @SuppressWarnings("squid:S1168")
    protected boolean compute(Object[] inputs, Queue<Object[]> outputs)
    {
        if (m_processorIndex >= m_processors.length)
        {
            return false;
        }
        Object[] out = new Object[m_pullables.length];

        for (int index = m_processorIndex; index < m_processors.length; index++)
        {
            boolean has_null = false;
            for (int i = 0; i < m_pullables.length; i++)
            {
                boolean status = m_pullables[i].hasNext();
                if (status == false)
                {
                    // No use in trying further
                    has_null = true;
                    break;
                }
                Object o = m_pullables[i].pull();
                if (o == null)
                {
                    has_null = true;
                    break;
                }
                out[i] = o;
            }
            if (!has_null)
            {
                SourceInfo sourceInfo = new SourceInfo();
                sourceInfo.setId(m_processorIndex);
                sourceInfo.setSourceName(m_processors[m_processorIndex].getSourceName());

                Object[] extendedOut = new Object[this.getOutputArity()];
                for(int i = 0; i < out.length; i++)
                    extendedOut[i] = out[i];

                extendedOut[out.length] = sourceInfo;

                outputs.add(extendedOut);
                return true;
            }
            m_processorIndex++;
            if (m_processorIndex < m_processors.length)
            {
                setPullablesTo(m_processorIndex);
            }
        }
        return false;
    }

    @Override
    public InfoProviderSplicer duplicate()
    {
        return new InfoProviderSplicer(m_processors);
    }
}


