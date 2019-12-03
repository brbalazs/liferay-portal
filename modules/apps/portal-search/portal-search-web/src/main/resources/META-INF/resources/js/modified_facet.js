AUI.add(
	'liferay-search-modified-facet',
	function(A) {
		var DEFAULTS_FORM_VALIDATOR = A.config.FormValidator;

		var Language = Liferay.Language;
		var Util = Liferay.Util;

		var ModifiedFacetFilter = function(config) {
			var instance = this;

			instance.form = config.form;
			instance.fromInputDatePicker = config.fromInputDatePicker;
			instance.fromInputName = config.fromInputName;
			instance.namespace = config.namespace;
			instance.searchCustomRangeButton = config.searchCustomRangeButton;
			instance.toInputDatePicker = config.toInputDatePicker;
			instance.toInputName = config.toInputName;

			instance.fromInput = A.one('#' + instance.fromInputName);
			instance.toInput = A.one('#' + instance.toInputName);

			instance._initializeFormValidator();

			instance.searchCustomRangeButton.on(
				'click',
				A.bind(instance.filter, instance)
			);

			instance.fromInput.on('keydown', instance._onDateInputKeyDown);
			instance.toInput.on('keydown', instance._onDateInputKeyDown);
		};

		A.mix(
			ModifiedFacetFilter.prototype,
			{
				_initializeFormValidator() {
					var instance = this;

					var dateRangeRuleName = instance.namespace + 'dateRange';

					A.mix(
						DEFAULTS_FORM_VALIDATOR.STRINGS,
						{
							[dateRangeRuleName]: Language.get(
								'search-custom-range-invalid-date-range'
							)
						},
						true
					);

					A.mix(
						DEFAULTS_FORM_VALIDATOR.RULES,
						{
							[dateRangeRuleName]() {
								return A.Date.isGreaterOrEqual(
									instance.toInputDatePicker.getDate(),
									instance.fromInputDatePicker.getDate()
								);
							}
						},
						true
					);

					var customRangeValidator = new A.FormValidator({
						boundingBox: instance.form,
						fieldContainer: 'div',
						on: {
							errorField() {
								Util.toggleDisabled(
									instance.searchCustomRangeButton,
									true
								);
							},
							validField() {
								Util.toggleDisabled(
									instance.searchCustomRangeButton,
									false
								);
							}
						},
						rules: {
							[instance.fromInputName]: {
								[dateRangeRuleName]: true
							},
							[instance.toInputName]: {
								[dateRangeRuleName]: true
							}
						}
					});

					var onRangeSelectionChange = function() {
						customRangeValidator.validate();
					};

					instance.fromInputDatePicker.on(
						'selectionChange',
						onRangeSelectionChange
					);

					instance.toInputDatePicker.on(
						'selectionChange',
						onRangeSelectionChange
					);
				},

				_onDateInputKeyDown(event) {
					if (!event.isKey('TAB')) {
						event.preventDefault();
					}
				},

				filter: function() {
					var instance = this;

					var ModifiedFacetFilterUtil =
						Liferay.Search.ModifiedFacetFilterUtil;

					var fromDate = instance.fromInputDatePicker.getDate();

					var toDate = instance.toInputDatePicker.getDate();

					var modifiedFromParameter = ModifiedFacetFilterUtil.toLocaleDateStringFormatted(
						fromDate
					);

					var modifiedToParameter = ModifiedFacetFilterUtil.toLocaleDateStringFormatted(
						toDate
					);

					var param = ModifiedFacetFilterUtil.getParameterName();
					var paramFrom = param + 'From';
					var paramTo = param + 'To';

					var parameterArray = document.location.search.substr(1).split('&');

					parameterArray = Liferay.Search.FacetUtil.removeURLParameters(param, parameterArray);

					parameterArray = Liferay.Search.FacetUtil.removeURLParameters(paramFrom, parameterArray);

					parameterArray = Liferay.Search.FacetUtil.removeURLParameters(paramTo, parameterArray);

					parameterArray = Liferay.Search.FacetUtil.addURLParameter(paramFrom, modifiedFromParameter, parameterArray);

					parameterArray = Liferay.Search.FacetUtil.addURLParameter(paramTo, modifiedToParameter, parameterArray);

					document.location.search = parameterArray.join('&');
				}
			}
		);

		Liferay.namespace('Search').ModifiedFacetFilter = ModifiedFacetFilter;

		var ModifiedFacetFilterUtil = {
			clearSelections: function(event) {
				var param = Liferay.Search.ModifiedFacetFilterUtil.getParameterName();
				var paramFrom = param + 'From';
				var paramTo = param + 'To';

				var parameterArray = document.location.search.substr(1).split('&');

				parameterArray = Liferay.Search.FacetUtil.removeURLParameters(param, parameterArray);

				parameterArray = Liferay.Search.FacetUtil.removeURLParameters(paramFrom, parameterArray);

				parameterArray = Liferay.Search.FacetUtil.removeURLParameters(paramTo, parameterArray);

				document.location.search = parameterArray.join('&');
			},

			getParameterName: function() {
				return 'modified';
			},

			/**
			 * Formats a date to 'YYYY-MM-DD' format.
			 * @param {Date} date The date to format.
			 * @returns {String} The date string.
			 */
			toLocaleDateStringFormatted: function(date) {
				var localDate = new Date(date);

				localDate.setMinutes(
					date.getMinutes() - date.getTimezoneOffset()
				);

				return localDate.toISOString().split('T')[0];
			}
		};

		Liferay.namespace('Search').ModifiedFacetFilterUtil = ModifiedFacetFilterUtil;
	},
	'',
	{
		requires: ['aui-form-validator', 'liferay-search-facet-util']
	}
);