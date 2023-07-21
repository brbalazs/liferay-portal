/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.document.library.uad.exporter;

import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.zip.ZipWriter;
import com.liferay.user.associated.data.exporter.UADExporter;

import org.osgi.service.component.annotations.Component;

/**
 * @author Brian Wing Shun Chan
 * @author Drew Brokke
 */
@Component(immediate = true, service = UADExporter.class)
public class DLFileEntryUADExporter extends BaseDLFileEntryUADExporter {

	@Override
	protected void writeToZip(DLFileEntry dlFileEntry, ZipWriter zipWriter)
		throws Exception {

		String dlFileEntryFileName = StringBundler.concat(
			dlFileEntry.getPrimaryKeyObj(), ".", dlFileEntry.getExtension());

		zipWriter.addEntry(dlFileEntryFileName, dlFileEntry.getContentStream());

		zipWriter.addEntry(
			dlFileEntry.getPrimaryKeyObj() + "-meta.xml", export(dlFileEntry));
	}

}