<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/alloy_mvc/jsp/testray/views/init.jsp" %>

<div class="case-summary-container">
	<h1><liferay-ui:message key="cases" /></h1>

	<hr noshade size="5" />

	<c:forEach items="${testrayCaseComposites}" var="testrayCaseComposite">
		<div class="testray-card testray-card-metadata-panel">
			<liferay-ui:panel-container
				cssClass="metadata-panel"
			>
				<liferay-ui:panel
					collapsible="${false}"
					title="${testrayCaseComposite.name}"
				>
					<dl class="data-list dl-horizontal">
						<dt>
							<liferay-ui:message key="project-name" />
						</dt>
						<dd>
							<c:out value="${testrayCaseComposite.testrayProjectName}" />
						</dd>
						<dt>
							<liferay-ui:message key="type" />
						</dt>
						<dd>
							<c:out value="${testrayCaseComposite.testrayCaseTypeName}" />
						</dd>
						<dt>
							<liferay-ui:message key="priority" />
						</dt>
						<dd>
							<c:out value="${testrayCaseComposite.priority}" />
						</dd>
						<dt>
							<liferay-ui:message key="team" />
						</dt>
						<dd>
							<c:out value="${testrayCaseComposite.testrayTeamName}" />
						</dd>
						<dt>
							<liferay-ui:message key="main-component" />
						</dt>
						<dd>
							<c:out value="${testrayCaseComposite.testrayComponentName}" />
						</dd>

						<c:if test="${!PortalPropsValues.TESTRAY_SIMPLIFIED_CASES}">
							<dt>
								<liferay-ui:message key="subcomponents" />
							</dt>
							<dd>
								<c:out value="${testrayCaseComposite.estrayComponentNames}" />
							</dd>
						</c:if>

						<dt>
							<liferay-ui:message key="description" />
						</dt>
						<dd>
							<testray:rich-output
								type="${testrayCaseComposite.descriptionType}"
								value="${testrayCaseComposite.description}"
							/>
						</dd>
						<dt>
							<liferay-ui:message key="estimated-duration" />
						</dt>
						<dd>
							${(testrayCaseComposite.estimatedDuration > 0) ? testrayCaseComposite.estimatedDuration : StringPool.DASH}
						</dd>
						<dt>
							<liferay-ui:message key="steps" />
						</dt>
						<dd>
							<testray:rich-output
								type="${testrayCaseComposite.stepsType}"
								value="${testrayCaseComposite.steps}"
							/>
						</dd>
						<dt>
							<liferay-ui:message key="date-last-modified" />
						</dt>
						<dd>
							<testray:date
								value="${testrayCaseComposite.modifiedDate}"
							/>
						</dd>
						<dt>
							<liferay-ui:message key="all-issues-found" />
						</dt>
						<dd>
							<c:out value="${testrayCaseComposite.testrayIssueNames}" />
						</dd>
						<dt>
							<liferay-ui:message key="requirements" />
						</dt>
						<dd>
							<table class="table table-bordered">
								<tr>
									<th><liferay-ui:message key="key" /></th>
									<th><liferay-ui:message key="link" /></th>
									<th><liferay-ui:message key="summary" /></th>
								</tr>

								<c:forEach items="${testrayCaseComposite.testrayRequirements}" var="testrayRequirement">
									<tr>
										<td>${testrayRequirement.key}</td>
										<td><aui:a href="${testrayRequirement.linkURL}" label="${fn:escapeXml(testrayRequirement.linkTitle)}" target="_blank" /></td>
										<td>${testrayRequirement.summary}</td>
									</tr>
								</c:forEach>
							</table>
						</dd>
					</dl>
				</liferay-ui:panel>
			</liferay-ui:panel-container>
		</div>
	</c:forEach>
</div>

<div class="case-summary-container">
	<h1><liferay-ui:message key="associated-requirements" /></h1>

	<hr noshade size="5" />

	<c:forEach items="${testrayRequirementComposites}" var="testrayRequirementComposite">
		<div class="testray-card testray-card-metadata-panel">
			<liferay-ui:panel-container
				cssClass="metadata-panel"
			>
				<liferay-ui:panel
					collapsible="${false}"
					title="${testrayRequirementComposite.key}"
				>
					<dl class="data-list dl-horizontal">
						<dt>
							<liferay-ui:message key="link" />
						</dt>
						<dd>
							<aui:a href="${testrayRequirementComposite.linkURL}" label="${fn:escapeXml(testrayRequirementComposite.linkTitle)}" target="_blank" />
						</dd>
						<dt>
							<liferay-ui:message key="team" />
						</dt>
						<dd>
							<c:out value="${testrayRequirementComposite.testrayTeamName}" />
						</dd>
						<dt>
							<liferay-ui:message key="component" />
						</dt>
						<dd>
							<c:out value="${testrayRequirementComposite.testrayComponentName}" />
						</dd>
						<dt>
							<liferay-ui:message key="jira-components" />
						</dt>
						<dd>
							<c:out value="${testrayRequirementComposite.components}" />
						</dd>
						<dt>
							<liferay-ui:message key="summary" />
						</dt>
						<dd>
							<c:out value="${testrayRequirementComposite.summary}" />
						</dd>
						<dt>
							<liferay-ui:message key="description" />
						</dt>
						<dd>
							<testray:rich-output
								type="${testrayRequirementComposite.descriptionType}"
								value="${testrayRequirementComposite.description}"
							/>
						</dd>
					</dl>
				</liferay-ui:panel>
			</liferay-ui:panel-container>
		</div>
	</c:forEach>
</div>