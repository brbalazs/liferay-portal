package com.liferay.commerce.google.merchant.internal.jsch;

import com.jcraft.jsch.HostKey;
import com.jcraft.jsch.HostKeyRepository;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.UserInfo;
import com.liferay.commerce.google.merchant.constants.GoogleMerchantConstants;

/**
 * @author Eric Chin
 */
public class FingerprintHostKeyRepository implements HostKeyRepository {

	public FingerprintHostKeyRepository(
		JSch jSch, String configuredFingerprint) {

		super();

		_configuredFingerprint = configuredFingerprint;
		_jSch = jSch;
	}

	@Override
	public int check(String s, byte[] key) {
		try {
			HostKey hostKey = new HostKey(
				GoogleMerchantConstants.GOOGLE_PARTNER_UPLOAD_URL, key);

			String fingerprint = hostKey.getFingerPrint(_jSch);

			if (!fingerprint.equals(_configuredFingerprint)) {
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

	private String _configuredFingerprint;

	private JSch _jSch;

}
