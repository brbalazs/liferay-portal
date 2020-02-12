package com.liferay.commerce.google.merchant.internal.sftp;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.commerce.google.merchant.internal.constants.CommerceGoogleMerchantConstants;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Thomas Stewart
 */
@ExtendedObjectClassDefinition(category = "google-merchant")
@Meta.OCD(
	id = "com.liferay.commerce.google.merchant.internal.sftp.SftpConfiguration",
	name = "sftp-upload-configuration"
)
public interface SftpConfiguration {

	@Meta.AD(
		deflt = CommerceGoogleMerchantConstants.COMMERCE_GOOGLE_PARTNER_UPLOAD_URL,
		name = "host",
		required = false
	)
	public String host();

	@Meta.AD(name = "feed-username", required = false)
	public String username();

	@Meta.AD(name = "feed-password", required = false)
	public String password();

	@Meta.AD(
		deflt = "19321",
		name = "port",
		required = false
	)
	public int port();

	@Meta.AD(name = "fingerprint", required = false)
	public String fingerprint();

}
