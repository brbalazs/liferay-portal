package com.liferay.commerce.google.merchant.sftp.web.jsch;

import com.jcraft.jsch.HostKey;
import com.jcraft.jsch.HostKeyRepository;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.UserInfo;
import com.liferay.commerce.google.merchant.sftp.web.constants.GoogleMerchantSftpWebPortletKeys;

/**
 * @author Eric Chin
 */
public class FingerprintHostKeyRepository implements HostKeyRepository {

	public FingerprintHostKeyRepository(JSch jSch) {
		super();

		_jSch = jSch;
	}

	@Override
	public int check(String s, byte[] key) {
		try {
			HostKey hostKey = new HostKey(
				GoogleMerchantSftpWebPortletKeys.GOOGLE_PARTNER_UPLOAD_URL, key);

			String fingerprint = hostKey.getFingerPrint(_jSch);

			String destinationFingerprint =
				GoogleMerchantSftpWebPortletKeys.GOOGLE_MERCHANT_PARTNER_UPLOAD_FINGERPRINT;

			if (!fingerprint.equals(destinationFingerprint)) {
				return NOT_INCLUDED;
			}
		}
		catch (Exception e) {
			return NOT_INCLUDED;
		}

		return OK;
	}

	@Override
	public void add(HostKey hostKey, UserInfo userInfo) {
	}

	@Override
	public void remove(String s, String s1) {
	}

	@Override
	public void remove(String s, String s1, byte[] bytes) {
	}

	@Override
	public String getKnownHostsRepositoryID() {
		return null;
	}

	@Override
	public HostKey[] getHostKey() {
		return new HostKey[0];
	}

	@Override
	public HostKey[] getHostKey(String s, String s1) {
		return new HostKey[0];
	}

	private JSch _jSch;

}
