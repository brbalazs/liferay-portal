package com.liferay.commerce.google.merchant.internal.sftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.liferay.commerce.google.merchant.constants.CommerceGoogleMerchantConstants;
import com.liferay.commerce.google.merchant.internal.jsch.FingerprintHostKeyRepository;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author Eric Chin
 */
@Component(
	configurationPid = "com.liferay.commerce.google.merchant.internal.sftp.SftpConfiguration",
	configurationPolicy = ConfigurationPolicy.OPTIONAL,
	immediate = true, service = SftpUploader.class
)
public class SftpUploader {

	@Activate
	public void activate(Map<String, Object> properties) {
		_sftpConfiguration = ConfigurableUtil.createConfigurable(
			SftpConfiguration.class, properties);
	}

	public void upload(String fileName, String fileContent) throws Exception {
		ChannelSftp channelSftp = null;
		Session jschSession = null;

		try {
			String username = _sftpConfiguration.username();
			String password = _sftpConfiguration.password();
			String host = _sftpConfiguration.host();
			int port = _sftpConfiguration.port();

			JSch jsch = new JSch();

			FingerprintHostKeyRepository fingerprintHostKeyRepository =
				new FingerprintHostKeyRepository(
					jsch, _sftpConfiguration.fingerprint());

			jsch.setHostKeyRepository(fingerprintHostKeyRepository);

			jschSession = jsch.getSession(username, host);

			jschSession.setPort(port);
			jschSession.setPassword(password);
			jschSession.connect();

			channelSftp = (ChannelSftp) jschSession.openChannel(
				CommerceGoogleMerchantConstants.COMMERCE_INTEGRATION_SFTP);

			channelSftp.connect();

			InputStream inputStream = new ByteArrayInputStream(
				fileContent.getBytes(StandardCharsets.UTF_8));

			channelSftp.put(inputStream, fileName);
		}
		finally {
			if (channelSftp != null) {
				channelSftp.disconnect();
				channelSftp.exit();
			}

			if (jschSession != null) {
				jschSession.disconnect();
			}
		}
	}

	private SftpConfiguration _sftpConfiguration;

}
