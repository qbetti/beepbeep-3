package ca.uqac.lif.cep.tmf;

import ca.uqac.lif.cep.Pullable;
import ca.uqac.lif.cep.io.IoTest;
import ca.uqac.lif.cep.io.ReadLines;
import ca.uqac.lif.cep.util.FileHelper;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ExtendedSpliceTest
{
    @Test
    public void test1 ()
    {
        InputStream is0 = FileHelper.internalFileToStream(IoTest.class, "resource/test1.txt");
        InputStream is1 = FileHelper.internalFileToStream(IoTest.class, "resource/test2.txt");
        ReadLines rl0 = new ReadLines(is0);
        ReadLines rl1 = new ReadLines(is1);
        ExtendedSplice es = new ExtendedSplice(rl0, rl1);
        Pullable p0 = es.getPullableOutput(0);
        Pullable p1 = es.getPullableOutput(1);
        assertEquals("A simple text file with short text.", (String) p0.pull());
        assertEquals(0, ((Number) p1.pull()).intValue());
        assertEquals("foo", (String) p0.pull());
        assertEquals(1, ((Number) p1.pull()).intValue());
        assertEquals("bar", (String) p0.pull());
        assertEquals(1, ((Number) p1.pull()).intValue());
        assertEquals("baz", (String) p0.pull());
        assertEquals(1, ((Number) p1.pull()).intValue());
        assertFalse(p0.hasNext());
        assertFalse(p1.hasNext());
    }
}
