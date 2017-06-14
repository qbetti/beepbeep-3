/*
    BeepBeep, an event stream processor
    Copyright (C) 2008-2017 Sylvain HallÃ©

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
package ca.uqac.lif.cep.tmf;

import java.util.Queue;

import ca.uqac.lif.cep.SingleProcessor;
import ca.uqac.lif.cep.functions.Equals;
import ca.uqac.lif.cep.functions.Function;

/**
 * A special case is when the function is equality, in which case the
 * processor removes stuttering events in the traditional sense of the
 * term.
 * <p>
 * Note that this is one example of a processor that could be obtained
 * by combining more primitive processors. For example, one could
 * {@link Fork} the input in three, {@link Trim} the first copy by one
 * event, use a {@link FunctionProcessor} to compare the trimmed trace
 * with the second fork, and send the third fork and the result of that
 * comparison into a {@link Filter}. As such, this processor does not
 * provide any increased expressiveness, but is more convenient to use.
 * 
 * @author Sylvain Hallé
 *
 */
public class StutterRemove extends WindowFunction 
{
	/**
	 * Whether this is the first window to be processed
	 */
	boolean m_firstWindow = true;
	
	public StutterRemove(Function condition)
	{
		super(condition);
	}
	
	public StutterRemove()
	{
		this(Equals.instance);
	}
	
	@Override
	public void reset()
	{
		super.reset();
		m_firstWindow = true;
	}

	@Override
	protected boolean doWithWindow(Object[] inputs, Queue<Object[]> outputs) 
	{
		if (m_firstWindow)
		{
			m_firstWindow = false;
			outputs.add(SingleProcessor.wrapObject(inputs[0]));
		}
		Object[] value = new Object[1];
		m_function.evaluate(inputs, value);
		if ((Boolean) value[0] == false)
			outputs.add(SingleProcessor.wrapObject(inputs[inputs.length - 1]));
		return true;
	}

	@Override
	public StutterRemove clone() 
	{
		return new StutterRemove(m_function.clone());
	}
}