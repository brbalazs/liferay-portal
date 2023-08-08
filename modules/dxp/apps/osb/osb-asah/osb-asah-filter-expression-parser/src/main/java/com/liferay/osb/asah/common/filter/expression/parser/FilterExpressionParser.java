// Generated from FilterExpression.g4 by ANTLR 4.5.3

/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.filter.expression.parser;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class FilterExpressionParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, AND=8, COMMA=9, 
		EQ=10, FLOATING_POINT_LITERAL=11, NEQ=12, GE=13, GT=14, INTEGER_LITERAL=15, 
		LE=16, LPAREN=17, RPAREN=18, LT=19, NOT=20, OR=21, STRING_LITERAL=22, 
		VARIABLE_SIMPLE_IDENTIFIER=23, VARIABLE_QUALIFIED_IDENTIFIER=24, WS=25;
	public static final int
		RULE_booleanOperandExpression = 0, RULE_booleanUnaryExpression = 1, RULE_comparisonExpression = 2, 
		RULE_equalityExpression = 3, RULE_expression = 4, RULE_filterExpression = 5, 
		RULE_filterByCountExpression = 6, RULE_functionCallExpression = 7, RULE_functionParameters = 8, 
		RULE_identifier = 9, RULE_literal = 10, RULE_logicalAndExpression = 11, 
		RULE_logicalOrExpression = 12, RULE_logicalTerm = 13;
	public static final String[] ruleNames = {
		"booleanOperandExpression", "booleanUnaryExpression", "comparisonExpression", 
		"equalityExpression", "expression", "filterExpression", "filterByCountExpression", 
		"functionCallExpression", "functionParameters", "identifier", "literal", 
		"logicalAndExpression", "logicalOrExpression", "logicalTerm"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'.filter(filter='", "'.filterByCount(filter='", "'operator='", 
		"'value='", "'true'", "'false'", "'null'", null, "','", null, null, "'ne'", 
		"'ge'", "'gt'", null, "'le'", "'('", "')'", "'lt'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, "AND", "COMMA", "EQ", 
		"FLOATING_POINT_LITERAL", "NEQ", "GE", "GT", "INTEGER_LITERAL", "LE", 
		"LPAREN", "RPAREN", "LT", "NOT", "OR", "STRING_LITERAL", "VARIABLE_SIMPLE_IDENTIFIER", 
		"VARIABLE_QUALIFIED_IDENTIFIER", "WS"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "FilterExpression.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public FilterExpressionParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class BooleanOperandExpressionContext extends ParserRuleContext {
		public BooleanOperandExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleanOperandExpression; }
	 
		public BooleanOperandExpressionContext() { }
		public void copyFrom(BooleanOperandExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ToLogicalTermContext extends BooleanOperandExpressionContext {
		public LogicalTermContext logicalTerm() {
			return getRuleContext(LogicalTermContext.class,0);
		}
		public ToLogicalTermContext(BooleanOperandExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).enterToLogicalTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).exitToLogicalTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExpressionVisitor ) return ((FilterExpressionVisitor<? extends T>)visitor).visitToLogicalTerm(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BooleanParenthesisContext extends BooleanOperandExpressionContext {
		public TerminalNode LPAREN() { return getToken(FilterExpressionParser.LPAREN, 0); }
		public LogicalOrExpressionContext logicalOrExpression() {
			return getRuleContext(LogicalOrExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(FilterExpressionParser.RPAREN, 0); }
		public BooleanParenthesisContext(BooleanOperandExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).enterBooleanParenthesis(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).exitBooleanParenthesis(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExpressionVisitor ) return ((FilterExpressionVisitor<? extends T>)visitor).visitBooleanParenthesis(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BooleanOperandExpressionContext booleanOperandExpression() throws RecognitionException {
		BooleanOperandExpressionContext _localctx = new BooleanOperandExpressionContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_booleanOperandExpression);
		try {
			setState(33);
			switch (_input.LA(1)) {
			case T__4:
			case T__5:
			case T__6:
			case FLOATING_POINT_LITERAL:
			case INTEGER_LITERAL:
			case STRING_LITERAL:
			case VARIABLE_SIMPLE_IDENTIFIER:
			case VARIABLE_QUALIFIED_IDENTIFIER:
				_localctx = new ToLogicalTermContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(28);
				logicalTerm();
				}
				break;
			case LPAREN:
				_localctx = new BooleanParenthesisContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(29);
				match(LPAREN);
				setState(30);
				logicalOrExpression(0);
				setState(31);
				match(RPAREN);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BooleanUnaryExpressionContext extends ParserRuleContext {
		public BooleanUnaryExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleanUnaryExpression; }
	 
		public BooleanUnaryExpressionContext() { }
		public void copyFrom(BooleanUnaryExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ToBooleanOperandExpressionContext extends BooleanUnaryExpressionContext {
		public BooleanOperandExpressionContext booleanOperandExpression() {
			return getRuleContext(BooleanOperandExpressionContext.class,0);
		}
		public ToBooleanOperandExpressionContext(BooleanUnaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).enterToBooleanOperandExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).exitToBooleanOperandExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExpressionVisitor ) return ((FilterExpressionVisitor<? extends T>)visitor).visitToBooleanOperandExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NotExpressionContext extends BooleanUnaryExpressionContext {
		public TerminalNode NOT() { return getToken(FilterExpressionParser.NOT, 0); }
		public BooleanUnaryExpressionContext booleanUnaryExpression() {
			return getRuleContext(BooleanUnaryExpressionContext.class,0);
		}
		public NotExpressionContext(BooleanUnaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).enterNotExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).exitNotExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExpressionVisitor ) return ((FilterExpressionVisitor<? extends T>)visitor).visitNotExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BooleanUnaryExpressionContext booleanUnaryExpression() throws RecognitionException {
		BooleanUnaryExpressionContext _localctx = new BooleanUnaryExpressionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_booleanUnaryExpression);
		try {
			setState(38);
			switch (_input.LA(1)) {
			case NOT:
				_localctx = new NotExpressionContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(35);
				match(NOT);
				setState(36);
				booleanUnaryExpression();
				}
				break;
			case T__4:
			case T__5:
			case T__6:
			case FLOATING_POINT_LITERAL:
			case INTEGER_LITERAL:
			case LPAREN:
			case STRING_LITERAL:
			case VARIABLE_SIMPLE_IDENTIFIER:
			case VARIABLE_QUALIFIED_IDENTIFIER:
				_localctx = new ToBooleanOperandExpressionContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(37);
				booleanOperandExpression();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ComparisonExpressionContext extends ParserRuleContext {
		public ComparisonExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comparisonExpression; }
	 
		public ComparisonExpressionContext() { }
		public void copyFrom(ComparisonExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class GreaterThanOrEqualsExpressionContext extends ComparisonExpressionContext {
		public ComparisonExpressionContext comparisonExpression() {
			return getRuleContext(ComparisonExpressionContext.class,0);
		}
		public TerminalNode GE() { return getToken(FilterExpressionParser.GE, 0); }
		public BooleanOperandExpressionContext booleanOperandExpression() {
			return getRuleContext(BooleanOperandExpressionContext.class,0);
		}
		public GreaterThanOrEqualsExpressionContext(ComparisonExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).enterGreaterThanOrEqualsExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).exitGreaterThanOrEqualsExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExpressionVisitor ) return ((FilterExpressionVisitor<? extends T>)visitor).visitGreaterThanOrEqualsExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LessThanOrEqualsExpressionContext extends ComparisonExpressionContext {
		public ComparisonExpressionContext comparisonExpression() {
			return getRuleContext(ComparisonExpressionContext.class,0);
		}
		public TerminalNode LE() { return getToken(FilterExpressionParser.LE, 0); }
		public BooleanOperandExpressionContext booleanOperandExpression() {
			return getRuleContext(BooleanOperandExpressionContext.class,0);
		}
		public LessThanOrEqualsExpressionContext(ComparisonExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).enterLessThanOrEqualsExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).exitLessThanOrEqualsExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExpressionVisitor ) return ((FilterExpressionVisitor<? extends T>)visitor).visitLessThanOrEqualsExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GreaterThanExpressionContext extends ComparisonExpressionContext {
		public ComparisonExpressionContext comparisonExpression() {
			return getRuleContext(ComparisonExpressionContext.class,0);
		}
		public TerminalNode GT() { return getToken(FilterExpressionParser.GT, 0); }
		public BooleanOperandExpressionContext booleanOperandExpression() {
			return getRuleContext(BooleanOperandExpressionContext.class,0);
		}
		public GreaterThanExpressionContext(ComparisonExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).enterGreaterThanExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).exitGreaterThanExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExpressionVisitor ) return ((FilterExpressionVisitor<? extends T>)visitor).visitGreaterThanExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ToBooleanUnaryExpressionContext extends ComparisonExpressionContext {
		public BooleanUnaryExpressionContext booleanUnaryExpression() {
			return getRuleContext(BooleanUnaryExpressionContext.class,0);
		}
		public ToBooleanUnaryExpressionContext(ComparisonExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).enterToBooleanUnaryExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).exitToBooleanUnaryExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExpressionVisitor ) return ((FilterExpressionVisitor<? extends T>)visitor).visitToBooleanUnaryExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LessThanExpressionContext extends ComparisonExpressionContext {
		public ComparisonExpressionContext comparisonExpression() {
			return getRuleContext(ComparisonExpressionContext.class,0);
		}
		public TerminalNode LT() { return getToken(FilterExpressionParser.LT, 0); }
		public BooleanOperandExpressionContext booleanOperandExpression() {
			return getRuleContext(BooleanOperandExpressionContext.class,0);
		}
		public LessThanExpressionContext(ComparisonExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).enterLessThanExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).exitLessThanExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExpressionVisitor ) return ((FilterExpressionVisitor<? extends T>)visitor).visitLessThanExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComparisonExpressionContext comparisonExpression() throws RecognitionException {
		return comparisonExpression(0);
	}

	private ComparisonExpressionContext comparisonExpression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ComparisonExpressionContext _localctx = new ComparisonExpressionContext(_ctx, _parentState);
		ComparisonExpressionContext _prevctx = _localctx;
		int _startState = 4;
		enterRecursionRule(_localctx, 4, RULE_comparisonExpression, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			_localctx = new ToBooleanUnaryExpressionContext(_localctx);
			_ctx = _localctx;
			_prevctx = _localctx;

			setState(41);
			booleanUnaryExpression();
			}
			_ctx.stop = _input.LT(-1);
			setState(57);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(55);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
					case 1:
						{
						_localctx = new GreaterThanExpressionContext(new ComparisonExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_comparisonExpression);
						setState(43);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(44);
						match(GT);
						setState(45);
						booleanOperandExpression();
						}
						break;
					case 2:
						{
						_localctx = new GreaterThanOrEqualsExpressionContext(new ComparisonExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_comparisonExpression);
						setState(46);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(47);
						match(GE);
						setState(48);
						booleanOperandExpression();
						}
						break;
					case 3:
						{
						_localctx = new LessThanExpressionContext(new ComparisonExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_comparisonExpression);
						setState(49);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(50);
						match(LT);
						setState(51);
						booleanOperandExpression();
						}
						break;
					case 4:
						{
						_localctx = new LessThanOrEqualsExpressionContext(new ComparisonExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_comparisonExpression);
						setState(52);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(53);
						match(LE);
						setState(54);
						booleanOperandExpression();
						}
						break;
					}
					} 
				}
				setState(59);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class EqualityExpressionContext extends ParserRuleContext {
		public EqualityExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_equalityExpression; }
	 
		public EqualityExpressionContext() { }
		public void copyFrom(EqualityExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class NotEqualsExpressionContext extends EqualityExpressionContext {
		public EqualityExpressionContext equalityExpression() {
			return getRuleContext(EqualityExpressionContext.class,0);
		}
		public TerminalNode NEQ() { return getToken(FilterExpressionParser.NEQ, 0); }
		public ComparisonExpressionContext comparisonExpression() {
			return getRuleContext(ComparisonExpressionContext.class,0);
		}
		public NotEqualsExpressionContext(EqualityExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).enterNotEqualsExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).exitNotEqualsExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExpressionVisitor ) return ((FilterExpressionVisitor<? extends T>)visitor).visitNotEqualsExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ToComparisonExpressionContext extends EqualityExpressionContext {
		public ComparisonExpressionContext comparisonExpression() {
			return getRuleContext(ComparisonExpressionContext.class,0);
		}
		public ToComparisonExpressionContext(EqualityExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).enterToComparisonExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).exitToComparisonExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExpressionVisitor ) return ((FilterExpressionVisitor<? extends T>)visitor).visitToComparisonExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class EqualsExpressionContext extends EqualityExpressionContext {
		public EqualityExpressionContext equalityExpression() {
			return getRuleContext(EqualityExpressionContext.class,0);
		}
		public TerminalNode EQ() { return getToken(FilterExpressionParser.EQ, 0); }
		public ComparisonExpressionContext comparisonExpression() {
			return getRuleContext(ComparisonExpressionContext.class,0);
		}
		public EqualsExpressionContext(EqualityExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).enterEqualsExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).exitEqualsExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExpressionVisitor ) return ((FilterExpressionVisitor<? extends T>)visitor).visitEqualsExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EqualityExpressionContext equalityExpression() throws RecognitionException {
		return equalityExpression(0);
	}

	private EqualityExpressionContext equalityExpression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		EqualityExpressionContext _localctx = new EqualityExpressionContext(_ctx, _parentState);
		EqualityExpressionContext _prevctx = _localctx;
		int _startState = 6;
		enterRecursionRule(_localctx, 6, RULE_equalityExpression, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			_localctx = new ToComparisonExpressionContext(_localctx);
			_ctx = _localctx;
			_prevctx = _localctx;

			setState(61);
			comparisonExpression(0);
			}
			_ctx.stop = _input.LT(-1);
			setState(71);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(69);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
					case 1:
						{
						_localctx = new EqualsExpressionContext(new EqualityExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_equalityExpression);
						setState(63);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(64);
						match(EQ);
						setState(65);
						comparisonExpression(0);
						}
						break;
					case 2:
						{
						_localctx = new NotEqualsExpressionContext(new EqualityExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_equalityExpression);
						setState(66);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(67);
						match(NEQ);
						setState(68);
						comparisonExpression(0);
						}
						break;
					}
					} 
				}
				setState(73);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class ExpressionContext extends ParserRuleContext {
		public LogicalOrExpressionContext logicalOrExpression() {
			return getRuleContext(LogicalOrExpressionContext.class,0);
		}
		public TerminalNode EOF() { return getToken(FilterExpressionParser.EOF, 0); }
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).exitExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExpressionVisitor ) return ((FilterExpressionVisitor<? extends T>)visitor).visitExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_expression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(74);
			logicalOrExpression(0);
			setState(75);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FilterExpressionContext extends ParserRuleContext {
		public Token filterType;
		public Token filter;
		public TerminalNode VARIABLE_SIMPLE_IDENTIFIER() { return getToken(FilterExpressionParser.VARIABLE_SIMPLE_IDENTIFIER, 0); }
		public TerminalNode STRING_LITERAL() { return getToken(FilterExpressionParser.STRING_LITERAL, 0); }
		public FilterExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_filterExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).enterFilterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).exitFilterExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExpressionVisitor ) return ((FilterExpressionVisitor<? extends T>)visitor).visitFilterExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FilterExpressionContext filterExpression() throws RecognitionException {
		FilterExpressionContext _localctx = new FilterExpressionContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_filterExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(77);
			((FilterExpressionContext)_localctx).filterType = match(VARIABLE_SIMPLE_IDENTIFIER);
			setState(78);
			match(T__0);
			setState(79);
			((FilterExpressionContext)_localctx).filter = match(STRING_LITERAL);
			setState(80);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FilterByCountExpressionContext extends ParserRuleContext {
		public Token filterType;
		public Token filter;
		public Token operator;
		public Token value;
		public List<TerminalNode> COMMA() { return getTokens(FilterExpressionParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(FilterExpressionParser.COMMA, i);
		}
		public TerminalNode VARIABLE_SIMPLE_IDENTIFIER() { return getToken(FilterExpressionParser.VARIABLE_SIMPLE_IDENTIFIER, 0); }
		public List<TerminalNode> STRING_LITERAL() { return getTokens(FilterExpressionParser.STRING_LITERAL); }
		public TerminalNode STRING_LITERAL(int i) {
			return getToken(FilterExpressionParser.STRING_LITERAL, i);
		}
		public TerminalNode INTEGER_LITERAL() { return getToken(FilterExpressionParser.INTEGER_LITERAL, 0); }
		public FilterByCountExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_filterByCountExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).enterFilterByCountExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).exitFilterByCountExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExpressionVisitor ) return ((FilterExpressionVisitor<? extends T>)visitor).visitFilterByCountExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FilterByCountExpressionContext filterByCountExpression() throws RecognitionException {
		FilterByCountExpressionContext _localctx = new FilterByCountExpressionContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_filterByCountExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(82);
			((FilterByCountExpressionContext)_localctx).filterType = match(VARIABLE_SIMPLE_IDENTIFIER);
			setState(83);
			match(T__1);
			setState(84);
			((FilterByCountExpressionContext)_localctx).filter = match(STRING_LITERAL);
			setState(85);
			match(COMMA);
			setState(86);
			match(T__2);
			setState(87);
			((FilterByCountExpressionContext)_localctx).operator = match(STRING_LITERAL);
			setState(88);
			match(COMMA);
			setState(89);
			match(T__3);
			setState(90);
			((FilterByCountExpressionContext)_localctx).value = match(INTEGER_LITERAL);
			setState(91);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctionCallExpressionContext extends ParserRuleContext {
		public Token functionName;
		public TerminalNode LPAREN() { return getToken(FilterExpressionParser.LPAREN, 0); }
		public FunctionParametersContext functionParameters() {
			return getRuleContext(FunctionParametersContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(FilterExpressionParser.RPAREN, 0); }
		public TerminalNode VARIABLE_SIMPLE_IDENTIFIER() { return getToken(FilterExpressionParser.VARIABLE_SIMPLE_IDENTIFIER, 0); }
		public FunctionCallExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionCallExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).enterFunctionCallExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).exitFunctionCallExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExpressionVisitor ) return ((FilterExpressionVisitor<? extends T>)visitor).visitFunctionCallExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionCallExpressionContext functionCallExpression() throws RecognitionException {
		FunctionCallExpressionContext _localctx = new FunctionCallExpressionContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_functionCallExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(93);
			((FunctionCallExpressionContext)_localctx).functionName = match(VARIABLE_SIMPLE_IDENTIFIER);
			setState(94);
			match(LPAREN);
			setState(95);
			functionParameters();
			setState(96);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctionParametersContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public List<TerminalNode> COMMA() { return getTokens(FilterExpressionParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(FilterExpressionParser.COMMA, i);
		}
		public List<LiteralContext> literal() {
			return getRuleContexts(LiteralContext.class);
		}
		public LiteralContext literal(int i) {
			return getRuleContext(LiteralContext.class,i);
		}
		public FunctionParametersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionParameters; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).enterFunctionParameters(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).exitFunctionParameters(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExpressionVisitor ) return ((FilterExpressionVisitor<? extends T>)visitor).visitFunctionParameters(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionParametersContext functionParameters() throws RecognitionException {
		FunctionParametersContext _localctx = new FunctionParametersContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_functionParameters);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(98);
			identifier();
			setState(103);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(99);
				match(COMMA);
				setState(100);
				literal();
				}
				}
				setState(105);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdentifierContext extends ParserRuleContext {
		public TerminalNode VARIABLE_SIMPLE_IDENTIFIER() { return getToken(FilterExpressionParser.VARIABLE_SIMPLE_IDENTIFIER, 0); }
		public TerminalNode VARIABLE_QUALIFIED_IDENTIFIER() { return getToken(FilterExpressionParser.VARIABLE_QUALIFIED_IDENTIFIER, 0); }
		public IdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).enterIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).exitIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExpressionVisitor ) return ((FilterExpressionVisitor<? extends T>)visitor).visitIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentifierContext identifier() throws RecognitionException {
		IdentifierContext _localctx = new IdentifierContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_identifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(106);
			_la = _input.LA(1);
			if ( !(_la==VARIABLE_SIMPLE_IDENTIFIER || _la==VARIABLE_QUALIFIED_IDENTIFIER) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LiteralContext extends ParserRuleContext {
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
	 
		public LiteralContext() { }
		public void copyFrom(LiteralContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class StringLiteralContext extends LiteralContext {
		public TerminalNode STRING_LITERAL() { return getToken(FilterExpressionParser.STRING_LITERAL, 0); }
		public StringLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).enterStringLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).exitStringLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExpressionVisitor ) return ((FilterExpressionVisitor<? extends T>)visitor).visitStringLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FloatingPointLiteralContext extends LiteralContext {
		public TerminalNode FLOATING_POINT_LITERAL() { return getToken(FilterExpressionParser.FLOATING_POINT_LITERAL, 0); }
		public FloatingPointLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).enterFloatingPointLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).exitFloatingPointLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExpressionVisitor ) return ((FilterExpressionVisitor<? extends T>)visitor).visitFloatingPointLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BooleanLiteralContext extends LiteralContext {
		public BooleanLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).enterBooleanLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).exitBooleanLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExpressionVisitor ) return ((FilterExpressionVisitor<? extends T>)visitor).visitBooleanLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NullLiteralContext extends LiteralContext {
		public NullLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).enterNullLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).exitNullLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExpressionVisitor ) return ((FilterExpressionVisitor<? extends T>)visitor).visitNullLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IntegerLiteralContext extends LiteralContext {
		public TerminalNode INTEGER_LITERAL() { return getToken(FilterExpressionParser.INTEGER_LITERAL, 0); }
		public IntegerLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).enterIntegerLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).exitIntegerLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExpressionVisitor ) return ((FilterExpressionVisitor<? extends T>)visitor).visitIntegerLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_literal);
		int _la;
		try {
			setState(113);
			switch (_input.LA(1)) {
			case FLOATING_POINT_LITERAL:
				_localctx = new FloatingPointLiteralContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(108);
				match(FLOATING_POINT_LITERAL);
				}
				break;
			case INTEGER_LITERAL:
				_localctx = new IntegerLiteralContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(109);
				match(INTEGER_LITERAL);
				}
				break;
			case T__4:
			case T__5:
				_localctx = new BooleanLiteralContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(110);
				_la = _input.LA(1);
				if ( !(_la==T__4 || _la==T__5) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				}
				break;
			case T__6:
				_localctx = new NullLiteralContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(111);
				match(T__6);
				}
				break;
			case STRING_LITERAL:
				_localctx = new StringLiteralContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(112);
				match(STRING_LITERAL);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LogicalAndExpressionContext extends ParserRuleContext {
		public LogicalAndExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logicalAndExpression; }
	 
		public LogicalAndExpressionContext() { }
		public void copyFrom(LogicalAndExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class AndExpressionContext extends LogicalAndExpressionContext {
		public LogicalAndExpressionContext logicalAndExpression() {
			return getRuleContext(LogicalAndExpressionContext.class,0);
		}
		public TerminalNode AND() { return getToken(FilterExpressionParser.AND, 0); }
		public EqualityExpressionContext equalityExpression() {
			return getRuleContext(EqualityExpressionContext.class,0);
		}
		public AndExpressionContext(LogicalAndExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).enterAndExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).exitAndExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExpressionVisitor ) return ((FilterExpressionVisitor<? extends T>)visitor).visitAndExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ToEqualityExpressionContext extends LogicalAndExpressionContext {
		public EqualityExpressionContext equalityExpression() {
			return getRuleContext(EqualityExpressionContext.class,0);
		}
		public ToEqualityExpressionContext(LogicalAndExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).enterToEqualityExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).exitToEqualityExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExpressionVisitor ) return ((FilterExpressionVisitor<? extends T>)visitor).visitToEqualityExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LogicalAndExpressionContext logicalAndExpression() throws RecognitionException {
		return logicalAndExpression(0);
	}

	private LogicalAndExpressionContext logicalAndExpression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		LogicalAndExpressionContext _localctx = new LogicalAndExpressionContext(_ctx, _parentState);
		LogicalAndExpressionContext _prevctx = _localctx;
		int _startState = 22;
		enterRecursionRule(_localctx, 22, RULE_logicalAndExpression, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			_localctx = new ToEqualityExpressionContext(_localctx);
			_ctx = _localctx;
			_prevctx = _localctx;

			setState(116);
			equalityExpression(0);
			}
			_ctx.stop = _input.LT(-1);
			setState(123);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new AndExpressionContext(new LogicalAndExpressionContext(_parentctx, _parentState));
					pushNewRecursionContext(_localctx, _startState, RULE_logicalAndExpression);
					setState(118);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(119);
					match(AND);
					setState(120);
					equalityExpression(0);
					}
					} 
				}
				setState(125);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class LogicalOrExpressionContext extends ParserRuleContext {
		public LogicalOrExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logicalOrExpression; }
	 
		public LogicalOrExpressionContext() { }
		public void copyFrom(LogicalOrExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ToLogicalAndExpressionContext extends LogicalOrExpressionContext {
		public LogicalAndExpressionContext logicalAndExpression() {
			return getRuleContext(LogicalAndExpressionContext.class,0);
		}
		public ToLogicalAndExpressionContext(LogicalOrExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).enterToLogicalAndExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).exitToLogicalAndExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExpressionVisitor ) return ((FilterExpressionVisitor<? extends T>)visitor).visitToLogicalAndExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class OrExpressionContext extends LogicalOrExpressionContext {
		public LogicalOrExpressionContext logicalOrExpression() {
			return getRuleContext(LogicalOrExpressionContext.class,0);
		}
		public TerminalNode OR() { return getToken(FilterExpressionParser.OR, 0); }
		public LogicalAndExpressionContext logicalAndExpression() {
			return getRuleContext(LogicalAndExpressionContext.class,0);
		}
		public OrExpressionContext(LogicalOrExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).enterOrExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).exitOrExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExpressionVisitor ) return ((FilterExpressionVisitor<? extends T>)visitor).visitOrExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LogicalOrExpressionContext logicalOrExpression() throws RecognitionException {
		return logicalOrExpression(0);
	}

	private LogicalOrExpressionContext logicalOrExpression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		LogicalOrExpressionContext _localctx = new LogicalOrExpressionContext(_ctx, _parentState);
		LogicalOrExpressionContext _prevctx = _localctx;
		int _startState = 24;
		enterRecursionRule(_localctx, 24, RULE_logicalOrExpression, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			_localctx = new ToLogicalAndExpressionContext(_localctx);
			_ctx = _localctx;
			_prevctx = _localctx;

			setState(127);
			logicalAndExpression(0);
			}
			_ctx.stop = _input.LT(-1);
			setState(134);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new OrExpressionContext(new LogicalOrExpressionContext(_parentctx, _parentState));
					pushNewRecursionContext(_localctx, _startState, RULE_logicalOrExpression);
					setState(129);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(130);
					match(OR);
					setState(131);
					logicalAndExpression(0);
					}
					} 
				}
				setState(136);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class LogicalTermContext extends ParserRuleContext {
		public LogicalTermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logicalTerm; }
	 
		public LogicalTermContext() { }
		public void copyFrom(LogicalTermContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ToFilterByCountExpressionContext extends LogicalTermContext {
		public FilterByCountExpressionContext filterByCountExpression() {
			return getRuleContext(FilterByCountExpressionContext.class,0);
		}
		public ToFilterByCountExpressionContext(LogicalTermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).enterToFilterByCountExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).exitToFilterByCountExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExpressionVisitor ) return ((FilterExpressionVisitor<? extends T>)visitor).visitToFilterByCountExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ToIdentifierContext extends LogicalTermContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ToIdentifierContext(LogicalTermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).enterToIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).exitToIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExpressionVisitor ) return ((FilterExpressionVisitor<? extends T>)visitor).visitToIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ToFilterExpressionContext extends LogicalTermContext {
		public FilterExpressionContext filterExpression() {
			return getRuleContext(FilterExpressionContext.class,0);
		}
		public ToFilterExpressionContext(LogicalTermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).enterToFilterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).exitToFilterExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExpressionVisitor ) return ((FilterExpressionVisitor<? extends T>)visitor).visitToFilterExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ToLiteralContext extends LogicalTermContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public ToLiteralContext(LogicalTermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).enterToLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).exitToLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExpressionVisitor ) return ((FilterExpressionVisitor<? extends T>)visitor).visitToLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ToFunctionCallExpressionContext extends LogicalTermContext {
		public FunctionCallExpressionContext functionCallExpression() {
			return getRuleContext(FunctionCallExpressionContext.class,0);
		}
		public ToFunctionCallExpressionContext(LogicalTermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).enterToFunctionCallExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExpressionListener ) ((FilterExpressionListener)listener).exitToFunctionCallExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExpressionVisitor ) return ((FilterExpressionVisitor<? extends T>)visitor).visitToFunctionCallExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LogicalTermContext logicalTerm() throws RecognitionException {
		LogicalTermContext _localctx = new LogicalTermContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_logicalTerm);
		try {
			setState(142);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				_localctx = new ToIdentifierContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(137);
				identifier();
				}
				break;
			case 2:
				_localctx = new ToLiteralContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(138);
				literal();
				}
				break;
			case 3:
				_localctx = new ToFunctionCallExpressionContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(139);
				functionCallExpression();
				}
				break;
			case 4:
				_localctx = new ToFilterExpressionContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(140);
				filterExpression();
				}
				break;
			case 5:
				_localctx = new ToFilterByCountExpressionContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(141);
				filterByCountExpression();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 2:
			return comparisonExpression_sempred((ComparisonExpressionContext)_localctx, predIndex);
		case 3:
			return equalityExpression_sempred((EqualityExpressionContext)_localctx, predIndex);
		case 11:
			return logicalAndExpression_sempred((LogicalAndExpressionContext)_localctx, predIndex);
		case 12:
			return logicalOrExpression_sempred((LogicalOrExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean comparisonExpression_sempred(ComparisonExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 5);
		case 1:
			return precpred(_ctx, 4);
		case 2:
			return precpred(_ctx, 3);
		case 3:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean equalityExpression_sempred(EqualityExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 4:
			return precpred(_ctx, 3);
		case 5:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean logicalAndExpression_sempred(LogicalAndExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 6:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean logicalOrExpression_sempred(LogicalOrExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 7:
			return precpred(_ctx, 2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\33\u0093\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\3\2\3\2\3\2\3\2\3\2\5\2$\n\2"+
		"\3\3\3\3\3\3\5\3)\n\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4"+
		"\3\4\3\4\3\4\7\4:\n\4\f\4\16\4=\13\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3"+
		"\5\7\5H\n\5\f\5\16\5K\13\5\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b"+
		"\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\7\nh"+
		"\n\n\f\n\16\nk\13\n\3\13\3\13\3\f\3\f\3\f\3\f\3\f\5\ft\n\f\3\r\3\r\3\r"+
		"\3\r\3\r\3\r\7\r|\n\r\f\r\16\r\177\13\r\3\16\3\16\3\16\3\16\3\16\3\16"+
		"\7\16\u0087\n\16\f\16\16\16\u008a\13\16\3\17\3\17\3\17\3\17\3\17\5\17"+
		"\u0091\n\17\3\17\2\6\6\b\30\32\20\2\4\6\b\n\f\16\20\22\24\26\30\32\34"+
		"\2\4\3\2\31\32\3\2\7\b\u0097\2#\3\2\2\2\4(\3\2\2\2\6*\3\2\2\2\b>\3\2\2"+
		"\2\nL\3\2\2\2\fO\3\2\2\2\16T\3\2\2\2\20_\3\2\2\2\22d\3\2\2\2\24l\3\2\2"+
		"\2\26s\3\2\2\2\30u\3\2\2\2\32\u0080\3\2\2\2\34\u0090\3\2\2\2\36$\5\34"+
		"\17\2\37 \7\23\2\2 !\5\32\16\2!\"\7\24\2\2\"$\3\2\2\2#\36\3\2\2\2#\37"+
		"\3\2\2\2$\3\3\2\2\2%&\7\26\2\2&)\5\4\3\2\')\5\2\2\2(%\3\2\2\2(\'\3\2\2"+
		"\2)\5\3\2\2\2*+\b\4\1\2+,\5\4\3\2,;\3\2\2\2-.\f\7\2\2./\7\20\2\2/:\5\2"+
		"\2\2\60\61\f\6\2\2\61\62\7\17\2\2\62:\5\2\2\2\63\64\f\5\2\2\64\65\7\25"+
		"\2\2\65:\5\2\2\2\66\67\f\4\2\2\678\7\22\2\28:\5\2\2\29-\3\2\2\29\60\3"+
		"\2\2\29\63\3\2\2\29\66\3\2\2\2:=\3\2\2\2;9\3\2\2\2;<\3\2\2\2<\7\3\2\2"+
		"\2=;\3\2\2\2>?\b\5\1\2?@\5\6\4\2@I\3\2\2\2AB\f\5\2\2BC\7\f\2\2CH\5\6\4"+
		"\2DE\f\4\2\2EF\7\16\2\2FH\5\6\4\2GA\3\2\2\2GD\3\2\2\2HK\3\2\2\2IG\3\2"+
		"\2\2IJ\3\2\2\2J\t\3\2\2\2KI\3\2\2\2LM\5\32\16\2MN\7\2\2\3N\13\3\2\2\2"+
		"OP\7\31\2\2PQ\7\3\2\2QR\7\30\2\2RS\7\24\2\2S\r\3\2\2\2TU\7\31\2\2UV\7"+
		"\4\2\2VW\7\30\2\2WX\7\13\2\2XY\7\5\2\2YZ\7\30\2\2Z[\7\13\2\2[\\\7\6\2"+
		"\2\\]\7\21\2\2]^\7\24\2\2^\17\3\2\2\2_`\7\31\2\2`a\7\23\2\2ab\5\22\n\2"+
		"bc\7\24\2\2c\21\3\2\2\2di\5\24\13\2ef\7\13\2\2fh\5\26\f\2ge\3\2\2\2hk"+
		"\3\2\2\2ig\3\2\2\2ij\3\2\2\2j\23\3\2\2\2ki\3\2\2\2lm\t\2\2\2m\25\3\2\2"+
		"\2nt\7\r\2\2ot\7\21\2\2pt\t\3\2\2qt\7\t\2\2rt\7\30\2\2sn\3\2\2\2so\3\2"+
		"\2\2sp\3\2\2\2sq\3\2\2\2sr\3\2\2\2t\27\3\2\2\2uv\b\r\1\2vw\5\b\5\2w}\3"+
		"\2\2\2xy\f\4\2\2yz\7\n\2\2z|\5\b\5\2{x\3\2\2\2|\177\3\2\2\2}{\3\2\2\2"+
		"}~\3\2\2\2~\31\3\2\2\2\177}\3\2\2\2\u0080\u0081\b\16\1\2\u0081\u0082\5"+
		"\30\r\2\u0082\u0088\3\2\2\2\u0083\u0084\f\4\2\2\u0084\u0085\7\27\2\2\u0085"+
		"\u0087\5\30\r\2\u0086\u0083\3\2\2\2\u0087\u008a\3\2\2\2\u0088\u0086\3"+
		"\2\2\2\u0088\u0089\3\2\2\2\u0089\33\3\2\2\2\u008a\u0088\3\2\2\2\u008b"+
		"\u0091\5\24\13\2\u008c\u0091\5\26\f\2\u008d\u0091\5\20\t\2\u008e\u0091"+
		"\5\f\7\2\u008f\u0091\5\16\b\2\u0090\u008b\3\2\2\2\u0090\u008c\3\2\2\2"+
		"\u0090\u008d\3\2\2\2\u0090\u008e\3\2\2\2\u0090\u008f\3\2\2\2\u0091\35"+
		"\3\2\2\2\r#(9;GIis}\u0088\u0090";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
