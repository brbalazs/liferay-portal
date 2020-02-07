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

<%@ include file="/timeline/init.jsp" %>

<ul class="mb-0 timeline">

	<%
	for (Map<String, String> value : elements) {
	%>

		<li class="timeline-item">
			<div class="panel panel-secondary">
				<div class="timeline-increment">
					<span class="timeline-icon"></span>
				</div>

				<div class="panel-body">
					<div class="mb-2 row">
						<div class="col">
							<h4 class="mb-0"><%= value.get("title") %></h4>
						</div>

						<div class="col-auto">
							<small><%= value.get("date") %></small>
						</div>
					</div>

					<small><%= value.get("description") %></small>
				</div>
			</div>
		</li>

	<%
	}
	%>

</ul>