/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.filter.expression;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.date.dog.util.TimeZoneDogUtil;
import com.liferay.osb.asah.common.filter.expression.parser.FilterExpressionBaseVisitor;
import com.liferay.osb.asah.common.filter.expression.parser.FilterExpressionParser;
import com.liferay.osb.asah.common.util.PostgresDialectThreadLocal;
import com.liferay.osb.asah.common.util.SetUtil;
import com.liferay.osb.asah.common.util.StringUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Param;
import org.jooq.Record1;
import org.jooq.SelectConditionStep;
import org.jooq.SelectJoinStep;
import org.jooq.impl.DSL;

/**
 * @author Marcellus Tavares
 * @author Ivica Cardic
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class FilterExpressionConditionVisitor
	extends FilterExpressionBaseVisitor<Object> {

	public FilterExpressionConditionVisitor(
		Long channelId, FilterExpression.FilterType filterType) {

		_channelId = channelId;
		_filterType = filterType;

		if ((filterType != null) && _tableReferences.containsKey(filterType)) {
			_referencedTableNames.add(_tableReferences.get(filterType));
		}
	}

	public Set<String> getReferencedTableNames() {
		return _referencedTableNames;
	}

	@Override
	public Object visitAndExpression(
		FilterExpressionParser.AndExpressionContext andExpressionContext) {

		Condition leftCondition = _visitChild(andExpressionContext, 0);
		Condition rightCondition = _visitChild(andExpressionContext, 2);

		return leftCondition.and(rightCondition);
	}

	@Override
	public Object visitBooleanLiteral(
		FilterExpressionParser.BooleanLiteralContext booleanLiteralContext) {

		return DSL.val(Boolean.parseBoolean(booleanLiteralContext.getText()));
	}

	@Override
	public Object visitBooleanParenthesis(
		FilterExpressionParser.BooleanParenthesisContext
			booleanParenthesisContext) {

		return _visitChild(booleanParenthesisContext, 1);
	}

	@Override
	public Object visitEqualsExpression(
		FilterExpressionParser.EqualsExpressionContext
			equalsExpressionContext) {

		Token startToken = equalsExpressionContext.start;
		Token stopToken = equalsExpressionContext.stop;

		String fieldName = startToken.getText();
		String value = StringUtil.unquoteAndDecodeInnerQuotes(
			stopToken.getText());

		if (Objects.equals(
				_filterType, FilterExpression.FilterType.INDIVIDUALS)) {

			if (Objects.equals(fieldName, "dataSourceId")) {
				_referencedTableNames.add("IdentityActivity");

				return DSL.field(
					"IdentityActivity.dataSourceId"
				).eq(
					Long.parseLong(value)
				);
			}
			else if (Objects.equals(
						fieldName, "dataSourceIndividualPKs/individualPKs")) {

				return DSL.field(
					"Identity.id"
				).eq(
					value
				);
			}
			else if (Objects.equals(fieldName, "userId")) {
				_referencedTableNames.add("User");
			}
		}

		if (Objects.equals(
				_filterType, FilterExpression.FilterType.ORGANIZATIONS)) {

			return _visitOrganizationExpression(fieldName, "eq", value);
		}

		if (Objects.equals(_filterType, FilterExpression.FilterType.SESSIONS)) {
			if (Objects.equals(fieldName, "context/referrer")) {
				return DSL.condition(
					String.format("'%s' IN UNNEST(Session.referrers)", value));
			}

			if (Objects.equals(fieldName, "context/url")) {
				return DSL.condition(
					String.format("'%s' IN UNNEST(Session.urls)", value));
			}
		}

		if (fieldName.startsWith("custom/")) {
			String[] identifierParts = StringUtils.split(fieldName, "/");

			return _getCustomFieldCondition(identifierParts[1], "eq", value);
		}
		else if (fieldName.startsWith("demographics/")) {
			String[] identifierParts = StringUtils.split(fieldName, "/");

			return _getDemographicsFieldCondition(
				equalsExpressionContext, identifierParts[1], "eq", value);
		}

		Field leftField = _getLeftField(equalsExpressionContext);
		Field rightField = _getRightField(equalsExpressionContext);

		if (rightField == null) {
			return leftField.isNull();
		}

		if (DateUtil.isValidPatternShort(value) &&
			!fieldName.equalsIgnoreCase("cast")) {

			leftField = _getDateField(leftField);

			Param<String> param = (Param<String>)rightField;

			rightField = DSL.function(
				"DATE", Date.class, DSL.val(param.getValue()));
		}

		if ((Objects.equals(
				leftField.getName(), "IdentityActivity.channelId") ||
			 Objects.equals(leftField.getName(), "Membership.segmentId")) &&
			(rightField instanceof Param)) {

			Param<String> param = (Param<String>)rightField;

			rightField = DSL.val(Long.parseLong(param.getValue()));
		}

		if (Objects.equals(fieldName, "interestName")) {
			Param<String> param = (Param<String>)rightField;

			rightField = DSL.lower(
				DSL.trim(DSL.replace(DSL.val(param.getValue()), "\n", "")));

			return DSL.lower(
				DSL.trim(
					DSL.replace(DSL.field("keyword", String.class), "\n", ""))
			).eq(
				rightField
			);
		}

		return leftField.eq(rightField);
	}

	@Override
	public Object visitExpression(
		FilterExpressionParser.ExpressionContext expressionContext) {

		FilterExpressionParser.LogicalOrExpressionContext
			logicalOrExpressionContext =
				expressionContext.logicalOrExpression();

		return logicalOrExpressionContext.accept(this);
	}

	@Override
	public Object visitFilterByCountExpression(
		FilterExpressionParser.FilterByCountExpressionContext
			filterByCountExpressionContext) {

		String filterString = _parseFilterStringExpression(
			filterByCountExpressionContext.filter);

		FilterExpression filterExpression = new FilterExpression(
			_channelId, filterString.substring(1, filterString.length() - 1),
			FilterExpression.FilterType.of(
				filterByCountExpressionContext.filterType.getText()));

		_referencedTableNames.addAll(
			filterExpression.getReferencedTableNames());

		String operator = filterByCountExpressionContext.operator.getText();
		Integer value = Integer.parseInt(
			filterByCountExpressionContext.value.getText());
		Field userIdField = DSL.field("Event.userId");

		Condition havingCondition = null;

		if (Objects.equals(StringUtil.unquote(operator), "ge")) {
			havingCondition = DSL.count(
				userIdField
			).ge(
				value
			);
		}
		else {
			havingCondition = DSL.count(
				userIdField
			).le(
				value
			);
		}

		Field identityIdField = DSL.field("Identity.id");
		Field individualIdField = DSL.field("Individual.id");

		return DSL.or(
			identityIdField.in(
				DSL.select(
					userIdField
				).from(
					DSL.table(
						"BQEvent"
					).as(
						"Event"
					)
				).where(
					filterExpression.getCondition()
				).groupBy(
					userIdField
				).having(
					havingCondition
				)),
			individualIdField.in(
				DSL.selectDistinct(
					DSL.field("Identity.individualId")
				).from(
					DSL.table(
						"BQEvent"
					).as(
						"Event"
					).join(
						DSL.table(
							"BQIdentity"
						).as(
							"Identity"
						)
					).on(
						DSL.field(
							"Event.userId"
						).eq(
							DSL.field("Identity.id")
						)
					)
				).where(
					DSL.and(
						filterExpression.getCondition(),
						DSL.field(
							"Identity.individualId"
						).isNotNull())
				).groupBy(
					userIdField, DSL.field("Identity.individualId")
				).having(
					havingCondition
				)));
	}

	public Object visitFilterExpression(
		FilterExpressionParser.FilterExpressionContext
			filterExpressionContext) {

		String filterString = _parseFilterStringExpression(
			filterExpressionContext.filter);

		FilterExpression filterExpression = new FilterExpression(
			_channelId, filterString.substring(1, filterString.length() - 1),
			FilterExpression.FilterType.of(
				filterExpressionContext.filterType.getText()));

		_referencedTableNames.addAll(
			filterExpression.getReferencedTableNames());

		return filterExpression.getCondition();
	}

	@Override
	public Object visitFloatingPointLiteral(
		FilterExpressionParser.FloatingPointLiteralContext
			floatingPointLiteralContext) {

		String doubleString = floatingPointLiteralContext.getText();

		return DSL.val(Double.parseDouble(doubleString));
	}

	@Override
	public Object visitFunctionCallExpression(
		FilterExpressionParser.FunctionCallExpressionContext
			functionCallExpressionContext) {

		Token functionNameToken = functionCallExpressionContext.functionName;

		String functionName = functionNameToken.getText();

		List<Object> parameters = _visitChild(functionCallExpressionContext, 2);

		Field field = (Field)parameters.get(0);

		if (functionName.equalsIgnoreCase("cast")) {
			Param param = (Param)parameters.get(1);

			String type = String.valueOf(param.getValue());

			String attributeType = _attributeTypes.get(
				StringUtils.lowerCase(type));

			if (attributeType == null) {
				return new FilterExpressionParserException(
					"Invalid type " + type);
			}

			return DSL.field(
				String.format("SAFE_CAST({0} AS %s)", attributeType), field);
		}

		String fieldName = field.getName();

		String qualifiedFieldName = fieldName;

		if (StringUtils.startsWith(fieldName, "ExpandoValue.")) {
			String[] parts = fieldName.split("\\.", 2);

			qualifiedFieldName = parts[1];

			if (Objects.equals(
					_filterType, FilterExpression.FilterType.INDIVIDUALS)) {

				String alias = "IndividualFields_" + qualifiedFieldName;

				_referencedTableNames.add(alias);

				field = DSL.field(alias + ".value");
			}
			else {
				field = DSL.field(
					"ExpandoValue_" + qualifiedFieldName + ".value");
			}
		}

		Condition condition = null;

		if (functionName.equalsIgnoreCase("between")) {
			Param param1 = (Param)parameters.get(1);

			Object object1 = param1.getValue();

			Param param2 = (Param)parameters.get(2);

			Object object2 = param2.getValue();

			if ((object1 instanceof String) &&
				DateUtil.isValidPatternShort((String)object1) &&
				(object2 instanceof String) &&
				DateUtil.isValidPatternShort((String)object2)) {

				field = _getDateField(field);

				object1 = DSL.function("DATE", Date.class, DSL.val(object1));
				object2 = DSL.function("DATE", Date.class, DSL.val(object2));
			}

			condition = field.between(object1, object2);
		}
		else if (functionName.equalsIgnoreCase("contains")) {
			Param param = (Param)parameters.get(1);

			String value = String.valueOf(param.getValue());

			if (StringUtils.startsWith(fieldName, "ExpandoValue.")) {
				condition = _getCustomFieldCondition(
					fieldName, "contains", value);
			}
			else {
				if (Objects.equals(fieldName, "Session.referrers")) {
					_referencedTableNames.add("SessionReferrers");

					field = DSL.field("SessionReferrer");
				}
				else if (Objects.equals(fieldName, "Session.urls")) {
					_referencedTableNames.add("SessionUrls");

					field = DSL.field("SessionUrl");
				}

				condition = DSL.lower(
					field
				).like(
					DSL.inline("%" + StringUtils.lowerCase(value) + "%")
				);
			}
		}
		else if (functionName.equalsIgnoreCase("endsWith")) {
			Param param = (Param)parameters.get(1);

			condition = field.similarTo("%" + param.getValue());
		}
		else if (functionName.equalsIgnoreCase("isInterested")) {
			Param param = (Param)parameters.get(1);

			condition = _getIsInterestedCondition(
				StringUtil.unquote((String)param.getValue()));
		}
		else if (functionName.equalsIgnoreCase("isMember")) {
			Param param = (Param)parameters.get(1);

			condition = _getIsMemberCondition(
				(String)param.getValue(), fieldName.replace("Individual.", ""));
		}
		else if (functionName.equalsIgnoreCase("notContains")) {
			Param param = (Param)parameters.get(1);

			String value = String.valueOf(param.getValue());

			if (Objects.equals(fieldName, "Session.referrers")) {
				_referencedTableNames.add("SessionReferrers");

				field = DSL.field("SessionReferrer");
			}
			else if (Objects.equals(fieldName, "Session.urls")) {
				_referencedTableNames.add("SessionUrls");

				field = DSL.field("SessionUrl");
			}

			condition = DSL.not(
				DSL.condition(
					String.format(
						"LOWER(%s) LIKE '%s'", field,
						"%" + StringUtils.lowerCase(value) + "%")));
		}
		else if (functionName.equalsIgnoreCase("sha256Hex")) {
			return DSL.field(String.format("TO_HEX(SHA256(%s))", fieldName));
		}
		else if (functionName.equalsIgnoreCase("similarTo")) {
			Param param = (Param)parameters.get(1);

			condition = field.similarTo(
				StringUtils.replaceChars(
					String.valueOf(param.getValue()), ".*", "_%"));
		}
		else if (functionName.equalsIgnoreCase("startsWith")) {
			Param param = (Param)parameters.get(1);

			condition = field.similarTo(param.getValue() + "%");
		}
		else {
			throw new FilterExpressionParserException(
				"Invalid string function: " + functionName);
		}

		if (!functionName.equalsIgnoreCase("contains")) {
			if (StringUtils.startsWith(field.getName(), "ExpandoValue_")) {
				condition = condition.and(
					DSL.field(
						"ExpandoValue_" + qualifiedFieldName + ".fieldName"
					).eq(
						qualifiedFieldName
					));
			}
			else if (StringUtils.startsWith(
						field.getName(), "IndividualFields_")) {

				String curFieldName = field.getName();

				String[] parts = curFieldName.split("\\.", 2);

				_referencedTableNames.add("ExpandoValue");

				condition = condition.and(
					DSL.field(
						parts[0] + ".name"
					).eq(
						qualifiedFieldName
					));
			}
		}

		if (Objects.equals(
				_filterType, FilterExpression.FilterType.ORGANIZATIONS)) {

			if (StringUtils.startsWith(fieldName, "ExpandoValue.")) {
				return _getIndividualIdsInOrganizationCondition(
					condition, "ExpandoValue_" + qualifiedFieldName);
			}

			return _getIndividualIdsInOrganizationCondition(condition, null);
		}

		return condition;
	}

	@Override
	public Object visitFunctionParameters(
		FilterExpressionParser.FunctionParametersContext
			functionParametersContext) {

		List<Object> parameters = new ArrayList<>();

		for (int i = 0; i < functionParametersContext.getChildCount(); i++) {
			ParseTree childParseTree = functionParametersContext.getChild(i);

			if (childParseTree instanceof TerminalNode) {
				continue;
			}

			parameters.add(childParseTree.accept(this));
		}

		return parameters;
	}

	@Override
	public Object visitGreaterThanExpression(
		FilterExpressionParser.GreaterThanExpressionContext
			greaterThanExpressionContext) {

		Token startToken = greaterThanExpressionContext.start;
		Token stopToken = greaterThanExpressionContext.stop;

		String fieldName = startToken.getText();
		String value = StringUtil.unquoteAndDecodeInnerQuotes(
			stopToken.getText());

		if (Objects.equals(
				_filterType, FilterExpression.FilterType.ORGANIZATIONS)) {

			return _visitOrganizationExpression(fieldName, "gt", value);
		}

		if (fieldName.startsWith("custom/")) {
			String[] identifierParts = StringUtils.split(fieldName, "/");

			return _getCustomFieldCondition(identifierParts[1], "gt", value);
		}

		if (fieldName.startsWith("demographics/")) {
			String[] identifierParts = StringUtils.split(fieldName, "/");

			return _getDemographicsFieldCondition(
				greaterThanExpressionContext, identifierParts[1], "gt", value);
		}

		Field leftField = _getLeftField(greaterThanExpressionContext);
		Field rightField = _getRightField(greaterThanExpressionContext);

		if (DateUtil.isValidPatternShort(value) &&
			!fieldName.equalsIgnoreCase("cast")) {

			leftField = _getDateField(leftField);

			Param<String> param = (Param<String>)rightField;

			return leftField.gt(
				DSL.function("DATE", Date.class, DSL.val(param.getValue())));
		}

		return leftField.gt(rightField);
	}

	@Override
	public Object visitGreaterThanOrEqualsExpression(
		FilterExpressionParser.GreaterThanOrEqualsExpressionContext
			greaterThanOrEqualsExpressionContext) {

		Token startToken = greaterThanOrEqualsExpressionContext.start;
		Token stopToken = greaterThanOrEqualsExpressionContext.stop;

		String fieldName = startToken.getText();
		String value = StringUtil.unquoteAndDecodeInnerQuotes(
			stopToken.getText());

		if (Objects.equals(
				_filterType, FilterExpression.FilterType.ORGANIZATIONS)) {

			return _visitOrganizationExpression(fieldName, "ge", value);
		}

		if (fieldName.startsWith("custom/")) {
			String[] identifierParts = StringUtils.split(fieldName, "/");

			return _getCustomFieldCondition(identifierParts[1], "ge", value);
		}

		if (fieldName.startsWith("demographics/")) {
			String[] identifierParts = StringUtils.split(fieldName, "/");

			return _getDemographicsFieldCondition(
				greaterThanOrEqualsExpressionContext, identifierParts[1], "ge",
				value);
		}

		Field leftField = _getLeftField(greaterThanOrEqualsExpressionContext);
		Field rightField = _getRightField(greaterThanOrEqualsExpressionContext);

		if (DateUtil.isValidPatternShort(value) &&
			!fieldName.equalsIgnoreCase("cast")) {

			leftField = _getDateField(leftField);

			Param<String> param = (Param<String>)rightField;

			return leftField.ge(
				DSL.function("DATE", Date.class, DSL.val(param.getValue())));
		}

		return leftField.ge(rightField);
	}

	@Override
	public Object visitIdentifier(
		FilterExpressionParser.IdentifierContext identifierContext) {

		String fieldName = identifierContext.getText();

		if (_fieldMappers.containsKey(fieldName)) {
			fieldName = _fieldMappers.get(fieldName);

			if (fieldName.contains(".")) {
				_checkReferencedTables(fieldName);

				return DSL.field(fieldName);
			}
		}

		if (StringUtils.contains(fieldName, "/")) {
			String[] identifierParts = StringUtils.split(fieldName, "/");

			if (Objects.equals(identifierParts[0], "custom")) {
				return DSL.field("ExpandoValue." + identifierParts[1]);
			}

			fieldName = identifierParts[1];
		}

		if (_filterType == null) {
			return DSL.field(_getTableNamespace() + fieldName);
		}

		String qualifiedFieldName = _fieldMappers.getOrDefault(
			_filterType.getName() + "." + fieldName,
			_getTableNamespace() + fieldName);

		_checkReferencedTables(qualifiedFieldName);

		return DSL.field(qualifiedFieldName);
	}

	@Override
	public Object visitIntegerLiteral(
		FilterExpressionParser.IntegerLiteralContext integerLiteralContext) {

		String longString = integerLiteralContext.getText();

		return DSL.val(Long.parseLong(longString));
	}

	@Override
	public Object visitLessThanExpression(
		FilterExpressionParser.LessThanExpressionContext
			lessThanExpressionContext) {

		Token startToken = lessThanExpressionContext.start;
		Token stopToken = lessThanExpressionContext.stop;

		String fieldName = startToken.getText();
		String value = StringUtil.unquoteAndDecodeInnerQuotes(
			stopToken.getText());

		if (Objects.equals(
				_filterType, FilterExpression.FilterType.ORGANIZATIONS)) {

			return _visitOrganizationExpression(fieldName, "lt", value);
		}

		if (fieldName.startsWith("custom/")) {
			String[] identifierParts = StringUtils.split(fieldName, "/");

			return _getCustomFieldCondition(identifierParts[1], "lt", value);
		}

		if (fieldName.startsWith("demographics/")) {
			String[] identifierParts = StringUtils.split(fieldName, "/");

			return _getDemographicsFieldCondition(
				lessThanExpressionContext, identifierParts[1], "lt", value);
		}

		Field leftField = _getLeftField(lessThanExpressionContext);
		Field rightField = _getRightField(lessThanExpressionContext);

		if (DateUtil.isValidPatternShort(value) &&
			!fieldName.equalsIgnoreCase("cast")) {

			leftField = _getDateField(leftField);

			Param<String> param = (Param<String>)rightField;

			return leftField.lt(
				DSL.function("DATE", Date.class, DSL.val(param.getValue())));
		}

		return leftField.lt(rightField);
	}

	@Override
	public Object visitLessThanOrEqualsExpression(
		FilterExpressionParser.LessThanOrEqualsExpressionContext
			lessThanOrEqualsExpressionContext) {

		Token startToken = lessThanOrEqualsExpressionContext.start;
		Token stopToken = lessThanOrEqualsExpressionContext.stop;

		String fieldName = startToken.getText();
		String value = StringUtil.unquoteAndDecodeInnerQuotes(
			stopToken.getText());

		if (Objects.equals(
				_filterType, FilterExpression.FilterType.ORGANIZATIONS)) {

			return _visitOrganizationExpression(fieldName, "le", value);
		}

		if (fieldName.startsWith("custom/")) {
			String[] identifierParts = StringUtils.split(fieldName, "/");

			return _getCustomFieldCondition(identifierParts[1], "le", value);
		}

		if (fieldName.startsWith("demographics/")) {
			String[] identifierParts = StringUtils.split(fieldName, "/");

			return _getDemographicsFieldCondition(
				lessThanOrEqualsExpressionContext, identifierParts[1], "le",
				value);
		}

		Field leftField = _getLeftField(lessThanOrEqualsExpressionContext);
		Field rightField = _getRightField(lessThanOrEqualsExpressionContext);

		if (DateUtil.isValidPatternShort(value) &&
			!fieldName.equalsIgnoreCase("cast")) {

			leftField = _getDateField(leftField);

			Param<String> param = (Param<String>)rightField;

			return leftField.le(
				DSL.function("DATE", Date.class, DSL.val(param.getValue())));
		}

		return leftField.le(_getRightField(lessThanOrEqualsExpressionContext));
	}

	@Override
	public Object visitNotEqualsExpression(
		FilterExpressionParser.NotEqualsExpressionContext
			notEqualsExpressionContext) {

		Token startToken = notEqualsExpressionContext.start;
		Token stopToken = notEqualsExpressionContext.stop;

		String fieldName = startToken.getText();
		String value = StringUtil.unquoteAndDecodeInnerQuotes(
			stopToken.getText());

		if (Objects.equals(
				_filterType, FilterExpression.FilterType.INDIVIDUALS) &&
			Objects.equals(fieldName, "userId")) {

			_referencedTableNames.add("User");
		}

		if (Objects.equals(
				_filterType, FilterExpression.FilterType.ORGANIZATIONS)) {

			return _visitOrganizationExpression(fieldName, "ne", value);
		}

		if (fieldName.startsWith("custom/")) {
			String[] identifierParts = StringUtils.split(fieldName, "/");

			return _getCustomFieldCondition(identifierParts[1], "ne", value);
		}
		else if (fieldName.startsWith("demographics/")) {
			String[] identifierParts = StringUtils.split(fieldName, "/");

			return _getDemographicsFieldCondition(
				notEqualsExpressionContext, identifierParts[1], "ne", value);
		}

		Field leftField = _getLeftField(notEqualsExpressionContext);
		Field rightField = _getRightField(notEqualsExpressionContext);

		if (rightField == null) {
			return leftField.isNotNull();
		}

		if (Objects.equals(leftField.getName(), "IdentityActivity.channelId") &&
			(rightField instanceof Param)) {

			Param<String> param = (Param<String>)rightField;

			rightField = DSL.val(Long.parseLong(param.getValue()));
		}

		if (DateUtil.isValidPatternShort(value) &&
			!fieldName.equalsIgnoreCase("cast")) {

			leftField = DSL.date(leftField);

			Param<String> param = (Param<String>)rightField;

			return leftField.ne(
				DSL.function("DATE", Date.class, DSL.val(param.getValue())));
		}

		return leftField.ne(rightField);
	}

	@Override
	public Object visitNotExpression(
		FilterExpressionParser.NotExpressionContext notExpressionContext) {

		Condition condition = _visitChild(notExpressionContext, 1);

		return DSL.not(condition);
	}

	@Override
	public Object visitNullLiteral(
		FilterExpressionParser.NullLiteralContext nullLiteralContext) {

		return null;
	}

	@Override
	public Object visitOrExpression(
		FilterExpressionParser.OrExpressionContext orExpressionContext) {

		Condition leftCondition = _visitChild(orExpressionContext, 0);
		Condition rightCondition = _visitChild(orExpressionContext, 2);

		return leftCondition.or(rightCondition);
	}

	@Override
	public Object visitStringLiteral(
		FilterExpressionParser.StringLiteralContext stringLiteralContext) {

		String string = stringLiteralContext.getText();

		return DSL.val(StringUtil.unquoteAndDecodeInnerQuotes(string));
	}

	private void _checkReferencedTables(String fieldName) {
		for (String tableName : _tableReferences.values()) {
			if (fieldName.startsWith(tableName)) {
				_referencedTableNames.add(tableName);

				break;
			}
		}
	}

	private Condition _getCustomFieldCondition(
		String fieldName, String operator, String value) {

		String alias = null;
		Condition condition = null;

		if (fieldName.startsWith("ExpandoValue.")) {
			String[] parts = fieldName.split("\\.", 2);

			fieldName = parts[1];
		}

		if (_filterType == FilterExpression.FilterType.INDIVIDUALS) {
			alias = "IndividualFields_" + fieldName;

			_referencedTableNames.add(alias);

			_referencedTableNames.add("ExpandoValue");

			condition = DSL.field(
				alias + ".name"
			).eq(
				fieldName
			);
		}
		else {
			alias = "ExpandoValue_" + fieldName;

			condition = DSL.field(
				alias + ".fieldName"
			).eq(
				fieldName
			);
		}

		String query = String.join(
			"", "CASE WHEN STARTS_WITH({0}.value, '[') AND ENDS_WITH(",
			"{0}.value, ']') THEN ({1} EXISTS (SELECT value FROM UNNEST(",
			"JSON_EXTRACT_STRING_ARRAY({0}.value,'$')) AS value WHERE ",
			"LOWER(value) = '", StringUtils.lowerCase(value),
			"')) ELSE LOWER({0}.value) {2} '", StringUtils.lowerCase(value),
			"' END");

		if (DateUtil.isValidPatternShort(value)) {
			query = String.join(
				"", "CASE WHEN {0}.name = '", fieldName, "' THEN DATE(",
				"PARSE_TIMESTAMP('%a %b %d %H:%M:%S %Z %Y', {0}.value)) {1} ",
				"SAFE_CAST('", value, "' AS DATE) ELSE false END");
		}
		else if (NumberUtils.isCreatable(value)) {
			query = String.join(
				"", "CASE WHEN STARTS_WITH({0}.value, '[') AND ENDS_WITH(",
				"{0}.value, ']') THEN (EXISTS (SELECT numeric_value FROM ",
				"UNNEST(JSON_EXTRACT_ARRAY({0}.value,'$')) AS numeric_value ",
				"WHERE SAFE_CAST(numeric_value AS NUMERIC) {1} SAFE_CAST('",
				value, "' AS NUMERIC))) WHEN SAFE_CAST({0}.value AS NUMERIC) ",
				"IS NULL THEN false ELSE SAFE_CAST({0}.value AS NUMERIC) {1} ",
				"SAFE_CAST('", value, "' AS NUMERIC) END");
		}

		if (operator.equalsIgnoreCase("contains")) {
			condition = condition.and(
				DSL.condition(
					String.join(
						"", "CASE WHEN STARTS_WITH(", alias, ".value, '[') ",
						"AND ENDS_WITH(", alias, ".value, ']') THEN (EXISTS ",
						"(SELECT value FROM UNNEST(JSON_EXTRACT_STRING_ARRAY(",
						alias, ".value,'$')) AS value WHERE LOWER(value) LIKE ",
						"'%", StringUtils.lowerCase(value), "%')) ELSE LOWER(",
						alias, ".value) LIKE '%", StringUtils.lowerCase(value),
						"%' END")));
		}
		else if (operator.equalsIgnoreCase("eq")) {
			if (StringUtil.isNull(value)) {
				Field aliasField = DSL.field(alias + ".value");

				condition = condition.and(
					DSL.or(
						aliasField.isNull(), aliasField.eq(""),
						aliasField.eq("[]"), aliasField.eq("[\"\"]")));
			}
			else {
				if (!DateUtil.isValidPatternShort(value) &&
					!NumberUtils.isCreatable(value)) {

					condition = condition.and(
						DSL.condition(
							StringUtil.replace(
								query, new String[] {"{0}", "{1}", "{2}"},
								new String[] {alias, "", "="})));
				}
				else {
					condition = condition.and(
						DSL.condition(
							StringUtil.replace(
								query, new String[] {"{0}", "{1}"},
								new String[] {alias, "="})));
				}
			}
		}
		else if (operator.equalsIgnoreCase("ge")) {
			condition = condition.and(
				DSL.condition(
					StringUtil.replace(
						query, new String[] {"{0}", "{1}"},
						new String[] {alias, ">="})));
		}
		else if (operator.equalsIgnoreCase("gt")) {
			condition = condition.and(
				DSL.condition(
					StringUtil.replace(
						query, new String[] {"{0}", "{1}"},
						new String[] {alias, ">"})));
		}
		else if (operator.equalsIgnoreCase("le")) {
			condition = condition.and(
				DSL.condition(
					StringUtil.replace(
						query, new String[] {"{0}", "{1}"},
						new String[] {alias, "<="})));
		}
		else if (operator.equalsIgnoreCase("lt")) {
			condition = condition.and(
				DSL.condition(
					StringUtil.replace(
						query, new String[] {"{0}", "{1}"},
						new String[] {alias, "<"})));
		}
		else if (operator.equalsIgnoreCase("ne")) {
			if (StringUtil.isNull(value)) {
				Field aliasField = DSL.field(alias + ".value");

				condition = condition.and(
					DSL.and(
						aliasField.isNotNull(), aliasField.ne(""),
						aliasField.ne("[]"), aliasField.ne("[\"\"]")));
			}
			else {
				if (!DateUtil.isValidPatternShort(value) &&
					!NumberUtils.isCreatable(value)) {

					condition = condition.and(
						DSL.condition(
							StringUtil.replace(
								query, new String[] {"{0}", "{1}", "{2}"},
								new String[] {alias, "NOT", "!="})));
				}
				else {
					condition = condition.and(
						DSL.condition(
							StringUtil.replace(
								query, new String[] {"{0}", "{1}"},
								new String[] {alias, "!="})));
				}
			}
		}

		return condition;
	}

	private Field _getDateField(Field field) {
		ZoneId zoneId = TimeZoneDogUtil.getZoneId();

		if (PostgresDialectThreadLocal.isPostgresDialect()) {
			if (StringUtils.equals(zoneId.toString(), "UTC")) {
				return DSL.function("DATE", Date.class, field);
			}

			return DSL.function(
				"DATE", Date.class,
				DSL.field(
					String.format("%s AT TIME ZONE '%s'", field, zoneId),
					OffsetDateTime.class));
		}

		return DSL.function("DATE", Date.class, field, DSL.val(zoneId));
	}

	private Condition _getDemographicsFieldCondition(
		ParserRuleContext parserRuleContext, String fieldName, String operator,
		String value) {

		Field leftField = _getLeftField(parserRuleContext);

		if (StringUtil.isNull(value)) {
			if (operator.equalsIgnoreCase("eq")) {
				return DSL.or(leftField.isNull(), leftField.eq(""));
			}

			return DSL.and(leftField.isNotNull(), leftField.ne(""));
		}

		fieldName = fieldName.toLowerCase();

		if (StringUtils.equalsIgnoreCase(fieldName, "birthday") ||
			StringUtils.endsWithIgnoreCase(fieldName, "date")) {

			leftField = DSL.date(leftField);
		}
		else {
			leftField = DSL.lower(leftField);

			value = value.toLowerCase();
		}

		if (operator.equalsIgnoreCase("eq")) {
			return leftField.eq(value);
		}
		else if (operator.equalsIgnoreCase("ge")) {
			return leftField.ge(value);
		}
		else if (operator.equalsIgnoreCase("gt")) {
			return leftField.gt(value);
		}
		else if (operator.equalsIgnoreCase("le")) {
			return leftField.le(value);
		}
		else if (operator.equalsIgnoreCase("lt")) {
			return leftField.lt(value);
		}

		return leftField.ne(value);
	}

	private Object _getIndividualIdsInOrganizationCondition(
		Condition condition, String expandoValueFieldName) {

		return DSL.field(
			"Individual.id", String.class
		).in(
			_getIndividualIdsInOrganizationSelectConditionStep(
				condition, expandoValueFieldName)
		);
	}

	private SelectConditionStep
		_getIndividualIdsInOrganizationSelectConditionStep(
			Condition condition, String expandoValueFieldName) {

		_referencedTableNames.add("Individual");

		SelectJoinStep selectJoinStep = DSL.selectDistinct(
			DSL.field("Individual.id", String.class)
		).from(
			DSL.table(
				"BQIndividual"
			).as(
				"Individual"
			)
		).crossJoin(
			DSL.table("UNNEST(Individual.memberships) AS IndividualMemberships")
		).join(
			DSL.table(
				"BQOrganization"
			).as(
				"Organization"
			)
		).on(
			DSL.condition(
				"Organization.id IN UNNEST(IndividualMemberships.ids)")
		);

		if (StringUtils.isNotBlank(expandoValueFieldName)) {
			selectJoinStep = selectJoinStep.join(
				DSL.table(
					"BQExpandoValue"
				).as(
					expandoValueFieldName
				)
			).on(
				DSL.and(
					DSL.field(
						expandoValueFieldName + ".classPK"
					).eq(
						DSL.field(
							"SAFE_CAST(Organization.organizationId AS STRING)")
					),
					DSL.field(
						expandoValueFieldName + ".classType"
					).eq(
						"com.liferay.portal.kernel.model.Organization"
					),
					DSL.field(
						expandoValueFieldName + ".dataSourceId"
					).eq(
						DSL.field("Organization.dataSourceId")
					))
			);
		}

		return selectJoinStep.where(
			DSL.field(
				"IndividualMemberships.name"
			).eq(
				"organizationIds"
			),
			condition);
	}

	private Object _getIndividualIdsNotInOrganizationCondition(
		Condition condition, String expandoValueFieldName) {

		return DSL.field(
			"Individual.id", String.class
		).notIn(
			_getIndividualIdsInOrganizationSelectConditionStep(
				condition, expandoValueFieldName)
		);
	}

	private Condition _getIsInterestedCondition(String keyword) {
		SelectConditionStep<Record1<String>> identitySelectConditionStep =
			DSL.selectDistinct(
				DSL.field("Identity.id", String.class)
			).from(
				DSL.table(
					"BQIdentity"
				).as(
					"Identity"
				)
			).join(
				DSL.table(
					"BQIdentityInterestScore"
				).as(
					"Interest"
				)
			).on(
				DSL.field(
					"Identity.id", String.class
				).eq(
					DSL.field("Interest.identityId", String.class)
				)
			).where(
				DSL.and(
					DSL.field(
						"Interest.keyword", String.class
					).eq(
						keyword
					),
					DSL.field(
						"Interest.interested", Boolean.class
					).eq(
						true
					))
			);

		SelectConditionStep<Record1<String>> individualSelectConditionStep =
			DSL.selectDistinct(
				DSL.field("Identity.individualId", String.class)
			).from(
				DSL.table(
					"BQIdentity"
				).as(
					"Identity"
				)
			).join(
				DSL.table(
					"BQIdentityInterestScore"
				).as(
					"Interest"
				)
			).on(
				DSL.field(
					"Identity.id", String.class
				).eq(
					DSL.field("Interest.identityId", String.class)
				)
			).where(
				DSL.and(
					DSL.field(
						"Interest.keyword", String.class
					).eq(
						keyword
					),
					DSL.field(
						"Interest.interested", Boolean.class
					).eq(
						true
					),
					DSL.field(
						"Identity.individualId", String.class
					).isNotNull())
			);

		return DSL.or(
			DSL.field(
				"Identity.id", String.class
			).in(
				identitySelectConditionStep
			),
			DSL.field(
				"Individual.id", String.class
			).in(
				individualSelectConditionStep
			));
	}

	private Condition _getIsMemberCondition(String id, String type) {
		_referencedTableNames.add("Individual");

		SelectConditionStep<Record1<String>> selectConditionStep =
			DSL.selectDistinct(
				DSL.field("Individual.id", String.class)
			).from(
				DSL.table(
					"BQIndividual"
				).as(
					"Individual"
				)
			).crossJoin(
				DSL.table(
					"UNNEST(Individual.memberships) AS IndividualMemberships")
			).where(
				DSL.and(
					DSL.field(
						"IndividualMemberships.name"
					).eq(
						type
					),
					DSL.condition(
						String.format(
							"'%s' IN UNNEST(IndividualMemberships.ids)", id)))
			);

		Field<String> field = DSL.field("Individual.id", String.class);

		return field.in(selectConditionStep);
	}

	private Field _getLeftField(ParserRuleContext parserRuleContext) {
		return _visitChild(parserRuleContext, 0);
	}

	private Field _getRightField(ParserRuleContext parserRuleContext) {
		Field rightField = _visitChild(parserRuleContext, 2);

		if (rightField == null) {
			return null;
		}

		Class<?> rightFieldType = rightField.getType();

		if ((rightField instanceof Param) &&
			rightFieldType.isAssignableFrom(String.class)) {

			Param<String> rightParam = (Param<String>)rightField;

			String value = rightParam.getValue();

			if (_timeFrameParameterNames.contains(value)) {
				return _getTimeFrameParam(value);
			}
		}

		return rightField;
	}

	private String _getTableNamespace() {
		String tableReference = _tableReferences.get(_filterType);

		if (tableReference == null) {
			return "";
		}

		return tableReference + ".";
	}

	private Param<LocalDateTime> _getTimeFrameParam(String value) {
		LocalDateTime localDateTime = LocalDateTime.of(
			LocalDate.now(TimeZoneDogUtil.getZoneId()), LocalTime.MIDNIGHT);

		if (value.equalsIgnoreCase("last24Hours")) {
			localDateTime = localDateTime.minusHours(24);
		}
		else if (value.equalsIgnoreCase("last28Days")) {
			localDateTime = localDateTime.minusDays(28);
		}
		else if (value.equalsIgnoreCase("last30Days")) {
			localDateTime = localDateTime.minusDays(30);
		}
		else if (value.equalsIgnoreCase("last7Days")) {
			localDateTime = localDateTime.minusDays(7);
		}
		else if (value.equalsIgnoreCase("last90Days")) {
			localDateTime = localDateTime.minusDays(90);
		}
		else {
			localDateTime = localDateTime.minusDays(1);
		}

		return DSL.val(localDateTime);
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

	private <T> T _visitChild(
		ParserRuleContext parserRuleContext, int childIndex) {

		ParseTree parseTree = parserRuleContext.getChild(childIndex);

		return (T)parseTree.accept(this);
	}

	private Object _visitOrganizationExpression(
		String fieldName, String operator, String value) {

		if (fieldName.startsWith("custom/")) {
			String[] identifierParts = StringUtils.split(fieldName, "/");

			return _getIndividualIdsInOrganizationCondition(
				_getCustomFieldCondition(identifierParts[1], operator, value),
				"ExpandoValue_" + identifierParts[1]);
		}

		if (fieldName.equalsIgnoreCase("id")) {
			return DSL.field(
				"Individual.id", String.class
			).in(
				DSL.selectDistinct(
					DSL.field("Individual.id", String.class)
				).from(
					DSL.table(
						"BQIndividual"
					).as(
						"Individual"
					)
				).crossJoin(
					DSL.table(
						"UNNEST(Individual.memberships) AS " +
							"IndividualMemberships")
				).where(
					DSL.and(
						DSL.field(
							"IndividualMemberships.name"
						).eq(
							"organizationIds"
						),
						DSL.condition(
							String.format(
								"'%s' IN UNNEST(IndividualMemberships.ids)",
								value)))
				)
			);
		}

		if (fieldName.equalsIgnoreCase("parentId") ||
			fieldName.equalsIgnoreCase("parentOrganizationId")) {

			return DSL.field(
				"Individual.id", String.class
			).in(
				DSL.selectDistinct(
					DSL.field("Individual.id", String.class)
				).from(
					DSL.table(
						"BQIndividual"
					).as(
						"Individual"
					)
				).crossJoin(
					DSL.table(
						"UNNEST(Individual.memberships) AS " +
							"IndividualMemberships")
				).join(
					DSL.table(
						"BQOrganization"
					).as(
						"Organization"
					)
				).on(
					DSL.condition(
						"Organization.id IN UNNEST(IndividualMemberships.ids)")
				).join(
					DSL.table(
						"BQOrganization"
					).as(
						"ParentOrganization"
					)
				).on(
					DSL.and(
						DSL.field(
							"Organization.parentOrganizationId"
						).eq(
							DSL.field("ParentOrganization.organizationId")
						),
						DSL.field(
							"Organization.dataSourceId"
						).eq(
							DSL.field("ParentOrganization.dataSourceId")
						))
				).where(
					DSL.field(
						"ParentOrganization.id"
					).eq(
						value
					)
				)
			);
		}

		String qualifiedFieldName = _fieldMappers.getOrDefault(
			_filterType.getName() + "." + fieldName,
			_getTableNamespace() + fieldName);

		if (operator.equalsIgnoreCase("eq") && StringUtil.isNull(value)) {
			return _getIndividualIdsNotInOrganizationCondition(
				DSL.field(
					qualifiedFieldName
				).isNotNull(),
				null);
		}

		Condition condition = null;

		if (fieldName.equalsIgnoreCase("modifiedDate")) {
			String query =
				"DATE(" + qualifiedFieldName + ") {0} SAFE_CAST('" + value +
					"' AS DATE)";

			if (operator.equalsIgnoreCase("eq")) {
				condition = DSL.condition(
					StringUtil.replace(query, "{0}", "="));
			}
			else if (operator.equalsIgnoreCase("gt")) {
				condition = DSL.condition(
					StringUtil.replace(query, "{0}", ">"));
			}
			else if (operator.equalsIgnoreCase("lt")) {
				condition = DSL.condition(
					StringUtil.replace(query, "{0}", "<"));
			}
		}
		else {
			if (operator.equalsIgnoreCase("eq")) {
				condition = DSL.field(
					qualifiedFieldName
				).eq(
					value
				);
			}
			else if (operator.equalsIgnoreCase("ne")) {
				if (StringUtil.isNull(value)) {
					condition = DSL.field(
						qualifiedFieldName
					).isNotNull();
				}
				else {
					condition = DSL.field(
						qualifiedFieldName
					).ne(
						value
					);
				}
			}
		}

		return _getIndividualIdsInOrganizationCondition(condition, null);
	}

	private static final Map<String, String> _attributeTypes =
		new HashMap<String, String>() {
			{
				put("boolean", "BOOLEAN");
				put("date", "DATE");
				put("number", "NUMERIC");
				put("text", "STRING");
			}
		};
	private static final Set<String> _timeFrameParameterNames = SetUtil.of(
		"last24Hours", "last28Days", "last30Days", "last7Days", "last90Days",
		"yesterday");

	private final Long _channelId;
	private final Map<String, String> _fieldMappers =
		new HashMap<String, String>() {
			{
				put("activities.day", "Event.eventDate");
				put("channelIds", "IdentityActivity.channelId");
				put("credentials/type", "credentialType");
				put("individualCount", "identitiesCount");
				put("individuals.additionalName", "Individual.middleName");
				put("individuals.address", "Individual.addresses");
				put("individuals.birthDate", "Individual.birthday");
				put("individuals.email", "Individual.emailAddress");
				put("individuals.familyName", "Individual.lastName");
				put("individuals.givenName", "Individual.firstName");
				put(
					"individuals.lastEnrichmentDate",
					"Individual.modifiedDate");
				put("individualSegmentIds", "Membership.segmentId");
				put("interestName", "keyword");
				put("organizations.hierarchyPath", "Organization.treePath");
				put("provider/type", "providerType");
				put("sessions.completeDate", "Session.sessionEnd");
				put("sessions.referrer", "Session.referrers");
				put("sessions.url", "Session.urls");
				put("userId", "User.id");
			}
		};
	private final FilterExpression.FilterType _filterType;
	private final Set<String> _referencedTableNames = new HashSet<>();
	private final Map<FilterExpression.FilterType, String> _tableReferences =
		new HashMap<FilterExpression.FilterType, String>() {
			{
				put(FilterExpression.FilterType.ACTIVITIES, "Event");
				put(FilterExpression.FilterType.INDIVIDUALS, "Individual");
				put(FilterExpression.FilterType.MEMBERSHIPS, "Membership");
				put(FilterExpression.FilterType.ORGANIZATIONS, "Organization");
				put(FilterExpression.FilterType.SESSIONS, "Session");
			}
		};

}