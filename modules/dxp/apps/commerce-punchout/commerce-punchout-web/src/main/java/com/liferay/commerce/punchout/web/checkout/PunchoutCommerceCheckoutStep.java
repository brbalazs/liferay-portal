/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.commerce.punchout.web.checkout;

import com.liferay.commerce.constants.CommerceCheckoutWebKeys;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.punchout.constants.PunchoutConstants;
import com.liferay.commerce.punchout.service.PunchoutReturnService;
import com.liferay.commerce.punchout.web.internal.helper.PunchoutSessionHelper;
import com.liferay.commerce.util.BaseCommerceCheckoutStep;
import com.liferay.commerce.util.CommerceCheckoutStep;
import com.liferay.frontend.taglib.servlet.taglib.util.JSPRenderer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jaclyn Ong
 */
@Component(
	immediate = true,
	property = {
		"commerce.checkout.step.name=" + PunchoutCommerceCheckoutStep.NAME,
		"commerce.checkout.step.order:Integer=" + Integer.MIN_VALUE
	},
	service = CommerceCheckoutStep.class
)
public class PunchoutCommerceCheckoutStep extends BaseCommerceCheckoutStep {

	public static final String NAME = "punchout";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean isActive(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) {

		if (_punchoutSessionHelper.punchoutEnabled(httpServletRequest) &&
			_punchoutSessionHelper.punchoutAllowed(httpServletRequest) &&
			_punchoutSessionHelper.punchoutSession(httpServletRequest)) {

			return true;
		}

		return false;
	}

	@Override
	public boolean isOrder() {
		return true;
	}

	@Override
	public boolean isVisible(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws Exception {

		return false;
	}

	@Override
	public void processAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {
	}

	@Override
	public void render(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws Exception {

		String punchoutReturnURL = _punchoutSessionHelper.getPunchoutReturnURL(
			httpServletRequest);

		CommerceOrder commerceOrder =
			(CommerceOrder)httpServletRequest.getAttribute(
				CommerceCheckoutWebKeys.COMMERCE_ORDER);

		if (_log.isDebugEnabled()) {
			_log.debug("Transferring cart to " + punchoutReturnURL);
		}

		String punchoutRedirectURL =
			_punchoutReturnService.returnToPunchoutVendor(
				commerceOrder, punchoutReturnURL);

		if (Validator.isBlank(punchoutRedirectURL)) {
			_jspRenderer.renderJSP(
				_servletContext, httpServletRequest, httpServletResponse,
				"/checkout_step/punchout_error.jsp");

			return;
		}

		HttpServletRequest originalHttpServletRequest =
			_portal.getOriginalServletRequest(httpServletRequest);

		HttpSession httpSession = originalHttpServletRequest.getSession();

		httpSession.setAttribute(
			PunchoutConstants.PUNCHOUT_REDIRECT_URL_ATTRIBUTE_NAME,
			punchoutRedirectURL);

		httpServletRequest.setAttribute(
			PunchoutConstants.PUNCHOUT_REDIRECT_URL_ATTRIBUTE_NAME,
			punchoutRedirectURL);

		_jspRenderer.renderJSP(
			_servletContext, httpServletRequest, httpServletResponse,
			"/checkout_step/punchout.jsp");
	}

	@Override
	public boolean showControls(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) {

		return false;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		PunchoutCommerceCheckoutStep.class);

	@Reference
	private JSPRenderer _jspRenderer;

	@Reference
	private Portal _portal;

	@Reference
	private PunchoutReturnService _punchoutReturnService;

	@Reference
	private PunchoutSessionHelper _punchoutSessionHelper;

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.commerce.punchout.web)"
	)
	private ServletContext _servletContext;

}