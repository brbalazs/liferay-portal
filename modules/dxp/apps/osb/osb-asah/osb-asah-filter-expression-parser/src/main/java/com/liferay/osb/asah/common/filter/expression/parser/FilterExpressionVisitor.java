// Generated from FilterExpression.g4 by ANTLR 4.5.3

/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.filter.expression.parser;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link FilterExpressionParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface FilterExpressionVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code ToLogicalTerm}
	 * labeled alternative in {@link FilterExpressionParser#booleanOperandExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitToLogicalTerm(FilterExpressionParser.ToLogicalTermContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BooleanParenthesis}
	 * labeled alternative in {@link FilterExpressionParser#booleanOperandExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanParenthesis(FilterExpressionParser.BooleanParenthesisContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NotExpression}
	 * labeled alternative in {@link FilterExpressionParser#booleanUnaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExpression(FilterExpressionParser.NotExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ToBooleanOperandExpression}
	 * labeled alternative in {@link FilterExpressionParser#booleanUnaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitToBooleanOperandExpression(FilterExpressionParser.ToBooleanOperandExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code GreaterThanOrEqualsExpression}
	 * labeled alternative in {@link FilterExpressionParser#comparisonExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGreaterThanOrEqualsExpression(FilterExpressionParser.GreaterThanOrEqualsExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LessThanOrEqualsExpression}
	 * labeled alternative in {@link FilterExpressionParser#comparisonExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLessThanOrEqualsExpression(FilterExpressionParser.LessThanOrEqualsExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code GreaterThanExpression}
	 * labeled alternative in {@link FilterExpressionParser#comparisonExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGreaterThanExpression(FilterExpressionParser.GreaterThanExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ToBooleanUnaryExpression}
	 * labeled alternative in {@link FilterExpressionParser#comparisonExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitToBooleanUnaryExpression(FilterExpressionParser.ToBooleanUnaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LessThanExpression}
	 * labeled alternative in {@link FilterExpressionParser#comparisonExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLessThanExpression(FilterExpressionParser.LessThanExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NotEqualsExpression}
	 * labeled alternative in {@link FilterExpressionParser#equalityExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotEqualsExpression(FilterExpressionParser.NotEqualsExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ToComparisonExpression}
	 * labeled alternative in {@link FilterExpressionParser#equalityExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitToComparisonExpression(FilterExpressionParser.ToComparisonExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code EqualsExpression}
	 * labeled alternative in {@link FilterExpressionParser#equalityExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualsExpression(FilterExpressionParser.EqualsExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link FilterExpressionParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(FilterExpressionParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link FilterExpressionParser#filterExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilterExpression(FilterExpressionParser.FilterExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link FilterExpressionParser#filterByCountExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilterByCountExpression(FilterExpressionParser.FilterByCountExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link FilterExpressionParser#functionCallExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCallExpression(FilterExpressionParser.FunctionCallExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link FilterExpressionParser#functionParameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionParameters(FilterExpressionParser.FunctionParametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link FilterExpressionParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(FilterExpressionParser.IdentifierContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FloatingPointLiteral}
	 * labeled alternative in {@link FilterExpressionParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloatingPointLiteral(FilterExpressionParser.FloatingPointLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IntegerLiteral}
	 * labeled alternative in {@link FilterExpressionParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntegerLiteral(FilterExpressionParser.IntegerLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BooleanLiteral}
	 * labeled alternative in {@link FilterExpressionParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanLiteral(FilterExpressionParser.BooleanLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NullLiteral}
	 * labeled alternative in {@link FilterExpressionParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullLiteral(FilterExpressionParser.NullLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StringLiteral}
	 * labeled alternative in {@link FilterExpressionParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteral(FilterExpressionParser.StringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AndExpression}
	 * labeled alternative in {@link FilterExpressionParser#logicalAndExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpression(FilterExpressionParser.AndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ToEqualityExpression}
	 * labeled alternative in {@link FilterExpressionParser#logicalAndExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitToEqualityExpression(FilterExpressionParser.ToEqualityExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ToLogicalAndExpression}
	 * labeled alternative in {@link FilterExpressionParser#logicalOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitToLogicalAndExpression(FilterExpressionParser.ToLogicalAndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OrExpression}
	 * labeled alternative in {@link FilterExpressionParser#logicalOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpression(FilterExpressionParser.OrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ToIdentifier}
	 * labeled alternative in {@link FilterExpressionParser#logicalTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitToIdentifier(FilterExpressionParser.ToIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ToLiteral}
	 * labeled alternative in {@link FilterExpressionParser#logicalTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitToLiteral(FilterExpressionParser.ToLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ToFunctionCallExpression}
	 * labeled alternative in {@link FilterExpressionParser#logicalTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitToFunctionCallExpression(FilterExpressionParser.ToFunctionCallExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ToFilterExpression}
	 * labeled alternative in {@link FilterExpressionParser#logicalTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitToFilterExpression(FilterExpressionParser.ToFilterExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ToFilterByCountExpression}
	 * labeled alternative in {@link FilterExpressionParser#logicalTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitToFilterByCountExpression(FilterExpressionParser.ToFilterByCountExpressionContext ctx);
}
