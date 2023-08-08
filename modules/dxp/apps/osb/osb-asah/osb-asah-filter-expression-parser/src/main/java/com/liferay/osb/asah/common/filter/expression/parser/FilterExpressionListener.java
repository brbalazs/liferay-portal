// Generated from FilterExpression.g4 by ANTLR 4.5.3

/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.filter.expression.parser;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link FilterExpressionParser}.
 */
public interface FilterExpressionListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code ToLogicalTerm}
	 * labeled alternative in {@link FilterExpressionParser#booleanOperandExpression}.
	 * @param ctx the parse tree
	 */
	void enterToLogicalTerm(FilterExpressionParser.ToLogicalTermContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ToLogicalTerm}
	 * labeled alternative in {@link FilterExpressionParser#booleanOperandExpression}.
	 * @param ctx the parse tree
	 */
	void exitToLogicalTerm(FilterExpressionParser.ToLogicalTermContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BooleanParenthesis}
	 * labeled alternative in {@link FilterExpressionParser#booleanOperandExpression}.
	 * @param ctx the parse tree
	 */
	void enterBooleanParenthesis(FilterExpressionParser.BooleanParenthesisContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BooleanParenthesis}
	 * labeled alternative in {@link FilterExpressionParser#booleanOperandExpression}.
	 * @param ctx the parse tree
	 */
	void exitBooleanParenthesis(FilterExpressionParser.BooleanParenthesisContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotExpression}
	 * labeled alternative in {@link FilterExpressionParser#booleanUnaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterNotExpression(FilterExpressionParser.NotExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotExpression}
	 * labeled alternative in {@link FilterExpressionParser#booleanUnaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitNotExpression(FilterExpressionParser.NotExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ToBooleanOperandExpression}
	 * labeled alternative in {@link FilterExpressionParser#booleanUnaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterToBooleanOperandExpression(FilterExpressionParser.ToBooleanOperandExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ToBooleanOperandExpression}
	 * labeled alternative in {@link FilterExpressionParser#booleanUnaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitToBooleanOperandExpression(FilterExpressionParser.ToBooleanOperandExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code GreaterThanOrEqualsExpression}
	 * labeled alternative in {@link FilterExpressionParser#comparisonExpression}.
	 * @param ctx the parse tree
	 */
	void enterGreaterThanOrEqualsExpression(FilterExpressionParser.GreaterThanOrEqualsExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code GreaterThanOrEqualsExpression}
	 * labeled alternative in {@link FilterExpressionParser#comparisonExpression}.
	 * @param ctx the parse tree
	 */
	void exitGreaterThanOrEqualsExpression(FilterExpressionParser.GreaterThanOrEqualsExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LessThanOrEqualsExpression}
	 * labeled alternative in {@link FilterExpressionParser#comparisonExpression}.
	 * @param ctx the parse tree
	 */
	void enterLessThanOrEqualsExpression(FilterExpressionParser.LessThanOrEqualsExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LessThanOrEqualsExpression}
	 * labeled alternative in {@link FilterExpressionParser#comparisonExpression}.
	 * @param ctx the parse tree
	 */
	void exitLessThanOrEqualsExpression(FilterExpressionParser.LessThanOrEqualsExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code GreaterThanExpression}
	 * labeled alternative in {@link FilterExpressionParser#comparisonExpression}.
	 * @param ctx the parse tree
	 */
	void enterGreaterThanExpression(FilterExpressionParser.GreaterThanExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code GreaterThanExpression}
	 * labeled alternative in {@link FilterExpressionParser#comparisonExpression}.
	 * @param ctx the parse tree
	 */
	void exitGreaterThanExpression(FilterExpressionParser.GreaterThanExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ToBooleanUnaryExpression}
	 * labeled alternative in {@link FilterExpressionParser#comparisonExpression}.
	 * @param ctx the parse tree
	 */
	void enterToBooleanUnaryExpression(FilterExpressionParser.ToBooleanUnaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ToBooleanUnaryExpression}
	 * labeled alternative in {@link FilterExpressionParser#comparisonExpression}.
	 * @param ctx the parse tree
	 */
	void exitToBooleanUnaryExpression(FilterExpressionParser.ToBooleanUnaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LessThanExpression}
	 * labeled alternative in {@link FilterExpressionParser#comparisonExpression}.
	 * @param ctx the parse tree
	 */
	void enterLessThanExpression(FilterExpressionParser.LessThanExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LessThanExpression}
	 * labeled alternative in {@link FilterExpressionParser#comparisonExpression}.
	 * @param ctx the parse tree
	 */
	void exitLessThanExpression(FilterExpressionParser.LessThanExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotEqualsExpression}
	 * labeled alternative in {@link FilterExpressionParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void enterNotEqualsExpression(FilterExpressionParser.NotEqualsExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotEqualsExpression}
	 * labeled alternative in {@link FilterExpressionParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void exitNotEqualsExpression(FilterExpressionParser.NotEqualsExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ToComparisonExpression}
	 * labeled alternative in {@link FilterExpressionParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void enterToComparisonExpression(FilterExpressionParser.ToComparisonExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ToComparisonExpression}
	 * labeled alternative in {@link FilterExpressionParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void exitToComparisonExpression(FilterExpressionParser.ToComparisonExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code EqualsExpression}
	 * labeled alternative in {@link FilterExpressionParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void enterEqualsExpression(FilterExpressionParser.EqualsExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code EqualsExpression}
	 * labeled alternative in {@link FilterExpressionParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void exitEqualsExpression(FilterExpressionParser.EqualsExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link FilterExpressionParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(FilterExpressionParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterExpressionParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(FilterExpressionParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link FilterExpressionParser#filterExpression}.
	 * @param ctx the parse tree
	 */
	void enterFilterExpression(FilterExpressionParser.FilterExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterExpressionParser#filterExpression}.
	 * @param ctx the parse tree
	 */
	void exitFilterExpression(FilterExpressionParser.FilterExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link FilterExpressionParser#filterByCountExpression}.
	 * @param ctx the parse tree
	 */
	void enterFilterByCountExpression(FilterExpressionParser.FilterByCountExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterExpressionParser#filterByCountExpression}.
	 * @param ctx the parse tree
	 */
	void exitFilterByCountExpression(FilterExpressionParser.FilterByCountExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link FilterExpressionParser#functionCallExpression}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCallExpression(FilterExpressionParser.FunctionCallExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterExpressionParser#functionCallExpression}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCallExpression(FilterExpressionParser.FunctionCallExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link FilterExpressionParser#functionParameters}.
	 * @param ctx the parse tree
	 */
	void enterFunctionParameters(FilterExpressionParser.FunctionParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterExpressionParser#functionParameters}.
	 * @param ctx the parse tree
	 */
	void exitFunctionParameters(FilterExpressionParser.FunctionParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link FilterExpressionParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(FilterExpressionParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterExpressionParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(FilterExpressionParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FloatingPointLiteral}
	 * labeled alternative in {@link FilterExpressionParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterFloatingPointLiteral(FilterExpressionParser.FloatingPointLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FloatingPointLiteral}
	 * labeled alternative in {@link FilterExpressionParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitFloatingPointLiteral(FilterExpressionParser.FloatingPointLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IntegerLiteral}
	 * labeled alternative in {@link FilterExpressionParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterIntegerLiteral(FilterExpressionParser.IntegerLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IntegerLiteral}
	 * labeled alternative in {@link FilterExpressionParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitIntegerLiteral(FilterExpressionParser.IntegerLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BooleanLiteral}
	 * labeled alternative in {@link FilterExpressionParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterBooleanLiteral(FilterExpressionParser.BooleanLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BooleanLiteral}
	 * labeled alternative in {@link FilterExpressionParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitBooleanLiteral(FilterExpressionParser.BooleanLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NullLiteral}
	 * labeled alternative in {@link FilterExpressionParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterNullLiteral(FilterExpressionParser.NullLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NullLiteral}
	 * labeled alternative in {@link FilterExpressionParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitNullLiteral(FilterExpressionParser.NullLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StringLiteral}
	 * labeled alternative in {@link FilterExpressionParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterStringLiteral(FilterExpressionParser.StringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StringLiteral}
	 * labeled alternative in {@link FilterExpressionParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitStringLiteral(FilterExpressionParser.StringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AndExpression}
	 * labeled alternative in {@link FilterExpressionParser#logicalAndExpression}.
	 * @param ctx the parse tree
	 */
	void enterAndExpression(FilterExpressionParser.AndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AndExpression}
	 * labeled alternative in {@link FilterExpressionParser#logicalAndExpression}.
	 * @param ctx the parse tree
	 */
	void exitAndExpression(FilterExpressionParser.AndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ToEqualityExpression}
	 * labeled alternative in {@link FilterExpressionParser#logicalAndExpression}.
	 * @param ctx the parse tree
	 */
	void enterToEqualityExpression(FilterExpressionParser.ToEqualityExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ToEqualityExpression}
	 * labeled alternative in {@link FilterExpressionParser#logicalAndExpression}.
	 * @param ctx the parse tree
	 */
	void exitToEqualityExpression(FilterExpressionParser.ToEqualityExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ToLogicalAndExpression}
	 * labeled alternative in {@link FilterExpressionParser#logicalOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterToLogicalAndExpression(FilterExpressionParser.ToLogicalAndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ToLogicalAndExpression}
	 * labeled alternative in {@link FilterExpressionParser#logicalOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitToLogicalAndExpression(FilterExpressionParser.ToLogicalAndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OrExpression}
	 * labeled alternative in {@link FilterExpressionParser#logicalOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterOrExpression(FilterExpressionParser.OrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OrExpression}
	 * labeled alternative in {@link FilterExpressionParser#logicalOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitOrExpression(FilterExpressionParser.OrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ToIdentifier}
	 * labeled alternative in {@link FilterExpressionParser#logicalTerm}.
	 * @param ctx the parse tree
	 */
	void enterToIdentifier(FilterExpressionParser.ToIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ToIdentifier}
	 * labeled alternative in {@link FilterExpressionParser#logicalTerm}.
	 * @param ctx the parse tree
	 */
	void exitToIdentifier(FilterExpressionParser.ToIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ToLiteral}
	 * labeled alternative in {@link FilterExpressionParser#logicalTerm}.
	 * @param ctx the parse tree
	 */
	void enterToLiteral(FilterExpressionParser.ToLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ToLiteral}
	 * labeled alternative in {@link FilterExpressionParser#logicalTerm}.
	 * @param ctx the parse tree
	 */
	void exitToLiteral(FilterExpressionParser.ToLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ToFunctionCallExpression}
	 * labeled alternative in {@link FilterExpressionParser#logicalTerm}.
	 * @param ctx the parse tree
	 */
	void enterToFunctionCallExpression(FilterExpressionParser.ToFunctionCallExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ToFunctionCallExpression}
	 * labeled alternative in {@link FilterExpressionParser#logicalTerm}.
	 * @param ctx the parse tree
	 */
	void exitToFunctionCallExpression(FilterExpressionParser.ToFunctionCallExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ToFilterExpression}
	 * labeled alternative in {@link FilterExpressionParser#logicalTerm}.
	 * @param ctx the parse tree
	 */
	void enterToFilterExpression(FilterExpressionParser.ToFilterExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ToFilterExpression}
	 * labeled alternative in {@link FilterExpressionParser#logicalTerm}.
	 * @param ctx the parse tree
	 */
	void exitToFilterExpression(FilterExpressionParser.ToFilterExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ToFilterByCountExpression}
	 * labeled alternative in {@link FilterExpressionParser#logicalTerm}.
	 * @param ctx the parse tree
	 */
	void enterToFilterByCountExpression(FilterExpressionParser.ToFilterByCountExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ToFilterByCountExpression}
	 * labeled alternative in {@link FilterExpressionParser#logicalTerm}.
	 * @param ctx the parse tree
	 */
	void exitToFilterByCountExpression(FilterExpressionParser.ToFilterByCountExpressionContext ctx);
}
