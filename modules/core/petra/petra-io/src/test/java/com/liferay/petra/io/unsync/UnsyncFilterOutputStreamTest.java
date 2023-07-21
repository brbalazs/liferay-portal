/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.petra.io.unsync;

import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;

import java.io.IOException;
import java.io.OutputStream;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Preston Crary
 */
public class UnsyncFilterOutputStreamTest {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		CodeCoverageAssertor.INSTANCE;

	@Test
	public void testClose() throws IOException {
		AtomicBoolean closeCalled = new AtomicBoolean();
		AtomicBoolean flushCalled = new AtomicBoolean();

		UnsyncFilterOutputStream unsyncFilterOutputStream =
			new UnsyncFilterOutputStream(
				new TestOutputStream() {

					@Override
					public void close() {
						closeCalled.set(true);
					}

					@Override
					public void flush() {
						flushCalled.set(true);
					}

				});

		unsyncFilterOutputStream.close();

		Assert.assertTrue(closeCalled.get());

		Assert.assertTrue(flushCalled.get());
	}

	@Test(expected = NullPointerException.class)
	public void testCloseNull() throws IOException {
		UnsyncFilterOutputStream unsyncFilterOutputStream =
			new UnsyncFilterOutputStream(null);

		unsyncFilterOutputStream.close();
	}

	@Test
	public void testCloseWithException() throws IOException {
		IOException ioException = new IOException();

		AtomicBoolean closeCalled = new AtomicBoolean();

		UnsyncFilterOutputStream unsyncFilterOutputStream =
			new UnsyncFilterOutputStream(
				new TestOutputStream() {

					@Override
					public void close() {
						closeCalled.set(true);
					}

					@Override
					public void flush() throws IOException {
						throw ioException;
					}

				});

		try {
			unsyncFilterOutputStream.close();

			Assert.fail();
		}
		catch (IOException ioe) {
			Assert.assertSame(ioException, ioe);
		}

		Assert.assertTrue(closeCalled.get());
	}

	@Test
	public void testCloseWithTwoExceptions() throws IOException {
		IOException flushException = new IOException();
		IOException closeException = new IOException();

		UnsyncFilterOutputStream unsyncFilterOutputStream =
			new UnsyncFilterOutputStream(
				new TestOutputStream() {

					@Override
					public void close() throws IOException {
						throw closeException;
					}

					@Override
					public void flush() throws IOException {
						throw flushException;
					}

				});

		try {
			unsyncFilterOutputStream.close();

			Assert.fail();
		}
		catch (IOException ioe) {
			Assert.assertSame(flushException, ioe);

			Throwable[] throwables = flushException.getSuppressed();

			Assert.assertEquals(
				Arrays.toString(throwables), 1, throwables.length);
			Assert.assertSame(closeException, throwables[0]);
		}
	}

	@Test
	public void testFlush() throws IOException {
		AtomicBoolean flushCalled = new AtomicBoolean();

		UnsyncFilterOutputStream unsyncFilterOutputStream =
			new UnsyncFilterOutputStream(
				new TestOutputStream() {

					@Override
					public void flush() {
						flushCalled.set(true);
					}

				});

		unsyncFilterOutputStream.flush();

		Assert.assertTrue(flushCalled.get());
	}

	@Test
	public void testWrite() throws IOException {
		int expectedB = 1;

		AtomicBoolean writeCalled = new AtomicBoolean();

		UnsyncFilterOutputStream unsyncFilterOutputStream =
			new UnsyncFilterOutputStream(
				new TestOutputStream() {

					@Override
					public void write(int b) {
						Assert.assertEquals(expectedB, b);

						writeCalled.set(true);
					}

				});

		unsyncFilterOutputStream.write(expectedB);

		Assert.assertTrue(writeCalled.get());
	}

	@Test
	public void testWriteBlock() throws IOException {
		byte[] expectedBytes = new byte[1];

		AtomicBoolean writeCalled = new AtomicBoolean();

		UnsyncFilterOutputStream unsyncFilterOutputStream =
			new UnsyncFilterOutputStream(
				new TestOutputStream() {

					@Override
					public void write(byte[] bytes, int offset, int length) {
						Assert.assertSame(expectedBytes, bytes);
						Assert.assertEquals(0, offset);
						Assert.assertEquals(1, length);

						writeCalled.set(true);
					}

				});

		unsyncFilterOutputStream.write(expectedBytes);

		Assert.assertTrue(writeCalled.get());
	}

	private abstract static class TestOutputStream extends OutputStream {

		@Override
		public void close() throws IOException {
			throw new UnsupportedOperationException();
		}

		@Override
		public void flush() throws IOException {
			throw new UnsupportedOperationException();
		}

		@Override
		public void write(byte[] bytes) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void write(byte[] bytes, int offset, int length) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void write(int b) {
			throw new UnsupportedOperationException();
		}

	}

}