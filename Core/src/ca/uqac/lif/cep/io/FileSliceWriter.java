package ca.uqac.lif.cep.io;

import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.ProcessorException;
import ca.uqac.lif.cep.tmf.Sink;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Queue;

public class FileSliceWriter extends Sink {


    private int m_counter;
    private String m_fileBasename;
    private String m_fileExtension;
    private String m_namePartSeparator;

    public FileSliceWriter(String fileBasename, String fileExtension, String namePartSeparator) {
        super();
        m_counter = 0;
        m_fileBasename = fileBasename;
        m_fileExtension = fileExtension;
        m_namePartSeparator = namePartSeparator;
    }

    @Override
    protected boolean compute(Object[] inputs, Queue<Object[]> output) throws ProcessorException {
        if (inputs != null && inputs[0] != null) {
            try {
                FileWriter writer = new FileWriter(buildFileName());
                writer.write(inputs[0].toString());
                writer.close();
                m_counter++;
                return true;

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    private String buildFileName() {
        return m_fileBasename + m_namePartSeparator + m_counter + m_fileExtension;
    }

    @Override
    public Processor duplicate() {
        return null;
    }
}
