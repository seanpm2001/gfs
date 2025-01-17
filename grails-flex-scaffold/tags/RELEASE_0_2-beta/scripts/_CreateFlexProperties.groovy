/**
 * Copyright 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Ezequiel Martin Apfel
 * @since 03-Mar-2009
 */

import org.codehaus.groovy.grails.commons.GrailsClassUtils as GCU

flexHome = Ant.project.properties."environment.FLEX_HOME"

Ant.property(file:"${flexScaffoldPluginDir}/scripts/flexScaffold.properties")

def antProp = Ant.project.properties

createFlexProperties = 
{	Map args = [:] ->
	Ant.sequential
	{
		copy(file: "${flexScaffoldPluginDir}/src/flex/flex.properties", 
			tofile: "$basedir/flex.properties", overwrite: true)
		replace(file: "$basedir/flex.properties",
 			token: "@project-name@", value: "${grailsAppName}")
	}
}