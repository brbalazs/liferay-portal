package com.liferay.commerce.google.merchant.sftp.web.portlet.action;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.HASH;
import com.jcraft.jsch.HostKey;
import com.jcraft.jsch.HostKeyRepository;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.liferay.commerce.google.merchant.sftp.web.constants.GoogleMerchantSftpWebPortletKeys;
import com.liferay.commerce.google.merchant.sftp.web.portlet.GoogleMerchantSftpUploadConfiguration;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Component(
	configurationPid = "com.liferay.commerce.google.merchant.sftp.web.portlet.GoogleMerchantSftpUploadConfiguration",
	configurationPolicy = ConfigurationPolicy.OPTIONAL,
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

		String username = _googleMerchantSftpUploadConfiguration.googleMerchantFeedUsername();
		String password =_googleMerchantSftpUploadConfiguration.googleMerchantFeedPassword();
		String hostKeyConfig = _googleMerchantSftpUploadConfiguration.hostKey();

		String host = "partnerupload.google.com";
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
			bar = foo[i]&0xff;
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
}
