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
package command.company
{
	import com.adobe.cairngorm.commands.ICommand;
	import com.adobe.cairngorm.control.CairngormEvent;

	import mx.controls.Alert;
	import mx.rpc.IResponder;

	import event.DefaultNavigationEvent;
	import event.company.CompanyCRUDEvent;

	import model.ApplicationModelLocator;
	import model.company.CompanyModel;

    import mx.core.Application;
	import mx.core.IFlexDisplayObject;
	import mx.managers.PopUpManager;


	/**
	 * @author Gonzalo Javier Clavell
	 * @since 20-May-2009
	 */
	public class CompanyCancelCommand implements ICommand
	{
		private var _model:CompanyModel = ApplicationModelLocator.instance.companyModel;

		public function execute(event:CairngormEvent):void
		{
            if (!_model.callFromPop)
            {
                new DefaultNavigationEvent("company.edit").dispatch();
            }
			else
			{
				var obj:Object = Application.application.systemManager.topLevelSystemManager.getChildByName(_model.callFromPop);
				PopUpManager.removePopUp(IFlexDisplayObject(obj));
				_model.callFromPop = null;
			}
            //No es una buena practica, pero me resulta de mas generar un evento/comando para nullear el selected en el model
			_model.selected = null;
		}

	}
}