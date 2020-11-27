AUI.add(
	'liferay-ddm-form-field-validation',
	function(A) {
		var Lang = A.Lang;

		var Renderer = Liferay.DDM.Renderer;

		var Util = Renderer.Util;

		var ValidationField = A.Component.create(
			{
				ATTRS: {
					builder: {
						getter: '_getFormBuilder'
					},

					dataType: {
						value: ''
					},

					errorMessageValue: {
						value: ''
					},

					parameterValue: {
						value: ''
					},

					selectedValidation: {
						getter: '_getSelectedValidation',
						value: 'notEmpty'
					},

					strings: {
						value: {
							email: Liferay.Language.get('email'),
							errorMessage: Liferay.Language.get('error-message'),
							ifInput: Liferay.Language.get('if-input'),
							showErrorMessage: Liferay.Language.get('show-error-message'),
							theValue: Liferay.Language.get('the-value'),
							url: Liferay.Language.get('url'),
							validationMessage: Liferay.Language.get('validation')
						}
					},

					type: {
						value: 'validation'
					},

					validations: {
						getter: '_getValidations'
					},

					value: {
						setter: '_setValue',
						state: true,
						valueFn: '_validationValueFn'
					}
				},

				EXTENDS: Liferay.DDM.Renderer.Field,

				NAME: 'liferay-ddm-form-field-validation',

				prototype: {
					initializer: function() {
						var instance = this;

						instance._eventHandlers.push(
							instance.after('render', instance._loadValidationFieldType, instance),
							instance.after('validationChange', A.bind('_loadValidationFieldType', instance)),
							instance.after('valueChange', A.bind('_afterValueChange', instance)),
							instance.bindContainerEvent('change', A.bind('_setErrorMessage', instance), '.message-input'),
							instance.bindContainerEvent('change', A.bind('_syncValidationUI', instance), '.enable-validation')
						);
					},

					createDecimalField: function(context) {
						var instance = this;

						var config = A.merge(
							context,
							{
								bubbleTargets: [instance],
								context: A.clone(context),
								cssClass: 'validation-input',
								dataType: 'double'
							}
						);

						return new Liferay.DDM.Field.Numeric(config);
					},

					createIntegerField: function(context) {
						var instance = this;

						var config = A.merge(
							context,
							{

								bubbleTargets: [instance],
								context: A.clone(context),
								cssClass: 'validation-input'
							}
						);

						return new Liferay.DDM.Field.Numeric(config);
					},

					createSelectField: function(context) {
						var instance = this;

						var config = A.merge(
							context,
							{
								after: {
									valueChange: A.bind(instance._syncValidationUI, instance)
								},
								context: A.clone(context),
								showPlaceholder: false
							}
						);

						return new Liferay.DDM.Field.Select(config);
					},

					createTextField: function(context) {
						var instance = this;

						var config = A.merge(
							context,
							{
								bubbleTargets: [instance],
								context: A.clone(context),
								cssClass: 'validation-input'
							}
						);

						return new Liferay.DDM.Field.Text(config);
					},

					extractParameterValue: function(regex, expression) {
						var instance = this;

						regex.lastIndex = 0;

						var matches = regex.exec(expression);

						return matches && matches[2] || '';
					},

					getEvaluationContext: function(context) {
						return {
							validation: context.validation
						};
					},

					getTemplateContext: function() {
						var instance = this;

						var parameterMessage = '';

						var selectedValidation = instance.get('selectedValidation');

						if (selectedValidation) {
							parameterMessage = selectedValidation.parameterMessage;
						}

						var value = instance.get('value');

						var errorMessageValue = instance.get('errorMessageValue')

						if (Lang.isObject(errorMessageValue)) {
							if (errorMessageValue[instance._getEditingLanguageId()] !== undefined) {
								errorMessageValue = errorMessageValue[instance._getEditingLanguageId()];
							}
							else {
								errorMessageValue = errorMessageValue[instance._getDefaultLanguageId()];
							}
						}

						return A.merge(
							ValidationField.superclass.getTemplateContext.apply(instance, arguments),
							{
								enableValidationValue: !!(value && value.expression),
								errorMessageValue: errorMessageValue,
								parameterMessagePlaceholder: parameterMessage,
								parameterValue: instance.get('parameterValue'),
								strings: instance.get('strings'),
								validationsOptions: instance._getValidationsOptions()
							}
						);
					},

					getValue: function() {
						var instance = this;

						var expression = '';

						var selectedValidation = instance.get('selectedValidation');

						var validationEnabled = instance._getEnableValidationValue();

						if (selectedValidation && validationEnabled) {
							var root = instance.getRoot();

							var nameField = root.getField('name');

							expression = Lang.sub(
								selectedValidation.template,
								{
									name: nameField && nameField.get('value') || '',
									parameter: instance._getParameterValue()
								}
							);
						}

						var errorMessage = instance._getLocalizedErrorMessage();

						errorMessage[instance._getEditingLanguageId()] = instance._getMessageValue();

						return {
							errorMessage: errorMessage,
							expression: expression
						};
					},

					hasFocus: function(node) {
						var instance = this;

						return instance._validationOptions && instance._validationOptions.hasFocus(node);
					},

					_afterValueChange: function() {
						var instance = this;

						instance.evaluate();
					},

					_createField: function(dataType) {
						var instance = this;

						var parameterMessage = '';

						var selectedValidation = instance.get('selectedValidation');

						if (selectedValidation) {
							parameterMessage = selectedValidation.parameterMessage;
						}

						var fieldConfig = {
							fieldName: '',
							options: [],
							placeholder: parameterMessage,
							readOnly: false,
							showLabel: false,
							strings: {},
							value: instance.get('parameterValue'),
							visible: true
						};

						var field;

						if (dataType == 'double') {
							field = instance.createDecimalField(fieldConfig);
						}
						else if (dataType == 'integer') {
							field = instance.createIntegerField(fieldConfig);
						}
						else {
							field = instance.createTextField(fieldConfig);
						}

						field.after('blur', A.bind('_setParameterValue', instance));

						return field;
					},

					_getDefaultLanguageId: function() {
						var instance = this;

						var builder = instance.get('builder');

						var defaultLanguageId = instance.get('locale');

						if (builder) {
							defaultLanguageId = builder.get('defaultLanguageId');
						}

						return defaultLanguageId;
					},

					_getEditingLanguageId: function() {
						var instance = this;

						var builder = instance.get('builder');

						var currentEditingLanguageId = instance.get('locale');

						if (builder) {
							currentEditingLanguageId = builder.get('editingLanguageId');
						}

						return currentEditingLanguageId;
					},

					_getEnableValidationValue: function() {
						var instance = this;

						var container = instance.get('container');

						var enableValidationNode = container.one('.enable-validation');

						return !!enableValidationNode.attr('checked');
					},

					_getFormBuilder: function() {
						var instance = this;

						var form = instance.get('parent');

						var builder;

						if (form) {
							builder = form.get('builder');
						}

						return builder;
					},

					_getLocalizedErrorMessage() {
						var instance = this;

						var errorMessages = {};
						var localizedValue = instance.get('localizedValue');

						if (!localizedValue) {
							localizedValue = instance.get('value');

							Object.keys(localizedValue.errorMessage || {}).forEach(
								function(locale) {
									var value = localizedValue.errorMessage[locale];
			
									if (!value) {
										var defaultLanguageId = instance._getDefaultLanguageId();

										value = localizedValue.errorMessage[defaultLanguageId];
									}

									return errorMessages[locale] = value;
								}
							);
						}
						else {
							Object.keys(localizedValue || {}).forEach(
								function(locale) {
									var value = localizedValue[locale].errorMessage[locale];

									if (!value) {
										var defaultLanguageId = instance._getDefaultLanguageId();

										value = localizedValue[defaultLanguageId].errorMessage[defaultLanguageId];
									}

									return errorMessages[locale] = value;
								}
							);
						}

						return errorMessages;
					},

					_getMessageValue: function() {
						var instance = this;

						var container = instance.get('container');

						var messageNode = container.one('.message-input');

						return messageNode.val();
					},

					_getParameterValue: function() {
						var instance = this;

						var container = instance.get('container');

						var parameterNode = container.one('.validation-input input');

						return parameterNode.val();
					},

					_getSelectedValidation: function(val) {
						var instance = this;

						var validations = instance.get('validations');

						var selectedValidation = A.Array.find(
							validations,
							function(validation) {
								return validation.name === val;
							}
						);

						if (!selectedValidation) {
							selectedValidation = validations[0];
						}

						return selectedValidation;
					},

					_getValidations: function() {
						var instance = this;

						return Util.getValidations(instance.get('dataType')) || [];
					},

					_getValidationsOptions: function() {
						var instance = this;

						var selectedValidation = instance.get('selectedValidation');

						var validations = instance.get('validations');

						return validations.map(
							function(validation) {
								var status = '';

								if (selectedValidation && selectedValidation.name === validation.name) {
									status = 'selected';
								}

								return {
									label: validation.label,
									status: status,
									value: validation.name
								};
							}
						);
					},

					_loadValidationFieldType: function(event) {
						var instance = this;

						var validationContainer = A.one('.lfr-ddm-form-field-validation');

						var validationFieldContainers = validationContainer.all('.lfr-ddm-form-field-container');

						validationFieldContainers.each(
							function(validationFieldContainer) {
								if (validationFieldContainer) {
									validationFieldContainer.remove();
								}
							}
						);

						var context = instance.get('context');

						var validation = context.validation;

						var dataType = validation.dataType;

						var value = [];

						if (instance.get('selectedValidation')) {
							value.push(instance.get('selectedValidation').name);
						}

						instance._validationOptions = instance.createSelectField(
							{
								options: instance._getValidationsOptions(),
								value: value
							}
						);

						instance._validationField = instance._createField(dataType);

						instance._validationOptions.render(validationContainer.one('.validation-options'));

						instance._validationField.render(validationContainer.one('.validation-input'));
					},

					_setErrorMessage: function(event) {
						var instance = this;

						var input = event.target;

						instance.set('errorMessageValue', {
							...instance.get('errorMessageValue'),
							[instance._getEditingLanguageId()]: input.val()
						});

						instance.set('value', instance.getValue());
					},

					_setParameterValue: function(event) {
						var instance = this;

						var input = event.target;

						instance.set('parameterValue', input.get('value'));
						instance.set('value', instance.getValue());
					},

					_setValue: function(validation) {
						var instance = this;

						if (validation) {
							var errorMessage = validation.errorMessage;
							var expression = validation.expression;

							A.each(
								instance.get('validations'),
								function(item) {
									var regex = item.regex;

									if (regex.test(expression)) {
										instance.set('errorMessageValue', errorMessage);
										instance.set('selectedValidation', item.name);

										instance.set(
											'parameterValue',
											instance.extractParameterValue(regex, expression)
										);
									}
								}
							);
						}

						return validation;
					},

					_syncValidationUI: function(event) {
						var instance = this;

						var currentTarget = event.currentTarget;

						var newVal = '';

						if (currentTarget.get('type') == 'select') {
							newVal = currentTarget.get('value')[0];
							currentTarget = currentTarget.get('container');
						}
						else {
							newVal = currentTarget.val();
						}

						var selectedValidation = '';

						if (newVal) {
							selectedValidation = newVal;
						}

						if (currentTarget.hasClass('types-select')) {
							var validations = instance.get('validations');

							selectedValidation = validations[0].name;
						}

						instance.set('selectedValidation', selectedValidation);

						instance.set('value', instance.getValue());
					},

					_validationValueFn: function() {
						var instance = this;

						var errorMessage = {};

						errorMessage[instance._getEditingLanguageId()] = Liferay.Language.get('is-empty');

						return {
							errorMessage: errorMessage,
							expression: 'NOT(equals({name}, ""))'
						};
					}
				}
			}
		);

		Liferay.namespace('DDM.Field').Validation = ValidationField;
	},
	'',
	{
		requires: ['aui-dropdown', 'liferay-ddm-form-renderer-field']
	}
);