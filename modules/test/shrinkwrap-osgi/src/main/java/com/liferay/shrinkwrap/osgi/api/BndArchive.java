/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.shrinkwrap.osgi.api;

import aQute.bnd.osgi.Jar;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.jboss.shrinkwrap.api.Assignable;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;

/**
 * @author Carlos Sierra Andrés
 */
public class BndArchive implements Assignable {

	public BndArchive(Jar jar) {
		_jar = jar;
	}

	@Override
	public <TYPE extends Assignable> TYPE as(Class<TYPE> typeClass) {
		try {
			ByteArrayOutputStream byteArrayOutputStream =
				new ByteArrayOutputStream();

			_jar.write(byteArrayOutputStream);

			ZipImporter zipImporter = ShrinkWrap.create(ZipImporter.class);

			zipImporter.importFrom(
				new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));

			return zipImporter.as(typeClass);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Jar getJar() {
		return _jar;
	}

	private final Jar _jar;

}