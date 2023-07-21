/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.taglib;

import com.liferay.portal.kernel.io.unsync.UnsyncStringWriter;
import com.liferay.taglib.servlet.PageContextFactoryUtil;
import com.liferay.taglib.servlet.PipingPageContext;

import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.Tag;

/**
 * @author Shuyang Zhou
 */
public interface DirectTag extends Tag {

	public default void doBodyTag(
			HttpServletRequest request, HttpServletResponse response,
			Consumer<PageContext> consumer)
		throws JspException {

		doBodyTag(PageContextFactoryUtil.create(request, response), consumer);
	}

	public default void doBodyTag(
			PageContext pageContext, Consumer<PageContext> consumer)
		throws JspException {

		setPageContext(pageContext);

		int start = doStartTag();

		if (start == SKIP_BODY) {
			doEndTag();

			return;
		}

		if (this instanceof BodyTag) {
			BodyTag bodyTag = (BodyTag)this;

			if (start == BodyTag.EVAL_BODY_BUFFERED) {
				JspWriter jspWriter = pageContext.pushBody();

				bodyTag.setBodyContent((BodyContent)jspWriter);

				bodyTag.doInitBody();
			}

			do {
				consumer.accept(pageContext);
			}
			while (bodyTag.doAfterBody() == BodyTag.EVAL_BODY_AGAIN);

			if (start == BodyTag.EVAL_BODY_BUFFERED) {
				pageContext.popBody();
			}
		}
		else {
			consumer.accept(pageContext);
		}

		doEndTag();
	}

	public default String doBodyTagAsString(
			HttpServletRequest request, HttpServletResponse response,
			Consumer<PageContext> consumer)
		throws JspException {

		return doBodyTagAsString(
			PageContextFactoryUtil.create(request, response), consumer);
	}

	public default String doBodyTagAsString(
			PageContext pageContext, Consumer<PageContext> consumer)
		throws JspException {

		UnsyncStringWriter unsyncStringWriter = new UnsyncStringWriter();

		doBodyTag(
			new PipingPageContext(pageContext, unsyncStringWriter), consumer);

		return unsyncStringWriter.toString();
	}

	public default void doTag(
			HttpServletRequest request, HttpServletResponse response)
		throws JspException {

		doTag(PageContextFactoryUtil.create(request, response));
	}

	public default void doTag(PageContext pageContext) throws JspException {
		setPageContext(pageContext);

		doStartTag();
		doEndTag();
	}

	public default String doTagAsString(
			HttpServletRequest request, HttpServletResponse response)
		throws JspException {

		return doTagAsString(PageContextFactoryUtil.create(request, response));
	}

	public default String doTagAsString(PageContext pageContext)
		throws JspException {

		UnsyncStringWriter unsyncStringWriter = new UnsyncStringWriter();

		doTag(new PipingPageContext(pageContext, unsyncStringWriter));

		return unsyncStringWriter.toString();
	}

}