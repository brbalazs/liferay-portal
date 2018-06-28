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

<%@ include file="/discount/init.jsp" %>

<%
CommerceDiscountValue commerceDiscountValue = (CommerceDiscountValue)request.getAttribute("liferay-commerce:discount:commerceDiscountValue");
boolean displayDiscountLevels = (boolean)request.getAttribute("liferay-commerce:discount:displayDiscountLevels");
%>

<c:choose>
	<c:when test="<%= commerceDiscountValue != null %>">

		<%
		BigDecimal[] percentages = commerceDiscountValue.getPercentages();
		%>

		<span class="discount-amount"><%= commerceDiscountValue.getDiscountAmount().toPlainString() %></span>

		<c:choose>
			<c:when test="<%= displayDiscountLevels && !ArrayUtil.isEmpty(percentages) %>">
				<span class="discount-percentage-level1"><%= percentages[0].toPlainString() %></span>

				<c:if test="<%= percentages[1].compareTo(BigDecimal.ZERO) > 0 %>">
					<span class="discount-percentage-level2"><%= percentages[1].toPlainString() %></span>
				</c:if>

				<c:if test="<%= percentages[2].compareTo(BigDecimal.ZERO) > 0 %>">
					<span class="discount-percentage-level3"><%= percentages[2].toPlainString() %></span>
				</c:if>
			</c:when>
			<c:otherwise>
				<span class="discount-percentage"><%= commerceDiscountValue.getDiscountPercentage().toPlainString() %></span>
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
	</c:otherwise>
</c:choose>