package ca.uqac.lif.cep.io;

import ca.uqac.lif.cep.Pullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Queue;

public class ExtendedReadFile extends ReadLines {

    /**
     * The file to read lines from
     */
    protected File m_file;

    /**
     * The line counter in the file
     */
    protected int m_lineCounter;

    /**
     * Creates a new file reader from an input file
     * @param file The input file to read from
     */
    @SuppressWarnings("unchecked")
    public ExtendedReadFile(File file) throws FileNotFoundException
    {
        super(new FileInputStream(file));
        m_outputArity = 3;
        m_outputPullables = new Pullable[m_outputArity];
        m_outputQueues = new Queue[m_outputArity];
        for (int i = 0; i < m_outputArity; i++)
        {
            m_outputQueues[i] = new ArrayDeque<Object>();
        }
        m_file = file;
        m_lineCounter = 0;
    }

    @Override
    protected boolean compute(Object[] inputs, Queue<Object[]> outputs) {
        if (m_scanner.hasNextLine()) {
            m_lineCounter++;
            String line = m_scanner.nextLine();
            if (m_trim) {
                line = line.trim();
            }
            if (m_addCrlf) {
                line += CRLF;
            }
            outputs.add(new Object[]{line, m_lineCounter, m_file});
            return true;
        }
        return false;
    }

    @Override
    public ExtendedReadFile duplicate() {
        try {
            return new ExtendedReadFile(m_file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
