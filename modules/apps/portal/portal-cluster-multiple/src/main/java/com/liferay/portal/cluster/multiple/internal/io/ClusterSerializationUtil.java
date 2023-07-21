/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.cluster.multiple.internal.io;

import com.liferay.petra.io.ProtectedObjectInputStream;
import com.liferay.petra.io.SerializationConstants;
import com.liferay.petra.io.unsync.UnsyncByteArrayInputStream;
import com.liferay.petra.io.unsync.UnsyncByteArrayOutputStream;
import com.liferay.petra.lang.ClassResolverUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * @author Tina Tian
 */
public class ClusterSerializationUtil {

	public static Object readObject(byte[] bytes, int offset, int length)
		throws ClassNotFoundException {

		byte serializableType = bytes[offset];

		if (SerializationConstants.TC_OBJECT != serializableType) {
			throw new IllegalStateException(
				"Unable to deserialize this type:" + serializableType);
		}

		try {
			ObjectInputStream objectInputStream =
				new ClusterProtectedAnnotatedObjectInputStream(
					new UnsyncByteArrayInputStream(
						bytes, offset + 1, length - 1));

			return objectInputStream.readObject();
		}
		catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

	public static byte[] writeObject(Serializable serializable) {
		try {
			UnsyncByteArrayOutputStream unsyncByteArrayOutputStream =
				new UnsyncByteArrayOutputStream();

			unsyncByteArrayOutputStream.write(SerializationConstants.TC_OBJECT);

			ObjectOutputStream objectOutputStream =
				new ClusterAnnotatedObjectOutputStream(
					unsyncByteArrayOutputStream);

			objectOutputStream.writeObject(serializable);

			objectOutputStream.flush();

			return unsyncByteArrayOutputStream.unsafeGetByteArray();
		}
		catch (IOException ioe) {
			throw new RuntimeException(
				"Unable to write ordinary serializable object " + serializable,
				ioe);
		}
	}

	private static class ClusterAnnotatedObjectOutputStream
		extends ObjectOutputStream {

		@Override
		protected void annotateClass(Class<?> clazz) throws IOException {
			ClassLoader classLoader = clazz.getClassLoader();

			String contextName = ClusterClassLoaderPool.getContextName(
				classLoader);

			writeUTF(contextName);
		}

		private ClusterAnnotatedObjectOutputStream(OutputStream outputStream)
			throws IOException {

			super(outputStream);
		}

	}

	private static class ClusterProtectedAnnotatedObjectInputStream
		extends ProtectedObjectInputStream {

		@Override
		protected Class<?> doResolveClass(ObjectStreamClass objectStreamClass)
			throws ClassNotFoundException, IOException {

			String contextName = readUTF();

			ClassLoader classLoader = ClusterClassLoaderPool.getClassLoader(
				contextName);

			String className = objectStreamClass.getName();

			return ClassResolverUtil.resolve(className, classLoader);
		}

		private ClusterProtectedAnnotatedObjectInputStream(
				InputStream inputStream)
			throws IOException {

			super(inputStream);
		}

	}

}