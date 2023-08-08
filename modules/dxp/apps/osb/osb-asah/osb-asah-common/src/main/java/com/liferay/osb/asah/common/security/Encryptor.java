/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.security;

import java.nio.charset.StandardCharsets;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

/**
 * @author Shinn Lok
 */
@Component
public class Encryptor {

	public String decrypt(String key, String encryptedString) {
		try {
			Cipher cipher = _getCipher(key, Cipher.DECRYPT_MODE);

			return new String(
				cipher.doFinal(_decoder.decode(encryptedString)),
				StandardCharsets.UTF_8);
		}
		catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	public String encode(Key key) {
		return _encoder.encodeToString(key.getEncoded());
	}

	public String encrypt(String key, String plainText) {
		try {
			Cipher cipher = _getCipher(key, Cipher.ENCRYPT_MODE);

			return new String(
				_encoder.encode(
					cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8))),
				StandardCharsets.UTF_8);
		}
		catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	public KeyPair generateKeyPair() throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");

		keyPairGenerator.initialize(1024);

		return keyPairGenerator.generateKeyPair();
	}

	public String getSignature(
		HttpHeaders httpHeaders, String path, String privateKey) {

		Map<String, String> sortedHeaders = new TreeMap<>();

		for (Map.Entry<String, List<String>> entry : httpHeaders.entrySet()) {
			List<String> value = entry.getValue();

			sortedHeaders.put(entry.getKey(), value.get(0));
		}

		for (Map.Entry<String, String> entry : sortedHeaders.entrySet()) {
			path = path.concat(
				entry.getKey()
			).concat(
				entry.getValue()
			);
		}

		try {
			Signature signature = Signature.getInstance("DSA");

			KeyFactory keyFactory = KeyFactory.getInstance("DSA");

			signature.initSign(
				keyFactory.generatePrivate(
					new PKCS8EncodedKeySpec(_decoder.decode(privateKey))));

			signature.update(path.getBytes(StandardCharsets.UTF_8));

			return _encoder.encodeToString(signature.sign());
		}
		catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	private Cipher _getCipher(String key, int mode) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

		MessageDigest sha = MessageDigest.getInstance("SHA-1");

		cipher.init(
			mode,
			new SecretKeySpec(
				Arrays.copyOf(
					sha.digest(key.getBytes(StandardCharsets.UTF_8)), 16),
				"AES"));

		return cipher;
	}

	private static final Base64.Decoder _decoder = Base64.getDecoder();
	private static final Base64.Encoder _encoder = Base64.getEncoder();

}