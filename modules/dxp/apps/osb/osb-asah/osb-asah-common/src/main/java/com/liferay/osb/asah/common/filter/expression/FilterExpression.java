/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.filter.expression;

import com.liferay.osb.asah.common.filter.expression.parser.FilterExpressionLexer;
import com.liferay.osb.asah.common.filter.expression.parser.FilterExpressionParser;
import com.liferay.osb.asah.common.filter.expression.parser.FilterExpressionVisitor;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import org.jooq.Condition;

import org.springframework.util.Assert;

/**
 * @author Marcellus Tavares
 * @author Ivica Cardic
 */
public class FilterExpression {

	public FilterExpression(String filterExpressionString) {
		this(filterExpressionString, null, false);
	}

	public FilterExpression(String filterExpressionString, boolean segment) {
		this(filterExpressionString, null, segment);
	}

	public FilterExpression(
		String filterExpressionString,
		FilterExpressionVisitor<?> filterExpressionVisitor) {

		Assert.notNull(
			filterExpressionString, "Filter expression string is null");

		FilterExpressionParser.ExpressionContext expressionContext = _parse(
			filterExpressionString);

		expressionContext.accept(filterExpressionVisitor);
	}

	public FilterExpression(
		String filterExpressionString, FilterType filterType) {

		this(filterExpressionString, filterType, false);
	}

	public FilterExpression(
		String filterExpressionString, FilterType filterType, boolean segment) {

		Assert.notNull(
			filterExpressionString, "Filter expression string is null");

		FilterExpressionParser.ExpressionContext expressionContext = _parse(
			_rewriteFilterExpression(filterExpressionString));

		if (filterType != null) {
			_filterType = filterType;
		}

		if ((_filterType == null) && segment) {
			_filterType = FilterType.INDIVIDUALS;
		}

		FilterExpressionConditionVisitor filterExpressionConditionVisitor =
			new FilterExpressionConditionVisitor(_filterType);

		expressionContext.accept(filterExpressionConditionVisitor);

		_condition = (Condition)expressionContext.accept(
			filterExpressionConditionVisitor);
		_referencedTableNames =
			filterExpressionConditionVisitor.getReferencedTableNames();
	}

	public Condition getCondition() {
		return _condition;
	}

	public Set<String> getReferencedTableNames() {
		return _referencedTableNames;
	}

	public enum FilterType {

		ACTIVITIES("activities"), ASSETS("assets"), INDIVIDUALS("individuals"),
		MEMBERSHIPS("memberships"), ORGANIZATIONS("organizations"),
		SESSIONS("sessions");

		public static FilterType of(String name) {
			if (name == null) {
				return null;
			}

			if (name.equalsIgnoreCase("activities")) {
				return ACTIVITIES;
			}

			if (name.equalsIgnoreCase("assets")) {
				return ASSETS;
			}

			if (name.equalsIgnoreCase("individuals")) {
				return INDIVIDUALS;
			}

			if (name.equalsIgnoreCase("memberships")) {
				return MEMBERSHIPS;
			}

			if (name.equalsIgnoreCase("organizations")) {
				return ORGANIZATIONS;
			}

			if (name.equalsIgnoreCase("sessions")) {
				return SESSIONS;
			}

			return null;
		}

		public String getName() {
			return _name;
		}

		private FilterType(String name) {
			_name = name;
		}

		private final String _name;

	}

	private FilterExpressionParser.ExpressionContext _parse(
		String filterExpressionString) {

		try {
			ErrorListener errorListener = new ErrorListener();

			FilterExpressionLexer filterExpressionLexer =
				new FilterExpressionLexer(
					new ANTLRInputStream(filterExpressionString));

			filterExpressionLexer.addErrorListener(errorListener);

			FilterExpressionParser filterExpressionParser =
				new FilterExpressionParser(
					new CommonTokenStream(filterExpressionLexer));

			filterExpressionParser.setErrorHandler(new BailErrorStrategy());

			return filterExpressionParser.expression();
		}
		catch (ParseCancellationException parseCancellationException) {
			throw new FilterExpressionParserException(
				"Unable to parse " + filterExpressionString,
				parseCancellationException);
		}
	}

	private String _rewriteFilterExpression(String filterExpressionString) {
		Matcher matcher = _activityKeyPattern.matcher(filterExpressionString);

		while (matcher.find()) {
			String applicationId = matcher.group("applicationId");

			String eventId = matcher.group("eventId");

			if (applicationId.equalsIgnoreCase("Blog") &&
				eventId.equalsIgnoreCase("commentPosted")) {

				applicationId = "Comment";
				eventId = "posted";
			}

			String expression =
				"applicationId eq ''" + applicationId + "'' and eventId eq ''" +
					eventId + "'' and sha256Hex(assetId) eq ''" +
						matcher.group("assetIdHashed") + "''";

			filterExpressionString = matcher.replaceFirst(expression);

			matcher = _activityKeyPattern.matcher(filterExpressionString);
		}

		matcher = _interestPattern.matcher(filterExpressionString);

		while (matcher.find()) {
			String expression =
				"interests.filter(filter='(isInterested(keyword, ''" +
					matcher.group("keyword") + "''))')";

			if (!Boolean.parseBoolean(matcher.group("interested"))) {
				expression = "not(" + expression + ")";
			}

			filterExpressionString = matcher.replaceFirst(expression);

			matcher = _interestPattern.matcher(filterExpressionString);
		}

		matcher = _membershipPattern.matcher(filterExpressionString);

		while (matcher.find()) {
			String expression =
				"isMember(" + matcher.group("type") + ", '" +
					matcher.group("id") + "')";

			String operator = matcher.group("operator");

			if (operator.equals("ne")) {
				expression = "not(" + expression + ")";
			}

			filterExpressionString = matcher.replaceFirst(expression);

			matcher = _membershipPattern.matcher(filterExpressionString);
		}

		matcher = _notContainsPattern.matcher(filterExpressionString);

		while (matcher.find()) {
			filterExpressionString = matcher.replaceFirst("notContains");

			matcher = _notContainsPattern.matcher(filterExpressionString);
		}

		return filterExpressionString;
	}

	private static final Pattern _activityKeyPattern = Pattern.compile(
		"activityKey eq ''(?<applicationId>[\\w]+)#(?<eventId>[\\w]+)#" +
			"(?<assetIdHashed>[\\w]+)''");
	private static final Pattern _interestPattern = Pattern.compile(
		"interests.filter\\(filter='\\(name eq ''(?<keyword>[^']+)'' and " +
			"score eq ''(?<interested>true|false)''\\)'\\)");
	private static final Pattern _membershipPattern = Pattern.compile(
		"(?<type>groupIds|roleIds|teamIds|userGroupIds) (?<operator>eq|ne) '" +
			"(?<id>[\\w]+)'");
	private static final Pattern _notContainsPattern = Pattern.compile(
		"not contains");

	private Condition _condition;
	private FilterType _filterType;
	private Set<String> _referencedTableNames;

	private static class ErrorListener extends BaseErrorListener {

		@Override
		public void syntaxError(
			Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
			int charPositionInLine, String message,
			RecognitionException recognitionException) {

			throw new ParseCancellationException(message, recognitionException);
		}

	}

}