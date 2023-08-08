/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.filter.expression;

import com.liferay.osb.asah.common.filter.expression.parser.FilterExpressionBaseVisitor;
import com.liferay.osb.asah.common.filter.expression.parser.FilterExpressionParser;
import com.liferay.osb.asah.common.util.StringUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Marcellus Tavares
 */
public class FilterExpressionReferencedObjectsVisitor
	extends FilterExpressionBaseVisitor<Object> {

	public FilterExpressionReferencedObjectsVisitor() {
		for (String referencedObjectName : _REFERENCED_OBJECT_NAMES) {
			_referencedObjectIds.put(referencedObjectName, new HashSet<>());
		}
	}

	public Map<String, Set<String>> getReferencedObjectIds() {
		return _referencedObjectIds;
	}

	@Override
	public Object visitEqualsExpression(
		FilterExpressionParser.EqualsExpressionContext
			equalsExpressionContext) {

		Token startToken = equalsExpressionContext.start;
		Token stopToken = equalsExpressionContext.stop;

		_setReferencedObjectIds(
			startToken.getText(),
			StringUtil.unquoteAndDecodeInnerQuotes(stopToken.getText()));

		_visitChild(equalsExpressionContext, 0);

		return null;
	}

	@Override
	public Object visitExpression(
		FilterExpressionParser.ExpressionContext expressionContext) {

		FilterExpressionParser.LogicalOrExpressionContext
			logicalOrExpressionContext =
				expressionContext.logicalOrExpression();

		logicalOrExpressionContext.accept(this);

		return null;
	}

	@Override
	public Object visitFilterByCountExpression(
		FilterExpressionParser.FilterByCountExpressionContext
			filterByCountExpressionContext) {

		String filterString = _parseFilterStringExpression(
			filterByCountExpressionContext.filter);

		_filterTypeStack.push(
			FilterExpression.FilterType.of(
				filterByCountExpressionContext.filterType.getText()));

		new FilterExpression(
			filterString.substring(1, filterString.length() - 1), this);

		_filterTypeStack.pop();

		return null;
	}

	public Object visitFilterExpression(
		FilterExpressionParser.FilterExpressionContext
			filterExpressionContext) {

		String filterString = _parseFilterStringExpression(
			filterExpressionContext.filter);

		_filterTypeStack.push(
			FilterExpression.FilterType.of(
				filterExpressionContext.filterType.getText()));

		new FilterExpression(
			filterString.substring(1, filterString.length() - 1), this);

		_filterTypeStack.pop();

		return null;
	}

	@Override
	public Object visitIdentifier(
		FilterExpressionParser.IdentifierContext identifierContext) {

		String fieldName = identifierContext.getText();

		if (StringUtils.contains(fieldName, "/")) {
			String[] identifierParts = StringUtils.split(fieldName, "/");

			Set<String> referencedFieldMappingFieldNames =
				_referencedObjectIds.get("referencedFieldMappingFieldNames");

			if (StringUtils.equals(identifierParts[0], "custom")) {
				referencedFieldMappingFieldNames.add(identifierParts[1]);
			}
			else if (StringUtils.equals(identifierParts[0], "demographics")) {
				referencedFieldMappingFieldNames.add(
					_demographicFieldNames.getOrDefault(
						identifierParts[1], identifierParts[1]));
			}
		}

		return null;
	}

	@Override
	public Object visitNotEqualsExpression(
		FilterExpressionParser.NotEqualsExpressionContext
			notEqualsExpressionContext) {

		Token startToken = notEqualsExpressionContext.start;
		Token stopToken = notEqualsExpressionContext.stop;

		_setReferencedObjectIds(
			startToken.getText(),
			StringUtil.unquoteAndDecodeInnerQuotes(stopToken.getText()));

		_visitChild(notEqualsExpressionContext, 0);

		return null;
	}

	private String _parseFilterStringExpression(Token filterToken) {
		String filterString = filterToken.getText();

		filterString = filterString.replaceAll(",''", ", '");
		filterString = filterString.replaceAll("'',", "',");
		filterString = filterString.replaceAll("\\s''", " '");
		filterString = filterString.replaceAll("''\\s", "' ");
		filterString = filterString.replaceAll("''\\)", "')");

		return filterString;
	}

	private FilterExpression.FilterType _peekFilterType() {
		if (_filterTypeStack.isEmpty()) {
			return null;
		}

		return _filterTypeStack.peek();
	}

	private void _setReferencedObjectIds(String fieldName, String value) {
		if (fieldName.startsWith("activityKey")) {
			String[] valueParts = value.split("#");

			Set<String> referencedAssetIds = _referencedObjectIds.get(
				"referencedAssetIds");

			referencedAssetIds.add(valueParts[2]);
		}
		else if (fieldName.startsWith("groupIds")) {
			Set<String> referencedGroupIds = _referencedObjectIds.get(
				"referencedGroupIds");

			referencedGroupIds.add(value);
		}
		else if (fieldName.startsWith("roleIds")) {
			Set<String> referencedRoleIds = _referencedObjectIds.get(
				"referencedRoleIds");

			referencedRoleIds.add(value);
		}
		else if (fieldName.startsWith("teamIds")) {
			Set<String> referencedTeamIds = _referencedObjectIds.get(
				"referencedTeamIds");

			referencedTeamIds.add(value);
		}
		else if (fieldName.startsWith("userGroupIds")) {
			Set<String> referencedUserGroupIds = _referencedObjectIds.get(
				"referencedUserGroupIds");

			referencedUserGroupIds.add(value);
		}
		else if (fieldName.startsWith("userId")) {
			Set<String> referencedUserIds = _referencedObjectIds.get(
				"referencedUserIds");

			referencedUserIds.add(value);
		}
		else if (Objects.equals(
					_peekFilterType(),
					FilterExpression.FilterType.ORGANIZATIONS) &&
				 (fieldName.equals("id") || fieldName.equals("parentId"))) {

			Set<String> referencedOrganizationIds = _referencedObjectIds.get(
				"referencedOrganizationIds");

			referencedOrganizationIds.add(value);
		}
	}

	private void _visitChild(
		ParserRuleContext parserRuleContext, int childIndex) {

		ParseTree parseTree = parserRuleContext.getChild(childIndex);

		parseTree.accept(this);
	}

	private static final String[] _REFERENCED_OBJECT_NAMES = {
		"referencedAssetIds", "referencedDataSourceIds",
		"referencedFieldMappingFieldNames", "referencedGroupIds",
		"referencedOrganizationIds", "referencedRoleIds", "referencedTeamIds",
		"referencedUserGroupIds", "referencedUserIds"
	};

	private static final Map<String, String> _demographicFieldNames =
		new HashMap<String, String>() {
			{
				put("additionalName", "middleName");
				put("birthDate", "birthday");
				put("email", "emailAddress");
				put("familyName", "lastName");
				put("givenName", "firstName");
			}
		};

	private final Stack<FilterExpression.FilterType> _filterTypeStack =
		new Stack<>();
	private final Map<String, Set<String>> _referencedObjectIds =
		new HashMap<>();

}