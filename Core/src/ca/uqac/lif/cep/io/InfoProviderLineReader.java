package ca.uqac.lif.cep.io;

import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.ProcessorException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Queue;
import java.util.Scanner;

public class InfoProviderLineReader extends SourceInfoProvider {

    private File m_file;
    private InputStream m_inStream;
    private Scanner m_scanner;
    private long m_lineNb;

    public InfoProviderLineReader(File file) throws FileNotFoundException {
        this(new FileInputStream(file));
        m_file = file;
    }

    private InfoProviderLineReader(InputStream is) {
        super(3);
        m_inStream = is;
        m_scanner = new Scanner(is);
        m_lineNb = 0;
    }


    @Override
    protected boolean compute(Object[] inputs, Queue<Object[]> output) throws ProcessorException {
        if(m_scanner == null)
            return false;

        if(!m_scanner.hasNextLine()) {
            m_scanner.close();
            return false;
        }

        String line = m_scanner.nextLine();
        m_lineNb++;

        output.add(new Object[]{line, m_lineNb, m_file});
        return true;
    }


    @Override
    public Processor duplicate() {
        try {
            return new InfoProviderLineReader(m_file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getSourceName() {
        return m_file.getAbsolutePath();
    }
}
