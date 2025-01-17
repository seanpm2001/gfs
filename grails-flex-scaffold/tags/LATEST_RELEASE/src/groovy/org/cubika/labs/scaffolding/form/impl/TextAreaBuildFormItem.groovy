package org.cubika.labs.scaffolding.form.impl

////////////////////////////////////////////////////////////////////
// Copyright 2009 the original author or authors.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
////////////////////////////////////////////////////////////////////

import org.cubika.labs.scaffolding.form.BuildFormItem
import org.cubika.labs.scaffolding.utils.FlexScaffoldingUtils as FSU

/**
 * Extends AbstractBuildFormItem adding textarea building functionallity
 *
 * @author Ezequiel Martin Apfel
 * @since  3-Feb-2009
 */
class TextAreaBuildFormItem extends AbstractBuildFormItem 
{
	/**
	 * Constructor
	 *
	 */
	TextAreaBuildFormItem(property)
	{
		super(property)
	}
	
	/**
	 * @see #AbstractBuildFormItem
	 *
	 */
	def buildFormItemComponent(binding)
	{
		def sw = new StringWriter()
		def pw = new PrintWriter(sw)
		
		pw.println	"				<mx:TextArea id=\"${getID()}\" text=\"{${binding}}\" width=\"217\"/>"
		
		sw.toString()
	}
	
	/**
	 * @see #AbstractBuildFormItem
	 *
	 */
	String getFormAttr()
	{
		def cast
		if (constraint.widget)
			cast = FSU.getClassCast(property)
		
		if (cast)	
			return "${cast}(${getID()}.${value()})"
		
		"${getID()}.${value()}"
	}
	
	/**
	 * @see #AbstractBuildFormItem
	 *
	 */
	String getID()
	{
		"txt${FSU.capitalize(property.name)}"
	}
	
	/**
	 * @see #AbstractBuildFormItem
	 *
	 */
	String value()
	{
		"text"
	}
}
