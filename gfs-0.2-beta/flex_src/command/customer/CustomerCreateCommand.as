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
 */
package command.customer
{
	import com.adobe.cairngorm.commands.ICommand;
	import com.adobe.cairngorm.control.CairngormEvent;
	
	import mx.rpc.IResponder;
	
	import event.DefaultNavigationEvent;
	import event.customer.CustomerCRUDEvent;
	
	import model.ApplicationModelLocator;
	import model.customer.CustomerModel;
	import vo.customer.CustomerVO;

	/**
	 * @author Ezequiel Martin Apfel
	 * @since 23-Feb-2009
	 */
	public class CustomerCreateCommand implements ICommand
	{
        private var _model:CustomerModel = ApplicationModelLocator.instance.customerModel;

        public function execute(event:CairngormEvent):void
		{
            
			var crudEvent:CustomerCRUDEvent = CustomerCRUDEvent(event); 
			
			_model.selected = new CustomerVO();
			_model.editView = false;

            _model.callFromPop = crudEvent.popUpName;

			if (!_model.callFromPop)
                new DefaultNavigationEvent("customer.list").dispatch();
		}
	}
}