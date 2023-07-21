/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.template.xsl.internal;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.language.LanguageUtil;

import java.util.Locale;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;

import org.apache.xml.utils.SAXSourceLocator;
import org.apache.xml.utils.WrappedRuntimeException;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * @author Raymond Augé
 */
public class XSLErrorListener implements ErrorListener {

	public XSLErrorListener(Locale locale) {
		_locale = locale;
	}

	@Override
	public void error(TransformerException exception)
		throws TransformerException {

		setLocation(exception);

		throw exception;
	}

	@Override
	public void fatalError(TransformerException exception)
		throws TransformerException {

		setLocation(exception);

		throw exception;
	}

	public int getColumnNumber() {
		return _columnNumber;
	}

	public int getLineNumber() {
		return _lineNumber;
	}

	public String getLocation() {
		return _location;
	}

	public String getMessage() {
		return _message;
	}

	public String getMessageAndLocation() {
		return _message + " " + _location;
	}

	public void setLocation(Throwable exception) {
		SourceLocator locator = null;
		Throwable cause = exception;
		Throwable rootCause = null;

		while (cause != null) {
			if (cause instanceof SAXParseException) {
				locator = new SAXSourceLocator((SAXParseException)cause);
				rootCause = cause;
			}
			else if (cause instanceof TransformerException) {
				SourceLocator causeLocator =
					((TransformerException)cause).getLocator();

				if (causeLocator != null) {
					locator = causeLocator;
					rootCause = cause;
				}
			}

			if (cause instanceof TransformerException) {
				cause = ((TransformerException)cause).getCause();
			}
			else if (cause instanceof WrappedRuntimeException) {
				cause = ((WrappedRuntimeException)cause).getException();
			}
			else if (cause instanceof SAXException) {
				cause = ((SAXException)cause).getException();
			}
			else {
				cause = null;
			}
		}

		_message = rootCause.getMessage();

		if (locator != null) {
			_lineNumber = locator.getLineNumber();
			_columnNumber = locator.getColumnNumber();

			StringBundler sb = new StringBundler(8);

			sb.append(LanguageUtil.get(_locale, "line"));
			sb.append(" #");
			sb.append(locator.getLineNumber());
			sb.append("; ");
			sb.append(LanguageUtil.get(_locale, "column"));
			sb.append(" #");
			sb.append(locator.getColumnNumber());
			sb.append("; ");

			_location = sb.toString();
		}
		else {
			_location = StringPool.BLANK;
		}
	}

	@Override
	public void warning(TransformerException exception)
		throws TransformerException {

		setLocation(exception);

		throw exception;
	}

	private int _columnNumber;
	private int _lineNumber;
	private final Locale _locale;
	private String _location;
	private String _message;

}