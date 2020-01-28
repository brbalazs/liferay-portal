package com.liferay.commerce.google.merchant.sftp.web.portlet.action;

import com.liferay.commerce.google.merchant.sftp.web.constants.GoogleMerchantSftpWebPortletKeys;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskManager;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + GoogleMerchantSftpWebPortletKeys.SFTP_WEB,
		"mvc.command.name=" + GoogleMerchantSftpMVCActionCommand.MVC_COMMAND_NAME
	},
	service = MVCActionCommand.class
)
public class GoogleMerchantSftpMVCActionCommand extends BaseMVCActionCommand {

	public static final String MVC_COMMAND_NAME = "/putFile";

	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

		ServiceContext serviceContext = ServiceContextFactory.getInstance(actionRequest);

		Map<String, Serializable> taskContextMap = new HashMap<>();

		_backgroundTaskManager.addBackgroundTask(
			themeDisplay.getUserId(), themeDisplay.getScopeGroupId(), "Google Merchant SFTP Upload",
			GoogleMerchantSftpWebPortletKeys.SFTP_B_T_E_CLASSNAME, taskContextMap, serviceContext);

	}

	@Reference
	private BackgroundTaskManager _backgroundTaskManager;

}
