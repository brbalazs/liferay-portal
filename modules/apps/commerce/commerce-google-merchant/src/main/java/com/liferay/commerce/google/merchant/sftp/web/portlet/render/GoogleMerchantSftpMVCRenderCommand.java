package com.liferay.commerce.google.merchant.sftp.web.portlet.render;

import com.liferay.commerce.google.merchant.sftp.web.constants.GoogleMerchantSftpWebPortletKeys;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import org.osgi.service.component.annotations.Component;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + GoogleMerchantSftpWebPortletKeys.SFTP_WEB,
		"mvc.command.name=/", "mvc.command.name=/view"
	},
	service = MVCRenderCommand.class
)
public class GoogleMerchantSftpMVCRenderCommand implements MVCRenderCommand {

	@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
		return "/view.jsp";
	}

}
