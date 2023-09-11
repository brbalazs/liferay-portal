grammar FilterExpression;

options {
	language = Java;
}

@header {
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

package com.liferay.osb.asah.common.filter.expression.parser;
}

booleanOperandExpression
	: logicalTerm # ToLogicalTerm
	| LPAREN logicalOrExpression RPAREN # BooleanParenthesis
	;

booleanUnaryExpression
	: NOT booleanUnaryExpression # NotExpression
	| booleanOperandExpression # ToBooleanOperandExpression
	;

comparisonExpression
    : comparisonExpression GT booleanOperandExpression # GreaterThanExpression
    | comparisonExpression GE booleanOperandExpression # GreaterThanOrEqualsExpression
    | comparisonExpression LT booleanOperandExpression # LessThanExpression
	| comparisonExpression LE booleanOperandExpression # LessThanOrEqualsExpression
	| booleanUnaryExpression #ToBooleanUnaryExpression
	;

equalityExpression
    : equalityExpression EQ comparisonExpression # EqualsExpression
    | equalityExpression NEQ comparisonExpression # NotEqualsExpression
    | comparisonExpression #ToComparisonExpression
	;

expression
	: logicalOrExpression EOF
	;

filterExpression
	: filterType=VARIABLE_SIMPLE_IDENTIFIER '.filter(filter=' filter=STRING_LITERAL ')'
	;

filterByCountExpression
	: filterType=VARIABLE_SIMPLE_IDENTIFIER '.filterByCount(filter=' filter=STRING_LITERAL COMMA 'operator=' operator=STRING_LITERAL COMMA 'value=' value=INTEGER_LITERAL ')'
	;

functionCallExpression
	: functionName=VARIABLE_SIMPLE_IDENTIFIER LPAREN functionParameters RPAREN
	;

functionParameters
	: identifier (COMMA literal)*
	;

identifier
	: VARIABLE_SIMPLE_IDENTIFIER
    | VARIABLE_QUALIFIED_IDENTIFIER
	;

literal
	: FLOATING_POINT_LITERAL # FloatingPointLiteral
	| INTEGER_LITERAL # IntegerLiteral
	| ('true' | 'false') # BooleanLiteral
	| 'null' # NullLiteral
	| STRING_LITERAL # StringLiteral
	;

logicalAndExpression
	: logicalAndExpression AND equalityExpression # AndExpression
	| equalityExpression # ToEqualityExpression
	;

logicalOrExpression
	: logicalOrExpression OR logicalAndExpression # OrExpression
	| logicalAndExpression # ToLogicalAndExpression
	;

logicalTerm
	: identifier # ToIdentifier
	| literal # ToLiteral
	| functionCallExpression # ToFunctionCallExpression
	| filterExpression # ToFilterExpression
	| filterByCountExpression # ToFilterByCountExpression
	;

AND
	: '&&'
	| '&'
	| 'and'
	| 'AND'
	;

COMMA
	: ','
	;

EQ
	: 'eq'
	| '='
	;

FLOATING_POINT_LITERAL
    : MINUS? DIGITS '.' DIGITS?
    | MINUS? '.' DIGITS
    ;

NEQ
	: 'ne'
	;

GE
	: 'ge'
	;

GT
	: 'gt'
	;

INTEGER_LITERAL
	: MINUS? DIGITS
	;

LE
	: 'le'
	;

LPAREN
	: '('
	;

RPAREN
	: ')'
	;

LT
	: 'lt'
	;

NOT
	: 'not'
	| 'NOT'
	;

OR
	: '||'
	| '|'
	| 'or'
	| 'OR'
	;

STRING_LITERAL
	: '"' ( '""' | ~["] )* '"'
	| '\'' ( '\'\'' | ~['] )* '\''
	;

VARIABLE_SIMPLE_IDENTIFIER
	: NAME_CHAR*
	;

VARIABLE_QUALIFIED_IDENTIFIER
    : NAME_CHAR* '/' NAME_CHAR*
	| NAME_CHAR* '/' NAME_CHAR* '/value'
	;

fragment
DIGITS
    : [0-9]+
    ;

fragment
MINUS
	: '-'
	;

fragment
NAME_CHAR
   : '_'
   | 'A'..'Z' | 'a'..'z'
   | '0'..'9'
   ;

WS
	: [ \r\t\u000C\n]+ -> skip
	;
