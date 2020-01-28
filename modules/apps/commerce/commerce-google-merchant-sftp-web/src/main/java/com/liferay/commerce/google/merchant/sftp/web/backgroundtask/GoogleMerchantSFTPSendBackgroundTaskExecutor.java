package com.botw.typhon.application.form.web.internal.backgroundtask;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.HASH;
import com.jcraft.jsch.HostKey;
import com.jcraft.jsch.HostKeyRepository;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.liferay.commerce.google.merchant.sftp.web.constants.GoogleMerchantSftpWebPortletKeys;
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
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
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
public class GoogleMerchantSFTPSendBackgroundTaskExecutor extends BaseBackgroundTaskExecutor {

	@Override
	public BackgroundTaskExecutor clone() {
		return this;
	}

	@Override
	public BackgroundTaskResult execute(BackgroundTask backgroundTask) throws Exception {
		Map<String, Serializable> taskContextMap = backgroundTask.getTaskContextMap();

		String username = _googleMerchantSftpUploadConfiguration.googleMerchantFeedUsername();
		String password =_googleMerchantSftpUploadConfiguration.googleMerchantFeedPassword();
		String hostKeyConfig = _googleMerchantSftpUploadConfiguration.hostKey();

		String host = GoogleMerchantSftpWebPortletKeys.GOOGLE_PARTNER_UPLOAD_URL;
		int port = 19321;

		JSch jsch = new JSch();

		HostKeyRepository hostKeyRepository = jsch.getHostKeyRepository();

		byte [] key = Base64.getDecoder().decode(hostKeyConfig);

		_validateHostKeyConfig(key, jsch.getConfig("md5"));

		HostKey hostKey = new HostKey(host, key);

		hostKeyRepository.add(hostKey, null);

		Session jschSession = jsch.getSession(username, host);

		jschSession.setPort(port);
		jschSession.setPassword(password);
		jschSession.connect();

		ChannelSftp channelSftp = (ChannelSftp) jschSession.openChannel("sftp");
		channelSftp.connect();

		String testString = "TEST";

		InputStream inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));

		channelSftp.put(inputStream, "test.xml");

		channelSftp.disconnect();
		channelSftp.exit();
		jschSession.disconnect();

		return BackgroundTaskResult.SUCCESS;
	}

	@Override
	public BackgroundTaskDisplay getBackgroundTaskDisplay(BackgroundTask backgroundTask) {
		return null;
	}

	// Referenced from https://stackoverflow.com/questions/47429132/using-servers-fingerprint-in-jsch-library-instead-of-setting-a-public-key-file
	private void _validateHostKeyConfig(byte[] key, String jschConfig)
		throws Exception {

		Class c = Class.forName(jschConfig);
		HASH hash = (HASH)(c.newInstance());
		hash.init();
		hash.update(key, 0, key.length);
		byte[] foo = hash.digest();
		StringBuffer sb = new StringBuffer();
		int bar;
		for(int i = 0; i < foo.length; i++) {
			bar = foo[i] & 0xff;
			sb.append(chars[(bar >>> 4) & 0xf]);
			sb.append(chars[(bar) & 0xf]);
			if(i + 1 < foo.length) {
				sb.append(":");
			}
		}
		String fingerprint = sb.toString();

		if (!fingerprint.equals(GoogleMerchantSftpWebPortletKeys.GOOGLE_MERCHANT_PARTNER_UPLOAD_FINGERPRINT)) {
			throw new Exception("Host key config does not match fingerprint for partnerupload.google.com");
		}
	}

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		_googleMerchantSftpUploadConfiguration = ConfigurableUtil.createConfigurable(
			GoogleMerchantSftpUploadConfiguration.class, properties);
	}

	private volatile GoogleMerchantSftpUploadConfiguration _googleMerchantSftpUploadConfiguration;

	private static String[] chars = {
		"0","1","2","3","4","5","6","7","8","9", "a","b","c","d","e","f"
	};

	private static final Log _log = LogFactoryUtil.getLog(GoogleMerchantSFTPSendBackgroundTaskExecutor.class);

}
