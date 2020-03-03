<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>

<%@ include file="/init.jsp" %>

<%
CPDefinitionSubscriptionInfoDisplayContext cpDefinitionSubscriptionInfoDisplayContext = (CPDefinitionSubscriptionInfoDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

CPDefinition cpDefinition = cpDefinitionSubscriptionInfoDisplayContext.getCPDefinition();
long cpDefinitionId = cpDefinitionSubscriptionInfoDisplayContext.getCPDefinitionId();
List<CPSubscriptionType> cpSubscriptionTypes = cpDefinitionSubscriptionInfoDisplayContext.getCPSubscriptionTypes();

String defaultCPSubscriptionType = StringPool.BLANK;

if (!cpSubscriptionTypes.isEmpty()) {
	CPSubscriptionType firstCPSubscriptionType = cpSubscriptionTypes.get(0);

	defaultCPSubscriptionType = firstCPSubscriptionType.getName();
}

boolean subscriptionEnabled = BeanParamUtil.getBoolean(cpDefinition, request, "subscriptionEnabled", false);
int subscriptionLength = BeanParamUtil.getInteger(cpDefinition, request, "subscriptionLength", 1);
String subscriptionType = BeanParamUtil.getString(cpDefinition, request, "subscriptionType", defaultCPSubscriptionType);
long maxSubscriptionCycles = BeanParamUtil.getLong(cpDefinition, request, "maxSubscriptionCycles");

boolean deliverySubscriptionEnabled = BeanParamUtil.getBoolean(cpDefinition, request, "deliverySubscriptionEnabled", false);
int deliverySubscriptionLength = BeanParamUtil.getInteger(cpDefinition, request, "deliverySubscriptionLength", 1);
String deliverySubscriptionType = BeanParamUtil.getString(cpDefinition, request, "deliverySubscriptionType", defaultCPSubscriptionType);
long deliveryMaxSubscriptionCycles = BeanParamUtil.getLong(cpDefinition, request, "deliveryMaxSubscriptionCycles");

String defaultCPSubscriptionTypeLabel = StringPool.BLANK;

CPSubscriptionType cpSubscriptionType = cpDefinitionSubscriptionInfoDisplayContext.getCPSubscriptionType(subscriptionType);

if (cpSubscriptionType != null) {
	defaultCPSubscriptionTypeLabel = cpSubscriptionType.getLabel(locale);
}

CPSubscriptionTypeJSPContributor cpSubscriptionTypeJSPContributor = cpDefinitionSubscriptionInfoDisplayContext.getCPSubscriptionTypeJSPContributor(subscriptionType);

boolean ending = false;
boolean deliveryEnding = false;

if (maxSubscriptionCycles > 0) {
	ending = true;
}

if (deliveryMaxSubscriptionCycles > 0) {
	deliveryEnding = true;
}
%>

<aui:alert closeable="<%= false %>" cssClass="mt-3" type="warning">
	<liferay-ui:message key="all-channels-associated-with-this-product-must-have-at-least-one-payment-method-active-that-supports-recurring-payments" />
</aui:alert>

<portlet:actionURL name="editProductDefinition" var="editProductDefinitionSubscriptionInfoActionURL" />

<aui:form action="<%= editProductDefinitionSubscriptionInfoActionURL %>" method="post" name="fm">
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="updateSubscriptionInfo" />
	<aui:input name="redirect" type="hidden" value="<%= String.valueOf(cpDefinitionSubscriptionInfoDisplayContext.getPortletURL()) %>" />
	<aui:input name="cpDefinitionId" type="hidden" value="<%= cpDefinitionId %>" />

	<aui:model-context bean="<%= cpDefinition %>" model="<%= CPDefinition.class %>" />

	<aui:input checked="<%= subscriptionEnabled %>" label="enable-subscription" name="subscriptionEnabled" type="toggle-switch" value="<%= subscriptionEnabled %>" />

	<div class="<%= subscriptionEnabled ? StringPool.BLANK : "hide" %>" id="<portlet:namespace />subscriptionOptions">
		<aui:select name="subscriptionType" onChange='<%= renderResponse.getNamespace() + "selectSubscriptionType();" %>'>

			<%
			for (CPSubscriptionType curCPSubscriptionType : cpSubscriptionTypes) {
			%>

				<aui:option data-label="<%= curCPSubscriptionType.getLabel(locale) %>" label="<%= curCPSubscriptionType.getLabel(locale) %>" selected="<%= subscriptionType.equals(curCPSubscriptionType.getName()) %>" value="<%= curCPSubscriptionType.getName() %>" />

			<%
			}
			%>

		</aui:select>

		<%
		if (cpSubscriptionTypeJSPContributor != null) {
			cpSubscriptionTypeJSPContributor.render(cpDefinition, request, PipingServletResponse.createPipingServletResponse(pageContext));
		}
		%>

		<div id="<portlet:namespace />cycleLengthContainer">
			<aui:input name="subscriptionLength" suffix="<%= defaultCPSubscriptionTypeLabel %>" value="<%= String.valueOf(subscriptionLength) %>">
				<aui:validator name="digits" />
				<aui:validator name="min">1</aui:validator>
			</aui:input>
		</div>

		<div id="<portlet:namespace />neverEndsContainer">
			<div class="never-ends-header">
				<aui:input checked="<%= ending ? false : true %>" name="neverEnds" type="toggle-switch" />
			</div>

			<div class="never-ends-content">
				<aui:input disabled="<%= ending ? false : true %>" helpMessage="max-subscription-cycles-help" label="end-after" name="maxSubscriptionCycles" suffix='<%= LanguageUtil.get(request, "cycles") %>' value="<%= String.valueOf(maxSubscriptionCycles) %>">
					<aui:validator name="digits" />

					<aui:validator errorMessage='<%= LanguageUtil.format(request, "please-enter-a-value-greater-than-or-equal-to-x", 1) %>' name="custom">
						function(val, fieldNode, ruleValue) {
							if (AUI.$('#<portlet:namespace />neverEnds')[0].checked) {
								return true;
							}

							if (parseInt(val, 10) > 0) {
								return true;
							}

							return false;
						}
					</aui:validator>
				</aui:input>
			</div>
		</div>
	</div>
</aui:form>

<aui:form action="<%= editProductDefinitionSubscriptionInfoActionURL %>" cssClass="container-fluid-1280" method="post" name="fmDelivery">
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="updateDeliverySubscriptionInfo" />
	<aui:input name="redirect" type="hidden" value="<%= String.valueOf(cpDefinitionSubscriptionInfoDisplayContext.getPortletURL()) %>" />
	<aui:input name="cpDefinitionId" type="hidden" value="<%= cpDefinitionId %>" />

	<aui:model-context bean="<%= cpDefinition %>" model="<%= CPDefinition.class %>" />

	<aui:fieldset-group markupView="lexicon">
		<aui:fieldset>
			<aui:input checked="<%= deliverySubscriptionEnabled %>" label="enable-delivery-subscription" name="deliverySubscriptionEnabled" type="toggle-switch" value="<%= deliverySubscriptionEnabled %>" />

			<div class="<%= subscriptionEnabled ? StringPool.BLANK : "hide" %>" id="<portlet:namespace />deliverySubscriptionOptions">
				<aui:select name="deliverySubscriptionType" onChange='<%= renderResponse.getNamespace() + "selectDeliverySubscriptionType();" %>'>

					<%
						for (CPSubscriptionType curCPSubscriptionType : cpSubscriptionTypes) {
					%>

					<aui:option data-label="<%= curCPSubscriptionType.getLabel(locale) %>" label="<%= curCPSubscriptionType.getLabel(locale) %>" selected="<%= deliverySubscriptionType.equals(curCPSubscriptionType.getName()) %>" value="<%= curCPSubscriptionType.getName() %>" />

					<%
						}
					%>

				</aui:select>

				<%
					if (cpSubscriptionTypeJSPContributor != null) {
						cpSubscriptionTypeJSPContributor.render(cpDefinition, request, PipingServletResponse.createPipingServletResponse(pageContext));
					}
				%>

				<div id="<portlet:namespace />cycleLengthContainer">
					<aui:input name="deliverySubscriptionLength" suffix="<%= defaultCPSubscriptionTypeLabel %>" value="<%= String.valueOf(deliverySubscriptionLength) %>">
						<aui:validator name="digits" />
						<aui:validator name="min">1</aui:validator>
					</aui:input>
				</div>

				<div id="<portlet:namespace />deliveryNeverEndsContainer">
					<div class="never-ends-header">
						<aui:input checked="<%= ending ? false : true %>" name="deliveryNeverEnds" type="toggle-switch" />
					</div>

					<div class="never-ends-content">
						<aui:input disabled="<%= ending ? false : true %>" helpMessage="max-subscription-cycles-help" label="end-after" name="deliveryMaxSubscriptionCycles" suffix='<%= LanguageUtil.get(request, "cycles") %>' value="<%= String.valueOf(deliveryMaxSubscriptionCycles) %>">
							<aui:validator name="digits" />

							<aui:validator errorMessage='<%= LanguageUtil.format(request, "please-enter-a-value-greater-than-or-equal-to-x", 1) %>' name="custom">
								function(val, fieldNode, ruleValue) {
								if (AUI.$('#<portlet:namespace />neverEnds')[0].checked) {
								return true;
								}

								if (parseInt(val, 10) > 0) {
								return true;
								}

								return false;
								}
							</aui:validator>
						</aui:input>
					</div>
				</div>
			</div>
		</aui:fieldset>
	</aui:fieldset-group>

	<aui:button-row>
		<aui:button cssClass="btn-lg" type="submit" />

		<aui:button cssClass="btn-lg" href="<%= catalogURL %>" type="cancel" />
	</aui:button-row>
</aui:form>

<aui:script>
	Liferay.Util.toggleBoxes(
		'<portlet:namespace />subscriptionEnabled',
		'<portlet:namespace />subscriptionOptions'
	);

	Liferay.Util.toggleBoxes(
		'<portlet:namespace />deliverySubscriptionEnabled',
		'<portlet:namespace />deliverySubscriptionOptions'
	);

	Liferay.provide(
		window,
		'<portlet:namespace />selectSubscriptionType',
		function() {
			var A = AUI();

			var subscriptionEnabled = A.one(
				'#<portlet:namespace />subscriptionEnabled'
			).attr('checked');
			var subscriptionLength = A.one(
				'#<portlet:namespace />subscriptionLength'
			).val();
			var subscriptionType = A.one(
				'#<portlet:namespace />subscriptionType'
			).val();
			var maxSubscriptionCycles = A.one(
				'#<portlet:namespace />maxSubscriptionCycles'
			).val();

			var portletURL = new Liferay.PortletURL.createURL(
				'<%= currentURLObj %>'
			);

			portletURL.setParameter('subscriptionEnabled', subscriptionEnabled);
			portletURL.setParameter('subscriptionLength', subscriptionLength);
			portletURL.setParameter('subscriptionType', subscriptionType);
			portletURL.setParameter('maxSubscriptionCycles', maxSubscriptionCycles);

			window.location.replace(portletURL.toString());
		},
		['liferay-portlet-url']
	);

	Liferay.provide(
		window,
		'<portlet:namespace />selectDeliverySubscriptionType',
		function() {
			var A = AUI();

			var deliverySubscriptionEnabled = A.one(
				'#<portlet:namespace />deliverySubscriptionEnabled'
			).attr('checked');
			var deliverySubscriptionLength = A.one(
				'#<portlet:namespace />deliverySubscriptionLength'
			).val();
			var deliverySubscriptionType = A.one(
				'#<portlet:namespace />deliverySubscriptionType'
			).val();
			var deliveryMaxSubscriptionCycles = A.one(
				'#<portlet:namespace />deliveryMaxSubscriptionCycles'
			).val();

			var portletURL = new Liferay.PortletURL.createURL(
				'<%= currentURLObj %>'
			);

			portletURL.setParameter(
				'deliverySubscriptionEnabled',
				deliverySubscriptionEnabled
			);
			portletURL.setParameter(
				'deliverySubscriptionLength',
				deliverySubscriptionLength
			);
			portletURL.setParameter(
				'deliverySubscriptionType',
				deliverySubscriptionType
			);
			portletURL.setParameter(
				'deliveryMaxSubscriptionCycles',
				deliveryMaxSubscriptionCycles
			);

			window.location.replace(portletURL.toString());
		},
		['liferay-portlet-url']
	);
</aui:script>

<aui:script use="liferay-form">
	A.one('#<portlet:namespace />neverEnds').on('change', function(event) {
		var formValidator = Liferay.Form.get('<portlet:namespace />fm')
			.formValidator;

		formValidator.validateField('<portlet:namespace />maxSubscriptionCycles');
	});
</aui:script>

<aui:script use="liferay-form">
	A.one('#<portlet:namespace />deliveryNeverEnds').on('change', function(event) {
		var formValidator = Liferay.Form.get('<portlet:namespace />fmDelivery')
			.formValidator;

		formValidator.validateField(
			'<portlet:namespace />deliveryMaxSubscriptionCycles'
		);
	});
</aui:script>

<aui:script use="aui-toggler">
	new A.Toggler({
		animated: true,
		content: '#<portlet:namespace />neverEndsContainer .never-ends-content',
		expanded: <%= ending %>,
		header: '#<portlet:namespace />neverEndsContainer .never-ends-header',
		on: {
			animatingChange: function(event) {
				var instance = this;

				if (!instance.get('expanded')) {
					A.one('#<portlet:namespace />maxSubscriptionCycles').attr(
						'disabled',
						false
					);
				} else {
					A.one('#<portlet:namespace />maxSubscriptionCycles').attr(
						'disabled',
						true
					);
				}
			}
		}
	});
</aui:script>

<aui:script use="aui-toggler">
	new A.Toggler({
		animated: true,
		content:
			'#<portlet:namespace />deliveryNeverEndsContainer .never-ends-content',
		expanded: <%= deliveryEnding %>,
		header:
			'#<portlet:namespace />deliveryNeverEndsContainer .never-ends-header',
		on: {
			animatingChange: function(event) {
				var instance = this;

				if (!instance.get('expanded')) {
					A.one(
						'#<portlet:namespace />deliveryMaxSubscriptionCycles'
					).attr('disabled', false);
				} else {
					A.one(
						'#<portlet:namespace />deliveryMaxSubscriptionCycles'
					).attr('disabled', true);
				}
			}
		}
	});
</aui:script>