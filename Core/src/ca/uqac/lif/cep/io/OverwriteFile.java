package ca.uqac.lif.cep.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Processor which overwrites a file before
 * each write into it.
 * @author Quentin Betti
 */
public class OverwriteFile extends WriteOutputStream
{
    /**
     * The file to overwrite
     */
    private File m_file;

    /**
     * Creates a new {@link OverwriteFile} processor
     * @param file The file to overwrite
     * @throws FileNotFoundException
     */
    public OverwriteFile(File file) throws FileNotFoundException
    {
        super(new FileOutputStream(file, false));
        m_file = file;
    }

    /**
     * Creates a new {@link OverwriteFile} processor
     * @param path The path of the file to overwrite
     * @throws FileNotFoundException
     */
    public OverwriteFile(String path) throws FileNotFoundException
    {
        this(new File(path));
    }

    @Override
    protected void append(Object o) throws IOException
    {
        m_outputStream.close();
        m_outputStream = new FileOutputStream(m_file, false);
        super.append(o);
    }
}
