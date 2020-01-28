package com.liferay.commerce.google.merchant.sftp.web.backgroundtask;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.liferay.commerce.google.merchant.sftp.web.constants.GoogleMerchantSftpWebPortletKeys;
import com.liferay.commerce.google.merchant.sftp.web.jsch.FingerprintHostKeyRepository;
import com.liferay.commerce.google.merchant.sftp.web.portlet.GoogleMerchantSftpUploadConfiguration;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.backgroundtask.BackgroundTask;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskExecutor;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskResult;
import com.liferay.portal.kernel.backgroundtask.BaseBackgroundTaskExecutor;
import com.liferay.portal.kernel.backgroundtask.display.BackgroundTaskDisplay;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author Thomas Stewart
 */
@Component(
	configurationPid = "com.liferay.commerce.google.merchant.sftp.web.portlet.GoogleMerchantSftpUploadConfiguration",
	configurationPolicy = ConfigurationPolicy.OPTIONAL,
	immediate = true,
	property = {
		"background.task.executor.class.name=" + GoogleMerchantSftpWebPortletKeys.SFTP_B_T_E_CLASSNAME
	},
	service = BackgroundTaskExecutor.class
)
public class GoogleMerchantSFTPSendBackgroundTaskExecutor
	extends BaseBackgroundTaskExecutor {

	@Override
	public BackgroundTaskExecutor clone() {
		return this;
	}

	@Override
	public BackgroundTaskResult execute(BackgroundTask backgroundTask)
		throws Exception {

		String username =
			_googleMerchantSftpUploadConfiguration.googleMerchantFeedUsername();
		String password =
			_googleMerchantSftpUploadConfiguration.googleMerchantFeedPassword();

		String host = _googleMerchantSftpUploadConfiguration.host();
		int port = _googleMerchantSftpUploadConfiguration.port();

		JSch jsch = new JSch();

		FingerprintHostKeyRepository fingerprintHostKeyRepository =
			new FingerprintHostKeyRepository(jsch);

		jsch.setHostKeyRepository(fingerprintHostKeyRepository);

		Session jschSession = jsch.getSession(username, host);

		jschSession.setPort(port);
		jschSession.setPassword(password);
		jschSession.connect();

		ChannelSftp channelSftp = (ChannelSftp) jschSession.openChannel("sftp");
		channelSftp.connect();

		String testString = "TEST";

		InputStream inputStream = new ByteArrayInputStream(
			testString.getBytes(StandardCharsets.UTF_8));

		channelSftp.put(inputStream, "test.xml");

		channelSftp.disconnect();
		channelSftp.exit();
		jschSession.disconnect();

		return BackgroundTaskResult.SUCCESS;
	}

	@Override
	public BackgroundTaskDisplay getBackgroundTaskDisplay(
		BackgroundTask backgroundTask) {

		return null;
	}

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		_googleMerchantSftpUploadConfiguration =
			ConfigurableUtil.createConfigurable(
				GoogleMerchantSftpUploadConfiguration.class, properties);
	}

	private volatile GoogleMerchantSftpUploadConfiguration
		_googleMerchantSftpUploadConfiguration;

	private static final Log _log = LogFactoryUtil.getLog(
		GoogleMerchantSFTPSendBackgroundTaskExecutor.class);

}
