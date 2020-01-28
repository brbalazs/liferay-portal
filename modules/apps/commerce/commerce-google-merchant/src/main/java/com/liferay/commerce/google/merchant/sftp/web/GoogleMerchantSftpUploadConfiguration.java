package com.liferay.commerce.google.merchant.sftp.web;

import aQute.bnd.annotation.metatype.Meta;
import com.liferay.commerce.google.merchant.sftp.web.constants.GoogleMerchantSftpWebPortletKeys;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Thomas Stewart
 */
@ExtendedObjectClassDefinition(category = "google-merchant")
@Meta.OCD(
	id = "com.liferay.commerce.google.merchant.sftp.web.GoogleMerchantSftpUploadConfiguration",
	name = "google-merchant-sftp-upload-configuration"
)
public interface GoogleMerchantSftpUploadConfiguration {

	@Meta.AD(
		deflt = GoogleMerchantSftpWebPortletKeys.GOOGLE_PARTNER_UPLOAD_URL,
		name = "host",
		required = false
	)
	public String host();

	@Meta.AD(
		deflt = "",
		name = "google-merchant-feed-username",
		required = false
	)
	public String googleMerchantFeedUsername();

	@Meta.AD(
		deflt = "",
		name = "google-merchant-feed-password",
		required = false
	)
	public String googleMerchantFeedPassword();

	@Meta.AD(
		deflt = "19321",
		name = "port",
		required = false
	)
	public int port();
}
