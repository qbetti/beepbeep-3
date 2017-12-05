package ca.uqac.lif.cep.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class OverwriteFile extends WriteOutputStream {

    private File m_file;

    public OverwriteFile(File file) throws FileNotFoundException {
        super(new FileOutputStream(file, false));
        m_file = file;
    }

    public OverwriteFile(String path) throws FileNotFoundException {
        this(new File(path));
    }


    @Override
    protected void append(Object o) throws IOException {
        m_outputStream.close();
        m_outputStream = new FileOutputStream(m_file, false);

        super.append(o);
    }
}
