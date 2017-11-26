/*
    BeepBeep, an event stream processor
    Copyright (C) 2008-2016 Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.cep;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Queue;
import java.util.Vector;

import org.junit.Test;

import ca.uqac.lif.cep.Connector.ConnectorException;
import ca.uqac.lif.cep.functions.FunctionProcessor;
import ca.uqac.lif.cep.functions.UnaryFunction;
import ca.uqac.lif.cep.numbers.Addition;
import ca.uqac.lif.cep.tmf.Passthrough;
import ca.uqac.lif.cep.tmf.QueueSink;
import ca.uqac.lif.cep.tmf.QueueSource;

/**
 * Unit tests for the {@link GroupProcessor}.
 * @author Sylvain Hallé
 */
public class GroupTest extends BeepBeepUnitTest 
{
	@Test
	public void testGroup1() throws ConnectorException
	{
		Passthrough pt1 = new Passthrough(1);
		GroupProcessor gp = new GroupProcessor(1, 1);
		gp.addProcessor(pt1);
		gp.associateInput(0, pt1, 0);
		gp.associateOutput(0, pt1, 0);
		Pushable push1 = gp.getPushableInput(0);
		QueueSink qsink = new QueueSink(1);
		Connector.connect(gp, qsink);
		Queue<Object> queue = qsink.getQueue(0);
		push1.push(0);
		Utilities.queueContains(0, queue);
		push1.push(1);
		Utilities.queueContains(1, queue);
		push1.push(2);
		Utilities.queueContains(2, queue);
	}
	
	@Test
	public void testGroup2() throws ConnectorException
	{
		Passthrough pt1 = new Passthrough(2);
		GroupProcessor gp = new GroupProcessor(2, 2);
		gp.addProcessor(pt1);
		gp.associateInput(0, pt1, 1);
		gp.associateInput(1, pt1, 0);
		gp.associateOutput(0, pt1, 0);
		gp.associateOutput(1, pt1, 1);
		Pushable push1 = gp.getPushableInput(0);
		assertNotNull(push1);
		Pushable push2 = gp.getPushableInput(1);
		assertNotNull(push2);
		QueueSink qsink = new QueueSink(2);
		Connector.connect(gp, qsink);
		Queue<Object> queue1 = qsink.getQueue(0);
		Queue<Object> queue2 = qsink.getQueue(1);
		push1.push(0);
		assertTrue(queue1.isEmpty());
		assertTrue(queue2.isEmpty());
		push2.push(100);
		Utilities.queueContains(100, queue1);
		Utilities.queueContains(0, queue2);
		push1.push(1);
		push2.push(101);
		Utilities.queueContains(101, queue1);
		Utilities.queueContains(1, queue2);
		push2.push(102);
		assertTrue(queue1.isEmpty());
		assertTrue(queue2.isEmpty());
	}
	
	@Test
	public void testGroup3() throws ConnectorException
	{
		Passthrough pt1 = new Passthrough(1);
		Passthrough pt2 = new Passthrough(1);
		Connector.connect(pt1, pt2);
		GroupProcessor gp = new GroupProcessor(1, 1);
		gp.addProcessor(pt1);
		gp.addProcessor(pt2);
		gp.associateInput(0, pt1, 0);
		gp.associateOutput(0, pt2, 0);
		Pushable push1 = gp.getPushableInput(0);
		QueueSink qsink = new QueueSink(1);
		Connector.connect(gp, qsink);
		Queue<Object> queue = qsink.getQueue(0);
		push1.push(0);
		Utilities.queueContains(0, queue);
		push1.push(1);
		Utilities.queueContains(1, queue);
		push1.push(2);
		Utilities.queueContains(2, queue);
	}

	@Test
	public void testClone1() throws ConnectorException
	{
		Passthrough pt1 = new Passthrough(1);
		GroupProcessor gp = new GroupProcessor(1, 1);
		gp.addProcessor(pt1);
		gp.associateInput(0, pt1, 0);
		gp.associateOutput(0, pt1, 0);
		GroupProcessor gp_clone = gp.duplicate();
		assertNotNull(gp_clone);
		// Make sure we don't refer accidentally to the original objects
		pt1 = null;
		gp = null;
		Pushable push1 = gp_clone.getPushableInput(0);
		QueueSink qsink = new QueueSink(1);
		Connector.connect(gp_clone, qsink);
		Queue<Object> queue = qsink.getQueue(0);
		push1.push(0);
		Utilities.queueContains(0, queue);
		push1.push(1);
		Utilities.queueContains(1, queue);
		push1.push(2);
		Utilities.queueContains(2, queue);
	}
	
	@Test
	public void testClone2() throws ConnectorException
	{
		Passthrough pt1 = new Passthrough(2);
		GroupProcessor gp = new GroupProcessor(2, 2);
		gp.addProcessor(pt1);
		gp.associateInput(0, pt1, 1);
		gp.associateInput(1, pt1, 0);
		gp.associateOutput(0, pt1, 0);
		gp.associateOutput(1, pt1, 1);
		GroupProcessor gp_clone = gp.duplicate();
		// Make sure we don't refer accidentally to the original objects
		pt1 = null;
		gp = null;
		Pushable push1 = gp_clone.getPushableInput(0);
		assertNotNull(push1);
		Pushable push2 = gp_clone.getPushableInput(1);
		assertNotNull(push2);
		QueueSink qsink = new QueueSink(2);
		Connector.connect(gp_clone, qsink);
		Queue<Object> queue1 = qsink.getQueue(0);
		Queue<Object> queue2 = qsink.getQueue(1);
		push1.push(0);
		assertTrue(queue1.isEmpty());
		assertTrue(queue2.isEmpty());
		push2.push(100);
		Utilities.queueContains(100, queue1);
		Utilities.queueContains(0, queue2);
		push1.push(1);
		push2.push(101);
		Utilities.queueContains(101, queue1);
		Utilities.queueContains(1, queue2);
		push2.push(102);
		assertTrue(queue1.isEmpty());
		assertTrue(queue2.isEmpty());
	}
	
	@Test
	public void testClone3() throws ConnectorException
	{
		Passthrough pt1 = new Passthrough(1);
		Passthrough pt2 = new Passthrough(1);
		Connector.connect(pt1, pt2);
		GroupProcessor gp = new GroupProcessor(1, 1);
		gp.addProcessor(pt1);
		gp.addProcessor(pt2);
		gp.associateInput(0, pt1, 0);
		gp.associateOutput(0, pt2, 0);
		GroupProcessor gp_clone = gp.duplicate();
		assertNotNull(gp_clone);
		// Make sure we don't refer accidentally to the original objects
		pt1 = null;
		pt2 = null;
		gp = null;
		Pushable push1 = gp_clone.getPushableInput(0);
		QueueSink qsink = new QueueSink(1);
		Connector.connect(gp_clone, qsink);
		Queue<Object> queue = qsink.getQueue(0);
		push1.push(0);
		Utilities.queueContains(0, queue);
		push1.push(1);
		Utilities.queueContains(1, queue);
		push1.push(2);
		Utilities.queueContains(2, queue);
	}
	
	@Test
	public void testClone4() throws ConnectorException
	{
		Passthrough pt1 = new Passthrough(2);
		Passthrough pt2 = new Passthrough(2);
		Connector.connect(pt1, pt2, 0, 1);
		Connector.connect(pt1, pt2, 1, 0);
		GroupProcessor gp = new GroupProcessor(2, 2);
		gp.addProcessor(pt1);
		gp.addProcessor(pt2);
		gp.associateInput(0, pt1, 0);
		gp.associateInput(1, pt1, 1);
		gp.associateOutput(0, pt2, 0);
		gp.associateOutput(1, pt2, 1);
		GroupProcessor gp_clone = gp.duplicate();
		pt1 = null;
		pt2 = null;
		gp = null;
		assertNotNull(gp_clone);
		Pushable push1 = gp_clone.getPushableInput(0);
		assertNotNull(push1);
		Pushable push2 = gp_clone.getPushableInput(1);
		assertNotNull(push2);
		QueueSink qsink = new QueueSink(2);
		Connector.connect(gp_clone, qsink);
		Queue<Object> queue1 = qsink.getQueue(0);
		Queue<Object> queue2 = qsink.getQueue(1);
		push1.push(0);
		assertTrue(queue1.isEmpty());
		assertTrue(queue2.isEmpty());
		push2.push(100);
		Utilities.queueContains(100, queue1);
		Utilities.queueContains(0, queue2);
		push1.push(1);
		push2.push(101);
		Utilities.queueContains(101, queue1);
		Utilities.queueContains(1, queue2);
		push2.push(102);
		assertTrue(queue1.isEmpty());
		assertTrue(queue2.isEmpty());
	}
	
	/**
	 * Try to clone a group processor that is already connected
	 * to something else. The goal of this test is only to check
	 * that the call to <code>duplicate()</code> does not throw an exception.
	 * @throws ConnectorException
	 */
	@Test
	public void testClone5() throws ConnectorException
	{
		Passthrough pt1 = new Passthrough(2);
		Passthrough pt2 = new Passthrough(2);
		Connector.connect(pt1, pt2, 0, 1);
		Connector.connect(pt1, pt2, 1, 0);
		GroupProcessor gp = new GroupProcessor(2, 2);
		gp.addProcessor(pt1);
		gp.addProcessor(pt2);
		gp.associateInput(0, pt1, 0);
		gp.associateInput(1, pt1, 1);
		gp.associateOutput(0, pt2, 0);
		gp.associateOutput(1, pt2, 1);
		QueueSource qs = new QueueSource(2);
		qs.addEvent(0);
		Connector.connect(qs, gp);
		GroupProcessor gp_clone = gp.duplicate();
		QueueSource qs2 = new QueueSource(2);
		qs2.addEvent(100);
		Connector.connect(qs2, gp_clone);
	}
	
	/**
	 * Clone a group within a group
	 * @throws ConnectorException 
	 */
	@Test
	public void testClone6() throws ConnectorException
	{
		Object o = null;
		GroupIn g_within = new GroupIn(1, 1);
		{
			PassthroughIn pt = new PassthroughIn(1);
			g_within.addProcessor(pt);
			g_within.associateInput(0, pt, 0);
			g_within.associateOutput(0, pt, 0);
		}
		GroupProcessor g_out = new GroupProcessor(1, 1);
		g_out.addProcessor(g_within);
		Passthrough pt = new Passthrough(1);
		g_out.addProcessor(pt);
		Connector.connect(pt, g_within);
		g_out.associateInput(0, pt, 0);
		g_out.associateOutput(0, g_within, 0);
		// Check that this piping works
		{
			QueueSource qs = new QueueSource(1);
			qs.addEvent(0);
			Connector.connect(qs, g_out);
			Pullable pull1 = g_out.getPullableOutput(0);
			o = pull1.pull();
			assertNotNull(o);
			assertEquals(0, ((Number) o).intValue());
		}
		// Now clone
		GroupProcessor g_clone = g_out.duplicate();
		{
			QueueSource qs = new QueueSource(1);
			qs.addEvent(0);
			Connector.connect(qs, g_clone);
			Pullable pull1 = g_clone.getPullableOutput(0);
			o = pull1.pull();
			assertNotNull(o);
			assertEquals(0, ((Number) o).intValue());
		}
	}
	
	/**
	 * Clone a group within a group.
	 * The difference with {@link #testClone6()} is that the
	 * group and the passthrough are in the reverse order.
	 * @throws ConnectorException 
	 */
	@Test
	public void testClone7() throws ConnectorException
	{
		Object o = null;
		GroupIn g_within = new GroupIn(1, 1);
		{
			PassthroughIn pt = new PassthroughIn(1);
			g_within.addProcessor(pt);
			g_within.associateInput(0, pt, 0);
			g_within.associateOutput(0, pt, 0);
		}
		GroupProcessor g_out = new GroupProcessor(1, 1);
		g_out.addProcessor(g_within);
		Passthrough pt = new Passthrough(1);
		g_out.addProcessor(pt);
		Connector.connect(g_within, pt);
		g_out.associateInput(0, g_within, 0);
		g_out.associateOutput(0, pt, 0);
		// Check that this piping works
		{
			QueueSource qs = new QueueSource(1);
			qs.addEvent(0);
			Connector.connect(qs, g_out);
			Pullable pull1 = g_out.getPullableOutput(0);
			o = pull1.pull();
			assertNotNull(o);
			assertEquals(0, ((Number) o).intValue());
		}
		// Now clone
		GroupProcessor g_clone = g_out.duplicate();
		{
			QueueSource qs = new QueueSource(1);
			qs.addEvent(0);
			Connector.connect(qs, g_clone);
			Pullable pull1 = g_clone.getPullableOutput(0);
			o = pull1.pull();
			assertNotNull(o);
			assertEquals(0, ((Number) o).intValue());
		}
	}
	
	@Test
	public void testGroupPull1() throws ConnectorException
	{
		// Create the group
		FunctionProcessor add = new FunctionProcessor(Addition.instance);
		GroupProcessor add_plus_10 = new GroupProcessor(2, 1);
		add_plus_10.addProcessor(add);
		add_plus_10.associateInput(0, add, 0);
		add_plus_10.associateInput(1, add, 1);
		add_plus_10.associateOutput(0, add, 0);
		
		// Connect the group to two sources and one sink
		Vector<Object> l_input1 = new Vector<Object>();
		l_input1.add(2);
		l_input1.add(3);
		l_input1.add(4);
		l_input1.add(6);
		QueueSource input1 = new QueueSource(1);
		input1.setEvents(l_input1);
		Vector<Object> l_input2 = new Vector<Object>();
		l_input2.add(1);
		l_input2.add(2);
		l_input2.add(3);
		l_input2.add(4);
		QueueSource input2 = new QueueSource(1);
		input2.setEvents(l_input2);
		Connector.connectFork(input1, input2, add_plus_10);
		QueueSink sink = new QueueSink(1);
		Connector.connect(add_plus_10, sink);
		Number recv, expected;
		
		// Run
		sink.pull();
		expected = 3;
		recv = (Number) sink.getQueue(0).remove();
		if (recv == null || recv.intValue() != expected.intValue())
		{
			fail("Expected " + expected + ", got " + recv);
		}
		sink.pull();
		expected = 5;
		recv = (Number) sink.getQueue(0).remove();
		if (recv == null || recv.intValue() != expected.intValue())
		{
			fail("Expected " + expected + ", got " + recv);
		}
		sink.pull();
		expected = 7;
		recv = (Number) sink.getQueue(0).remove();
		if (recv == null || recv.intValue() != expected.intValue())
		{
			fail("Expected " + expected + ", got " + recv);
		}
		sink.pull();
		expected = 10;
		recv = (Number) sink.getQueue(0).remove();
		if (recv == null || recv.intValue() != expected.intValue())
		{
			fail("Expected " + expected + ", got " + recv);
		}
	}
	
	@Test
	public void testGroupPush2() throws ConnectorException
	{
		// Create the group
		FunctionProcessor add = new FunctionProcessor(Addition.instance);
		Incrementer inc = new Incrementer(10);
		Connector.connect(inc, add, 0, 0);
		GroupProcessor add_plus_10 = new GroupProcessor(2, 1);
		add_plus_10.addProcessor(add);
		add_plus_10.associateInput(0, inc, 0);
		add_plus_10.associateInput(1, add, 1);
		add_plus_10.associateOutput(0, add, 0);
		
		// Connect the group to two sources and one sink
		Vector<Object> l_input1 = new Vector<Object>();
		l_input1.add(2);
		l_input1.add(3);
		l_input1.add(4);
		l_input1.add(6);
		QueueSource input1 = new QueueSource(1);
		input1.setEvents(l_input1);
		Vector<Object> l_input2 = new Vector<Object>();
		l_input2.add(1);
		l_input2.add(2);
		l_input2.add(3);
		l_input2.add(4);
		QueueSource input2 = new QueueSource(1);
		input2.setEvents(l_input2);
		Connector.connectFork(input1, input2, add_plus_10);
		QueueSink sink = new QueueSink(1);
		Connector.connect(add_plus_10, sink);
		Number recv;
		
		// Run
		input1.push();
		input2.push();
		recv = (Number) sink.getQueue(0).remove();
		assertEquals(13, recv.intValue());
		input1.push();
		input2.push();
		recv = (Number) sink.getQueue(0).remove();
		assertEquals(15, recv.intValue());
		input1.push();
		input2.push();
		recv = (Number) sink.getQueue(0).remove();
		assertEquals(17, recv.intValue());
		input1.push();
		input2.push();
		recv = (Number) sink.getQueue(0).remove();
		assertEquals(20, recv.intValue());
	}
	
	@Test
	public void testWrapperInGroup1() throws ConnectorException
	{
		GroupProcessor gp = new GroupProcessor(1, 1);
		Passthrough pt = new Passthrough(1);
		ProcessorWrapper pw = new ProcessorWrapper(pt);
		gp.addProcessor(pw);
		gp.associateInput(0, pw, 0);
		gp.associateOutput(0, pw, 0);
		QueueSink qs = new QueueSink(1);
		Connector.connect(gp, qs);
		Pushable p = gp.getPushableInput();
		p.push(0);
	}
	
	@Test
	public void testWrapperInGroup2() throws ConnectorException
	{
		GroupProcessor gp = new GroupProcessor(1, 1);
		Passthrough pt = new Passthrough(1);
		ProcessorWrapper pw = new ProcessorWrapper(pt);
		gp.addProcessor(pw);
		gp.associateInput(0, pw, 0);
		gp.associateOutput(0, pw, 0);
		GroupProcessor gp_new = gp.duplicate();
		QueueSink qs = new QueueSink(1);
		Connector.connect(gp_new, qs);
		Pushable p = gp_new.getPushableInput();
		p.push(0);
	}
	
	@Test
	public void testWrapperInGroup3() throws ConnectorException
	{
		GroupProcessor gp = new GroupProcessor(1, 1);
		Passthrough pt = new Passthrough(1);
		ProcessorWrapper pw = new ProcessorWrapper(pt);
		gp.addProcessor(pw);
		Passthrough pt2 = new Passthrough(1);
		gp.addProcessor(pt2);
		Connector.connect(pw, pt2);
		gp.associateInput(0, pw, 0);
		gp.associateOutput(0, pt2, 0);
		GroupProcessor gp_new = gp.duplicate();
		QueueSink qs = new QueueSink(1);
		Connector.connect(gp_new, qs);
		Pushable p = gp_new.getPushableInput();
		p.push(0);
	}
	
	public static class Incrementer extends FunctionProcessor
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1503097815282943066L;

		public Incrementer(int increment)
		{
			super(new IncrementFunction(increment));
		}
	}
	
	public static class IncrementFunction extends UnaryFunction<Number,Number>
	{
		int m_increment;
		
		public IncrementFunction(int increment)
		{
			super(Number.class, Number.class);
			m_increment = increment;
		}
		
		@Override
		public Number getValue(Number x)
		{
			return x.intValue() + m_increment;
		}	
	}

	public static class PassthroughIn extends Passthrough
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 3684767348745275848L;

		public PassthroughIn(int arity) 
		{
			super(arity);
		}
		
		public PassthroughIn duplicate()
		{
			return new PassthroughIn(getInputArity());
		}
		
	}
	
	public static class GroupIn extends GroupProcessor
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 3020653608268940867L;

		public GroupIn(int in_arity, int out_arity) {
			super(in_arity, out_arity);
			// TODO Auto-generated constructor stub
		}
		
		public GroupIn duplicate()
		{
			GroupIn in = new GroupIn(getInputArity(), getOutputArity());
			super.cloneInto(in);
			return in;
		}
	}
}
