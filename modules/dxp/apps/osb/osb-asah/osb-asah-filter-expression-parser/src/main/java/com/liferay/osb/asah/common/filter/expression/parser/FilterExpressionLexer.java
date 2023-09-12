// Generated from FilterExpression.g4 by ANTLR 4.5.3

/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.filter.expression.parser;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class FilterExpressionLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, AND=8, COMMA=9, 
		EQ=10, FLOATING_POINT_LITERAL=11, NEQ=12, GE=13, GT=14, INTEGER_LITERAL=15, 
		LE=16, LPAREN=17, RPAREN=18, LT=19, NOT=20, OR=21, STRING_LITERAL=22, 
		VARIABLE_SIMPLE_IDENTIFIER=23, VARIABLE_QUALIFIED_IDENTIFIER=24, WS=25;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "AND", "COMMA", 
		"EQ", "FLOATING_POINT_LITERAL", "NEQ", "GE", "GT", "INTEGER_LITERAL", 
		"LE", "LPAREN", "RPAREN", "LT", "NOT", "OR", "STRING_LITERAL", "VARIABLE_SIMPLE_IDENTIFIER", 
		"VARIABLE_QUALIFIED_IDENTIFIER", "DIGITS", "MINUS", "NAME_CHAR", "WS"
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


	public FilterExpressionLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "FilterExpression.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\33\u011b\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\3\2\3\2\3\2\3\2\3\2\3\2"+
		"\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4"+
		"\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3"+
		"\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t"+
		"\3\t\3\t\3\t\3\t\3\t\5\t\u008d\n\t\3\n\3\n\3\13\3\13\3\13\5\13\u0094\n"+
		"\13\3\f\5\f\u0097\n\f\3\f\3\f\3\f\5\f\u009c\n\f\3\f\5\f\u009f\n\f\3\f"+
		"\3\f\5\f\u00a3\n\f\3\r\3\r\3\r\3\16\3\16\3\16\3\17\3\17\3\17\3\20\5\20"+
		"\u00af\n\20\3\20\3\20\3\21\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\24"+
		"\3\25\3\25\3\25\3\25\3\25\3\25\5\25\u00c3\n\25\3\26\3\26\3\26\3\26\3\26"+
		"\3\26\3\26\5\26\u00cc\n\26\3\27\3\27\3\27\3\27\7\27\u00d2\n\27\f\27\16"+
		"\27\u00d5\13\27\3\27\3\27\3\27\3\27\3\27\7\27\u00dc\n\27\f\27\16\27\u00df"+
		"\13\27\3\27\5\27\u00e2\n\27\3\30\7\30\u00e5\n\30\f\30\16\30\u00e8\13\30"+
		"\3\31\7\31\u00eb\n\31\f\31\16\31\u00ee\13\31\3\31\3\31\7\31\u00f2\n\31"+
		"\f\31\16\31\u00f5\13\31\3\31\7\31\u00f8\n\31\f\31\16\31\u00fb\13\31\3"+
		"\31\3\31\7\31\u00ff\n\31\f\31\16\31\u0102\13\31\3\31\3\31\3\31\3\31\3"+
		"\31\3\31\5\31\u010a\n\31\3\32\6\32\u010d\n\32\r\32\16\32\u010e\3\33\3"+
		"\33\3\34\3\34\3\35\6\35\u0116\n\35\r\35\16\35\u0117\3\35\3\35\2\2\36\3"+
		"\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37"+
		"\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\2\65\2\67\29\33\3\2\7\3"+
		"\2$$\3\2))\3\2\62;\6\2\62;C\\aac|\5\2\13\f\16\17\"\"\u0131\2\3\3\2\2\2"+
		"\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2"+
		"\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2"+
		"\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2"+
		"\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2"+
		"\2\29\3\2\2\2\3;\3\2\2\2\5K\3\2\2\2\7b\3\2\2\2\tl\3\2\2\2\13s\3\2\2\2"+
		"\rx\3\2\2\2\17~\3\2\2\2\21\u008c\3\2\2\2\23\u008e\3\2\2\2\25\u0093\3\2"+
		"\2\2\27\u00a2\3\2\2\2\31\u00a4\3\2\2\2\33\u00a7\3\2\2\2\35\u00aa\3\2\2"+
		"\2\37\u00ae\3\2\2\2!\u00b2\3\2\2\2#\u00b5\3\2\2\2%\u00b7\3\2\2\2\'\u00b9"+
		"\3\2\2\2)\u00c2\3\2\2\2+\u00cb\3\2\2\2-\u00e1\3\2\2\2/\u00e6\3\2\2\2\61"+
		"\u0109\3\2\2\2\63\u010c\3\2\2\2\65\u0110\3\2\2\2\67\u0112\3\2\2\29\u0115"+
		"\3\2\2\2;<\7\60\2\2<=\7h\2\2=>\7k\2\2>?\7n\2\2?@\7v\2\2@A\7g\2\2AB\7t"+
		"\2\2BC\7*\2\2CD\7h\2\2DE\7k\2\2EF\7n\2\2FG\7v\2\2GH\7g\2\2HI\7t\2\2IJ"+
		"\7?\2\2J\4\3\2\2\2KL\7\60\2\2LM\7h\2\2MN\7k\2\2NO\7n\2\2OP\7v\2\2PQ\7"+
		"g\2\2QR\7t\2\2RS\7D\2\2ST\7{\2\2TU\7E\2\2UV\7q\2\2VW\7w\2\2WX\7p\2\2X"+
		"Y\7v\2\2YZ\7*\2\2Z[\7h\2\2[\\\7k\2\2\\]\7n\2\2]^\7v\2\2^_\7g\2\2_`\7t"+
		"\2\2`a\7?\2\2a\6\3\2\2\2bc\7q\2\2cd\7r\2\2de\7g\2\2ef\7t\2\2fg\7c\2\2"+
		"gh\7v\2\2hi\7q\2\2ij\7t\2\2jk\7?\2\2k\b\3\2\2\2lm\7x\2\2mn\7c\2\2no\7"+
		"n\2\2op\7w\2\2pq\7g\2\2qr\7?\2\2r\n\3\2\2\2st\7v\2\2tu\7t\2\2uv\7w\2\2"+
		"vw\7g\2\2w\f\3\2\2\2xy\7h\2\2yz\7c\2\2z{\7n\2\2{|\7u\2\2|}\7g\2\2}\16"+
		"\3\2\2\2~\177\7p\2\2\177\u0080\7w\2\2\u0080\u0081\7n\2\2\u0081\u0082\7"+
		"n\2\2\u0082\20\3\2\2\2\u0083\u0084\7(\2\2\u0084\u008d\7(\2\2\u0085\u008d"+
		"\7(\2\2\u0086\u0087\7c\2\2\u0087\u0088\7p\2\2\u0088\u008d\7f\2\2\u0089"+
		"\u008a\7C\2\2\u008a\u008b\7P\2\2\u008b\u008d\7F\2\2\u008c\u0083\3\2\2"+
		"\2\u008c\u0085\3\2\2\2\u008c\u0086\3\2\2\2\u008c\u0089\3\2\2\2\u008d\22"+
		"\3\2\2\2\u008e\u008f\7.\2\2\u008f\24\3\2\2\2\u0090\u0091\7g\2\2\u0091"+
		"\u0094\7s\2\2\u0092\u0094\7?\2\2\u0093\u0090\3\2\2\2\u0093\u0092\3\2\2"+
		"\2\u0094\26\3\2\2\2\u0095\u0097\5\65\33\2\u0096\u0095\3\2\2\2\u0096\u0097"+
		"\3\2\2\2\u0097\u0098\3\2\2\2\u0098\u0099\5\63\32\2\u0099\u009b\7\60\2"+
		"\2\u009a\u009c\5\63\32\2\u009b\u009a\3\2\2\2\u009b\u009c\3\2\2\2\u009c"+
		"\u00a3\3\2\2\2\u009d\u009f\5\65\33\2\u009e\u009d\3\2\2\2\u009e\u009f\3"+
		"\2\2\2\u009f\u00a0\3\2\2\2\u00a0\u00a1\7\60\2\2\u00a1\u00a3\5\63\32\2"+
		"\u00a2\u0096\3\2\2\2\u00a2\u009e\3\2\2\2\u00a3\30\3\2\2\2\u00a4\u00a5"+
		"\7p\2\2\u00a5\u00a6\7g\2\2\u00a6\32\3\2\2\2\u00a7\u00a8\7i\2\2\u00a8\u00a9"+
		"\7g\2\2\u00a9\34\3\2\2\2\u00aa\u00ab\7i\2\2\u00ab\u00ac\7v\2\2\u00ac\36"+
		"\3\2\2\2\u00ad\u00af\5\65\33\2\u00ae\u00ad\3\2\2\2\u00ae\u00af\3\2\2\2"+
		"\u00af\u00b0\3\2\2\2\u00b0\u00b1\5\63\32\2\u00b1 \3\2\2\2\u00b2\u00b3"+
		"\7n\2\2\u00b3\u00b4\7g\2\2\u00b4\"\3\2\2\2\u00b5\u00b6\7*\2\2\u00b6$\3"+
		"\2\2\2\u00b7\u00b8\7+\2\2\u00b8&\3\2\2\2\u00b9\u00ba\7n\2\2\u00ba\u00bb"+
		"\7v\2\2\u00bb(\3\2\2\2\u00bc\u00bd\7p\2\2\u00bd\u00be\7q\2\2\u00be\u00c3"+
		"\7v\2\2\u00bf\u00c0\7P\2\2\u00c0\u00c1\7Q\2\2\u00c1\u00c3\7V\2\2\u00c2"+
		"\u00bc\3\2\2\2\u00c2\u00bf\3\2\2\2\u00c3*\3\2\2\2\u00c4\u00c5\7~\2\2\u00c5"+
		"\u00cc\7~\2\2\u00c6\u00cc\7~\2\2\u00c7\u00c8\7q\2\2\u00c8\u00cc\7t\2\2"+
		"\u00c9\u00ca\7Q\2\2\u00ca\u00cc\7T\2\2\u00cb\u00c4\3\2\2\2\u00cb\u00c6"+
		"\3\2\2\2\u00cb\u00c7\3\2\2\2\u00cb\u00c9\3\2\2\2\u00cc,\3\2\2\2\u00cd"+
		"\u00d3\7$\2\2\u00ce\u00cf\7$\2\2\u00cf\u00d2\7$\2\2\u00d0\u00d2\n\2\2"+
		"\2\u00d1\u00ce\3\2\2\2\u00d1\u00d0\3\2\2\2\u00d2\u00d5\3\2\2\2\u00d3\u00d1"+
		"\3\2\2\2\u00d3\u00d4\3\2\2\2\u00d4\u00d6\3\2\2\2\u00d5\u00d3\3\2\2\2\u00d6"+
		"\u00e2\7$\2\2\u00d7\u00dd\7)\2\2\u00d8\u00d9\7)\2\2\u00d9\u00dc\7)\2\2"+
		"\u00da\u00dc\n\3\2\2\u00db\u00d8\3\2\2\2\u00db\u00da\3\2\2\2\u00dc\u00df"+
		"\3\2\2\2\u00dd\u00db\3\2\2\2\u00dd\u00de\3\2\2\2\u00de\u00e0\3\2\2\2\u00df"+
		"\u00dd\3\2\2\2\u00e0\u00e2\7)\2\2\u00e1\u00cd\3\2\2\2\u00e1\u00d7\3\2"+
		"\2\2\u00e2.\3\2\2\2\u00e3\u00e5\5\67\34\2\u00e4\u00e3\3\2\2\2\u00e5\u00e8"+
		"\3\2\2\2\u00e6\u00e4\3\2\2\2\u00e6\u00e7\3\2\2\2\u00e7\60\3\2\2\2\u00e8"+
		"\u00e6\3\2\2\2\u00e9\u00eb\5\67\34\2\u00ea\u00e9\3\2\2\2\u00eb\u00ee\3"+
		"\2\2\2\u00ec\u00ea\3\2\2\2\u00ec\u00ed\3\2\2\2\u00ed\u00ef\3\2\2\2\u00ee"+
		"\u00ec\3\2\2\2\u00ef\u00f3\7\61\2\2\u00f0\u00f2\5\67\34\2\u00f1\u00f0"+
		"\3\2\2\2\u00f2\u00f5\3\2\2\2\u00f3\u00f1\3\2\2\2\u00f3\u00f4\3\2\2\2\u00f4"+
		"\u010a\3\2\2\2\u00f5\u00f3\3\2\2\2\u00f6\u00f8\5\67\34\2\u00f7\u00f6\3"+
		"\2\2\2\u00f8\u00fb\3\2\2\2\u00f9\u00f7\3\2\2\2\u00f9\u00fa\3\2\2\2\u00fa"+
		"\u00fc\3\2\2\2\u00fb\u00f9\3\2\2\2\u00fc\u0100\7\61\2\2\u00fd\u00ff\5"+
		"\67\34\2\u00fe\u00fd\3\2\2\2\u00ff\u0102\3\2\2\2\u0100\u00fe\3\2\2\2\u0100"+
		"\u0101\3\2\2\2\u0101\u0103\3\2\2\2\u0102\u0100\3\2\2\2\u0103\u0104\7\61"+
		"\2\2\u0104\u0105\7x\2\2\u0105\u0106\7c\2\2\u0106\u0107\7n\2\2\u0107\u0108"+
		"\7w\2\2\u0108\u010a\7g\2\2\u0109\u00ec\3\2\2\2\u0109\u00f9\3\2\2\2\u010a"+
		"\62\3\2\2\2\u010b\u010d\t\4\2\2\u010c\u010b\3\2\2\2\u010d\u010e\3\2\2"+
		"\2\u010e\u010c\3\2\2\2\u010e\u010f\3\2\2\2\u010f\64\3\2\2\2\u0110\u0111"+
		"\7/\2\2\u0111\66\3\2\2\2\u0112\u0113\t\5\2\2\u01138\3\2\2\2\u0114\u0116"+
		"\t\6\2\2\u0115\u0114\3\2\2\2\u0116\u0117\3\2\2\2\u0117\u0115\3\2\2\2\u0117"+
		"\u0118\3\2\2\2\u0118\u0119\3\2\2\2\u0119\u011a\b\35\2\2\u011a:\3\2\2\2"+
		"\31\2\u008c\u0093\u0096\u009b\u009e\u00a2\u00ae\u00c2\u00cb\u00d1\u00d3"+
		"\u00db\u00dd\u00e1\u00e6\u00ec\u00f3\u00f9\u0100\u0109\u010e\u0117\3\b"+
		"\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
