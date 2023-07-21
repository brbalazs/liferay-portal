/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.solr7.internal.query;

import com.liferay.petra.string.StringPool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author André de Oliveira
 */
public class SolrPostProcesor {

	public SolrPostProcesor(String query, String keywords) {
		_query = query;
		_keywords = keywords;

		_sb = new StringBuilder(query.length());
	}

	public String postProcess() {
		while (findPhrase()) {
			appendPhrase();
		}

		appendRemainder();

		return _sb.toString();
	}

	protected void appendPhrase() {
		String before = _query.substring(_index, _firstQuoteIndex);

		_sb.append(before);

		_index = _secondQuoteIndex + 1;

		String phrase = _query.substring(_firstQuoteIndex, _index);

		if (_hasQuestionMark) {
			String regex = buildRegex(phrase);

			Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

			Matcher matcher = pattern.matcher(_keywords);

			if (matcher.find()) {
				_sb.append(matcher.group());

				return;
			}
		}

		_sb.append(phrase);
	}

	protected void appendRemainder() {
		_sb.append(_query.substring(_index));
	}

	protected String buildRegex(String phrase) {
		StringBuilder sb = new StringBuilder(phrase.length());

		int x = 0;

		while (true) {
			int y = phrase.indexOf(StringPool.QUESTION, x);

			if (y == -1) {
				break;
			}

			sb.append(Pattern.quote(phrase.substring(x, y)));
			sb.append(StringPool.PERIOD);
			sb.append(StringPool.PLUS);

			x = y + 1;
		}

		sb.append(Pattern.quote(phrase.substring(x)));

		return sb.toString();
	}

	protected boolean findPhrase() {
		_firstQuoteIndex = _query.indexOf(StringPool.QUOTE, _index);

		if (_firstQuoteIndex == -1) {
			return false;
		}

		_secondQuoteIndex = _query.indexOf(
			StringPool.QUOTE, _firstQuoteIndex + 1);

		if (_secondQuoteIndex == -1) {
			return false;
		}

		int questionMarkIndex = _query.indexOf(
			StringPool.QUESTION, _firstQuoteIndex);

		if (questionMarkIndex == -1) {
			_hasQuestionMark = false;
		}
		else {
			_hasQuestionMark = true;
		}

		return true;
	}

	private int _firstQuoteIndex;
	private boolean _hasQuestionMark;
	private int _index;
	private final String _keywords;
	private final String _query;
	private final StringBuilder _sb;
	private int _secondQuoteIndex;

}