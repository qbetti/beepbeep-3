/*
    BeepBeep, an event stream processor
    Copyright (C) 2008-2015 Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.cep;

/**
 * Represents a stateless <i>m</i>-to-<i>n</i> function.
 * 
 * @author Sylvain Hallé
 */
public interface Computable
{
	/**
	 * The maximum input arity that a computable can have
	 */
	public static int s_maxInputArity = 10;
	
	public Object[] compute(Object[] inputs);
	
	public int getInputArity();
	
	public int getOutputArity();
}
