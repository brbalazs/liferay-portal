/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.cluster.multiple.internal.io;

import com.liferay.petra.io.Deserializer;
import com.liferay.petra.io.Serializer;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;

import java.nio.ByteBuffer;

import java.util.Date;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Tina Tian
 */
public class ClusterSerializationUtilTest {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		CodeCoverageAssertor.INSTANCE;

	@Test
	public void testConstructor() {
		new ClusterSerializationUtil();
	}

	@Test
	public void testReadObject() throws Exception {

		// Test 1, noraml

		Serializer serializer = new Serializer();

		Date date = new Date(123456);

		serializer.writeObject(date);

		ByteBuffer byteBuffer = serializer.toByteBuffer();

		Object object = ClusterSerializationUtil.readObject(
			byteBuffer.array(), byteBuffer.position(), byteBuffer.remaining());

		Assert.assertEquals(object, date);

		// Test 2, wrong byte array

		serializer = new Serializer();

		serializer.writeObject(date);

		byteBuffer = serializer.toByteBuffer();

		byteBuffer.put(2, (byte)0xFF);

		try {
			object = ClusterSerializationUtil.readObject(
				byteBuffer.array(), byteBuffer.position(),
				byteBuffer.remaining());

			Assert.fail("Should fail to read object " + object);
		}
		catch (RuntimeException re) {
			Assert.assertTrue(
				String.valueOf(re.getCause()),
				re.getCause() instanceof StreamCorruptedException);
		}

		// Test 3, wrong type

		serializer = new Serializer();

		serializer.writeObject(getClass());

		byteBuffer = serializer.toByteBuffer();

		try {
			object = ClusterSerializationUtil.readObject(
				byteBuffer.array(), byteBuffer.position(),
				byteBuffer.remaining());

			Assert.fail("Should fail to read object " + object);
		}
		catch (IllegalStateException ise) {
			Assert.assertEquals(
				"Unable to deserialize this type:3", ise.getMessage());
		}
	}

	@Test
	public void testWriteObject() throws Exception {

		// Test 1, normal

		Date date = new Date(123456);

		byte[] bytes = ClusterSerializationUtil.writeObject(date);

		Deserializer deserializer = new Deserializer(ByteBuffer.wrap(bytes));

		Assert.assertEquals(date, deserializer.readObject());

		// Test 2, failed

		IOException ioe = new IOException("Forced IOException");

		Serializable serializable = new Serializable() {

			private void writeObject(ObjectOutputStream objectOutputStream)
				throws IOException {

				throw ioe;
			}

		};

		try {
			ClusterSerializationUtil.writeObject(serializable);

			Assert.fail();
		}
		catch (RuntimeException re) {
			String message = re.getMessage();

			Assert.assertTrue(
				message,
				message.startsWith(
					"Unable to write ordinary serializable object "));

			Assert.assertSame(ioe, re.getCause());
		}
	}

}