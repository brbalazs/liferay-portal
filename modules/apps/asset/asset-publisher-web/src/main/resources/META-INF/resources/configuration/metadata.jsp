<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<aui:input name="preferences--metadataFields--" type="hidden" />

<%

// Left list

List leftList = new ArrayList();

String[] metadataFields = assetPublisherDisplayContext.getMetadataFields();

for (int i = 0; i < metadataFields.length; i++) {
	String folderColumn = metadataFields[i];

	leftList.add(new KeyValuePair(folderColumn, LanguageUtil.get(request, folderColumn)));
}

// Right list

List rightList = new ArrayList();

Arrays.sort(metadataFields);

String[] allMetadataFields = {"create-date", "modified-date", "publish-date", "expiration-date", "priority", "author", "view-count", "categories", "tags"};

for (String folderColumn : allMetadataFields) {
	if (Arrays.binarySearch(metadataFields, folderColumn) < 0) {
		rightList.add(new KeyValuePair(folderColumn, LanguageUtil.get(request, folderColumn)));
	}
}

rightList = ListUtil.sort(rightList, new KeyValuePairComparator(false, true));
%>

<liferay-ui:input-move-boxes
	leftBoxName="currentMetadataFields"
	leftList="<%= leftList %>"
	leftReorder="<%= Boolean.TRUE.toString() %>"
	leftTitle="current"
	rightBoxName="availableMetadataFields"
	rightList="<%= rightList %>"
	rightTitle="available"
/>