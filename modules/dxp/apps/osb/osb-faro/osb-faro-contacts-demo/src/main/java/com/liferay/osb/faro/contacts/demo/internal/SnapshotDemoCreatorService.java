/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.faro.contacts.demo.internal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Time;

import java.io.InputStream;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.osgi.service.component.annotations.Component;

/**
 * @author Matthew Kong
 */
@Component(immediate = true, service = SnapshotDemoCreatorService.class)
public class SnapshotDemoCreatorService extends DemoCreatorService {

	@Override
	public void createData() throws Exception {
		ClassLoader classLoader = getClass().getClassLoader();

		try (InputStream inputStream = classLoader.getResourceAsStream(
				"com/liferay/osb/faro/dependencies" +
					"/elasticsearch-snapshot.zip")) {

			ZipInputStream zipInputStream = new ZipInputStream(inputStream);

			ZipEntry zipEntry = zipInputStream.getNextEntry();

			long timeOffset =
				Time.DAY *
					DateUtil.getDaysBetween(
						new Date(zipEntry.getTime()),
						new Date(System.currentTimeMillis()));

			while (zipEntry != null) {
				String zipEntryName = StringUtil.removeSubstring(
					zipEntry.getName(), ".json");

				String[] zipEntryNameParts = StringUtil.split(
					zipEntryName, StringPool.UNDERLINE);

				List<Map<String, Object>> objects = _objectMapper.readValue(
					StringUtil.read(zipInputStream),
					new TypeReference<List<Map<String, Object>>>() {
					});

				_adjustTime(objects, timeOffset);

				contactsEngineClient.addData(
					faroProject, zipEntryNameParts[0],
					_getCollectionName(zipEntryNameParts), objects);

				if (log.isInfoEnabled()) {
					log.info(
						StringBundler.concat(
							"Created ", objects.size(), " objects in ",
							zipEntryName));
				}

				zipEntry = zipInputStream.getNextEntry();
			}

			zipInputStream.close();
		}

		contactsEngineClient.deleteData(
			faroProject, "osbasahfaroinfo", "run-logs");
	}

	private Object _addOffset(Object value, long timeOffset) {
		if (value instanceof Number) {
			long longValue = GetterUtil.getLong(value);

			if (longValue != 0) {
				return longValue + timeOffset;
			}

			return value;
		}

		if (value instanceof String) {
			try {
				String stringValue = (String)value;

				Date date = _dateFormat.parse(stringValue);

				return _dateFormat.format(
					new Date(date.getTime() + timeOffset));
			}
			catch (Exception e) {
			}
		}

		return value;
	}

	private void _adjustTime(Object object, long timeOffset) {
		if (timeOffset == 0) {
			return;
		}

		if (object instanceof List) {
			for (Object childObject : (List)object) {
				_adjustTime(childObject, timeOffset);
			}
		}
		else if (object instanceof Map) {
			Map<String, Object> map = (Map<String, Object>)object;

			for (Map.Entry<String, Object> entry : map.entrySet()) {
				boolean dateField = _isDateField(
					StringUtil.toLowerCase(entry.getKey()));

				if (entry.getValue() instanceof Map ||
					entry.getValue() instanceof List) {

					if (dateField && (entry.getValue() instanceof List)) {
						List<String> values = (List<String>)entry.getValue();

						Stream stream = values.stream();

						entry.setValue(
							stream.map(
								value -> _addOffset(value, timeOffset)
							).collect(
								Collectors.toList()
							));
					}
					else {
						_adjustTime(entry.getValue(), timeOffset);
					}
				}
				else if (dateField) {
					entry.setValue(_addOffset(entry.getValue(), timeOffset));
				}
			}
		}
	}

	private String _getCollectionName(String[] zipEntryNameParts) {
		String collectionName = zipEntryNameParts[1];

		if (collectionName.equals("osbasahmarkers")) {
			return "OSBAsahMarkers";
		}
		else if (collectionName.equals("osbasahtasks")) {
			return "OSBAsahTasks";
		}

		String weDeployServiceName = zipEntryNameParts[0];

		if (weDeployServiceName.equals("osbasahsalesforceraw")) {
			if (collectionName.equals("account")) {
				return "Account";
			}
			else if (collectionName.equals("contact")) {
				return "Contact";
			}
			else if (collectionName.equals("lead")) {
				return "Lead";
			}
		}

		return collectionName;
	}

	private boolean _isDateField(String key) {
		if (key.contains("date") || key.contains("day") ||
			key.contains("time")) {

			return true;
		}

		return false;
	}

	private static final DateFormat _dateFormat = new SimpleDateFormat(
		"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	private final ObjectMapper _objectMapper = new ObjectMapper();

}