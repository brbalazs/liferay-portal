package com.liferay.commerce.google.merchant.sftp.web.portlet;

import aQute.bnd.annotation.metatype.Meta;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Thomas Stewart
 */
@ExtendedObjectClassDefinition(category = "google-merchant")
@Meta.OCD(
	id = "com.liferay.commerce.google.merchant.sftp.web.portlet.GoogleMerchantSftpUploadConfiguration",
	name = "google-merchant-sftp-upload-configuration"
)
public interface GoogleMerchantSftpUploadConfiguration {

	@Meta.AD(
		deflt = "",
		name = "host-key",
		required = false
	)
	public String hostKey();

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
}
