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
package org.cubika.labs.scaffolding.utils

import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClassProperty
import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass
import org.codehaus.groovy.grails.scaffolding.DomainClassPropertyComparator
import org.codehaus.groovy.grails.orm.hibernate.support.ClosureEventTriggeringInterceptor as Events
import grails.util.BuildSettingsHolder

import org.cubika.labs.scaffolding.form.FormItemConstants as FIC
import org.cubika.labs.scaffolding.utils.ConstraintValueUtils as CVU
import org.codehaus.groovy.grails.commons.GrailsDomainClass

/**
 * Utils used in flex templates  
 * @author Ezequiel Martin Apfel
 * @since 01-Feb-2009
 */
class FlexScaffoldingUtils
{
	/**
	 * Map with equivalent class AS3 / Groovy
	 */
	static private Map typesAS3Map = new HashMap()
	
	static private def resolver = new PathMatchingResourcePatternResolver()

    static public def grailsApplication
	
	static {
		
		//TODO: Add More types
		typesAS3Map.put(Boolean.class,['class':"Boolean",'import':""]);
		typesAS3Map.put(Long.class,['class':"Number",'import':"",'cast':"Number"]);
		typesAS3Map.put(Integer.class,['class':"int",'import':"",'cast':"Number"]);
		typesAS3Map.put(Double.class,['class':"Number",'import':"",'cast':"Number"]);
		typesAS3Map.put(Float.class,['class':"Number",'import':"",'cast':"Number"]);
		typesAS3Map.put(Date.class,['class':"Date",'import':""]);
		typesAS3Map.put(String.class,['class':"String",'import':"",'cast':"String"]);
		typesAS3Map.put(Set.class,['class':"ArrayCollection",'import':"import mx.collections.ArrayCollection"]);
        typesAS3Map.put(List.class,['class':"ArrayCollection",'import':"import mx.collections.ArrayCollection"]);

	}
	
	/**
	 * Gets equivalent AS3 Class for Groovy TODO: o Java class
	 * @param property 	- DefaultGrailsDomainClassProperty
	 * @return String 	- equivalent AS3 class
	 */
	static String getClass4AS3(DefaultGrailsDomainClassProperty property)
	{	
		//If property.type is contained in typeAS3Map
		if (typesAS3Map.containsKey(property.type) && !property.isIdentity() && property.name != "version" && CVU.display(property))
		{		
			return typesAS3Map.get(property.type)['class']+CVU.defaultValue(property)
		}
		
		//If property is a Relation
		if ((property.isOneToOne() || property.isManyToOne()) && CVU.display(property))
		{
			return "${property.referencedDomainClass.shortName}VO"
		}

		if (property.isIdentity())
		{
			return "Object"
		}
		
		if (property.name == "version")
		{
			return "Object"
		}
		
		//otherwise
		if (CVU.display(property))
        return "${property.type.propertyName}${CVU.defaultValue(property)}"
	}
	
	/**
	 * Gets class cast for as3
	 * @param property 	- DefaultGrailsDomainClassProperty
	 * @return String 	- cast for as3
	 */
	static String getClassCast(property)
	{
		if (typesAS3Map.containsKey(property.type))
		{
			return typesAS3Map.get(property.type)['cast'];
		} 
	}
	
	/**
	 * Gets import for as3   
	 * @param property - DefaultGrailsDomainClassProperty
	 * @return import for AS3 Class
	 */
	static String getImport4AS3(DefaultGrailsDomainClassProperty property)
	{
		
		if ((property.isOneToOne() || property.isManyToOne()) && CVU.display(property))
		{
			//vo.package.class
			return "import vo.${property.referencedDomainClass.propertyName}.${property.referencedDomainClass.shortName}VO"
		}
		
		if (typesAS3Map.containsKey(property.type)  && CVU.display(property))
		{
			return typesAS3Map.get(property.type)['import'];
		}
	}
	
	/**
	 * Gets DataGridColumn per DefaultGrailsDomainClassProperty	 
	 * TODO: create strategies for column itemRenderer
	 * @param property 	- DefaultGrailsDomainClassProperty
	 * @return String 	- DataGridColumn
	 */
	static String getDataGridColumn(DefaultGrailsDomainClassProperty property)
	{
		String result
		def sw
		def pw
		
		if (!property.isOneToOne() && !property.isOneToMany() && !property.isManyToOne() && CVU.display(property))
		{
			sw = new StringWriter()
			pw = new PrintWriter(sw)
			
			if (property.type == Date.class)
			{
				pw.println "<cubikalabs:CBKDateFormatterDataGridColumn dataField=\"${property.name}\" "+
				"headerText=\"{MultipleRM.getString(MultipleRM.localePrefix,'${property.domainClass.propertyName}.${property.name}')}\" />"

				return sw.toString()
			}
			
			if (CVU.richtext(property))
			{
				pw.println "<mx:DataGridColumn dataField=\"${property.name}\" "+
					"headerText=\"{MultipleRM.getString(MultipleRM.localePrefix,'${property.domainClass.propertyName}.${property.name}')}\">\n"+
					"					<mx:itemRenderer>\n"+
					"						<mx:Component>\n"+
					"							<mx:Label htmlText=\"{data.description}\"/>\n"+
					"						</mx:Component>\n"+
					"					</mx:itemRenderer>\n"+
					"</mx:DataGridColumn>"
				return sw.toString()
			}
			
			pw.println "<mx:DataGridColumn dataField=\"${property.name}\" "+
			"headerText=\"{MultipleRM.getString(MultipleRM.localePrefix,'${property.domainClass.propertyName}.${property.name}')}\" />"
			
			result = sw.toString()
		}
		
		return result
	}
	
	/**
	 * Capitalize a propertyname
	 * @param s - String 
	 * @return s capitalizate
	 */
	static String capitalize(String s)
	{
		return s.getAt(0).toUpperCase()+s.getAt(1..s.size()-1)
	}
	
	/**
	 * Gets array of DefaultGrailsDomainClassProperty with identity (id, version)
	 * @parm	domainClass - DefaultGrailsDomainClass
	 * @return DefaultGrailsDomainClassProperty[] with id and version
	 */
	static DefaultGrailsDomainClassProperty[] getPropertiesWithIdentity(DefaultGrailsDomainClass domainClass, Boolean inherited=true)
	{
		def excludedProps = [Events.ONLOAD_EVENT,
            Events.BEFORE_DELETE_EVENT,
            Events.BEFORE_INSERT_EVENT,
            Events.BEFORE_UPDATE_EVENT]
                       
        def properties = domainClass.properties

		if (inherited)
        properties = properties.findAll { !excludedProps.contains(it.name)}
		else
        properties = properties.findAll { !excludedProps.contains(it.name) && !it.inherited}
			
		properties
	}
	
	/**
	 * Gets array of DefaultGrailsDomainClassProperty without identity (id, version)
	 * @param order 			- if order is true the array is order by DomainClassPropertyComparator
	 * @param domainClass 		- DefaultGrailsDomainClass
	 * @return DefaultGrailsDomainClassProperty[] without id and version
	 */
	static DefaultGrailsDomainClassProperty[] getPropertiesWithoutIdentity(DefaultGrailsDomainClass domainClass, Boolean order=false, inherited=true)
	{
		def excludedProps = ['id', 'version',Events.ONLOAD_EVENT,
            Events.BEFORE_DELETE_EVENT,
            Events.BEFORE_INSERT_EVENT,
            Events.BEFORE_UPDATE_EVENT]
		
		def properties = domainClass.properties
		
		if (inherited)
        properties = properties.findAll { !excludedProps.contains(it.name)}
		else
        properties = properties.findAll { !excludedProps.contains(it.name) && !it.inherited}
		
		if (order)
		{
			DomainClassPropertyComparator comparator = new DomainClassPropertyComparator(domainClass)
			Collections.sort(properties, comparator)
		}
		
		properties
	}
	
	/**
	 * Gets namespace of RelationView
	 * @param property 	- DefaultGrailsDomainClassProperty
	 * @return String 	- namespace of RelationView
	 */
	static String getNameSpace(properties)
	{
		def ns = "" 
		properties.each
		{
			if ((it.isOneToMany() || it.isOneToOne() || it.isManyToOne()) && CVU.display(it))
            ns += "\nxmlns:${it.name}=\"view.${it.referencedDomainClass.propertyName}.external.*\" "
		}
		
		ns
	}
	
	
	/**
	 * Resolve path for pluging dir  
	 * @param pattern - String with partil path 
	 * @return a concret path
	 */
	static def resolveResources(pattern)
	{
		try 
		{
			def path
			//if is a localplugin
			def dir = resolver.getResources("file:${BuildSettingsHolder.settings?.projectPluginsDir.canonicalFile}${pattern}")
			
			if (dir)	
            path = dir.file[0]
			
			if (!path)
			{
				//if is a globalplugin
				if (BuildSettingsHolder.settings?.globalPluginsDir.exists())
                dir = resolver.getResources("file:${BuildSettingsHolder.settings?.globalPluginsDir.canonicalFile}${pattern}")
				
				if (dir)	
                path = dir.file[0]
			}
				
			path
    	}
    	catch (Throwable e) 
        {
            throw new Exception("${BuildSettingsHolder.settings?.projectPluginsDir.canonicalFile}${pattern} Not Found $e")
        }
  	}

    static String getSuperClassName(domainClass)
    {

        Class superClass = domainClass.getClazz().getSuperclass()

        if (!superClass.equals(Object.class) && !superClass.equals(GroovyObject.class))
        {
            GrailsDomainClass gdc = (GrailsDomainClass) grailsApplication.getDomainClass(superClass.getName())
            return gdc.propertyName
        }
    }
}
