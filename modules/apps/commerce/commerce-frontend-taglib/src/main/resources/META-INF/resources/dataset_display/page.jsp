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

<%@ include file="/dataset_display/init.jsp" %>

<div class="table-root" id="<%= containerId %>">
	<span aria-hidden="true" class="loading-animation my-7"></span>
</div>

<aui:script require="commerce-frontend-js/components/dataset_display/entry.es as datasetDisplay">
	datasetDisplay.default('<%= containerId %>', '<%= containerId %>', {
		views: <%= jsonSerializer.serializeDeep(clayDataSetDisplayViewsContext) %>,
		apiUrl: '<%= dataSetAPI %>',
		creationMenuItems: <%= jsonSerializer.serializeDeep(clayCreationMenu.getClayCreationMenuItems()) %>,
		formId: '<%= formId %>',
		dataProviderKey: '<%= dataProviderKey %>',
		id: '<%= id %>',
		items: <%= jsonSerializer.serializeDeep(items) %>,
		showPagination: <%= showPagination %>,
		showManagementBar: <%= showManagementBar %>,
		pagination: {
			deltas: <%= jsonSerializer.serializeDeep(paginationEntries) %>,
			initialDelta: <%= itemsPerPage %>,
			initialPageNumber: <%= pageNumber %>,
			initialTotalItems: <%= totalItems %>
		},
		namespace: '<%= namespace %>',
		portletURL: '<%= portletURL %>',
		selectedItemsKey: '<%= selectedItemsKey %>',
		selectionType: '<%= selectionType %>',
		spritemap: '/o/minium-theme/images/lexicon/icons.svg',
		style: '<%= style %>'
	});
</aui:script>