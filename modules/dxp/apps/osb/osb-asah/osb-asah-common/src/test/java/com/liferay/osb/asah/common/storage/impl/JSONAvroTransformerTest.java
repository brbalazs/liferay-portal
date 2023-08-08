/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.storage.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;

import com.liferay.osb.asah.common.json.JSONUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecordBuilder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author Marcellus Tavares
 */
public class JSONAvroTransformerTest {

	@BeforeEach
	public void setUp() {
		_jsonAvroTransformer = new JSONAvroTransformer();

		ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.registerModule(new JsonOrgModule());

		ReflectionTestUtils.setField(
			_jsonAvroTransformer, "_objectMapper", objectMapper);
	}

	@Test
	public void testRecordWithArrayDefault() {
		SchemaBuilder.RecordBuilder<Schema> recordBuilder =
			SchemaBuilder.record("user");

		SchemaBuilder.FieldAssembler<Schema> fieldAssembler =
			recordBuilder.fields();

		SchemaBuilder.FieldBuilder<Schema> fieldBuilder = fieldAssembler.name(
			"fruits");

		SchemaBuilder.FieldTypeBuilder<Schema> fieldTypeBuilder =
			fieldBuilder.type();

		SchemaBuilder.ArrayBuilder<SchemaBuilder.ArrayDefault<Schema>>
			arrayBuilder = fieldTypeBuilder.array();

		SchemaBuilder.TypeBuilder<SchemaBuilder.ArrayDefault<Schema>>
			typeBuilder = arrayBuilder.items();

		SchemaBuilder.ArrayDefault<Schema> arrayDefault =
			typeBuilder.stringType();

		arrayDefault.arrayDefault(Arrays.asList("strawberry"));

		Schema schema = fieldAssembler.endRecord();

		GenericData.Record expectedRecord = _buildRecord(
			new HashMap<String, Object>() {
				{
					put("fruits", Arrays.asList("strawberry"));
				}
			},
			schema);

		GenericData.Record actualRecord = _jsonAvroTransformer.transform(
			JSONUtil.put("fruits", null), schema);

		Assertions.assertEquals(expectedRecord, actualRecord);
	}

	@Test
	public void testRecordWithArrayRequired() {
		SchemaBuilder.RecordBuilder<Schema> recordBuilder =
			SchemaBuilder.record("user");

		SchemaBuilder.FieldAssembler<Schema> fieldAssembler =
			recordBuilder.fields();

		SchemaBuilder.FieldBuilder<Schema> fieldBuilder = fieldAssembler.name(
			"fruits");

		SchemaBuilder.FieldTypeBuilder<Schema> fieldTypeBuilder =
			fieldBuilder.type();

		SchemaBuilder.ArrayBuilder<SchemaBuilder.ArrayDefault<Schema>>
			arrayBuilder = fieldTypeBuilder.array();

		SchemaBuilder.TypeBuilder<SchemaBuilder.ArrayDefault<Schema>>
			typeBuilder = arrayBuilder.items();

		SchemaBuilder.ArrayDefault<Schema> arrayDefault =
			typeBuilder.stringType();

		arrayDefault.noDefault();

		Schema schema = fieldAssembler.endRecord();

		GenericData.Record expectedRecord = _buildRecord(
			new HashMap<String, Object>() {
				{
					put("fruits", Arrays.asList("apple", "banana"));
				}
			},
			schema);

		GenericData.Record actualRecord = _jsonAvroTransformer.transform(
			JSONUtil.put("fruits", JSONUtil.putAll("apple", "banana")), schema);

		Assertions.assertEquals(expectedRecord, actualRecord);
	}

	@Test
	public void testRecordWithBooleanDefault() {
		SchemaBuilder.RecordBuilder<Schema> recordBuilder =
			SchemaBuilder.record("user");

		SchemaBuilder.FieldAssembler<Schema> fieldAssembler =
			recordBuilder.fields();

		SchemaBuilder.FieldBuilder<Schema> fieldBuilder = fieldAssembler.name(
			"compliant");

		SchemaBuilder.FieldTypeBuilder<Schema> fieldTypeBuilder =
			fieldBuilder.type();

		SchemaBuilder.BooleanDefault<Schema> booleanDefault =
			fieldTypeBuilder.booleanType();

		booleanDefault.booleanDefault(true);

		Schema schema = fieldAssembler.endRecord();

		GenericRecordBuilder genericRecordBuilder = new GenericRecordBuilder(
			schema);

		genericRecordBuilder.set("compliant", false);

		GenericData.Record expectedRecord = _buildRecord(
			new HashMap<String, Object>() {
				{
					put("compliant", false);
				}
			},
			schema);

		GenericData.Record actualRecord = _jsonAvroTransformer.transform(
			JSONUtil.put("compliant", false), schema);

		Assertions.assertEquals(expectedRecord, actualRecord);
	}

	@Test
	public void testRecordWithEnumRequired() {
		SchemaBuilder.RecordBuilder<Schema> recordBuilder =
			SchemaBuilder.record("user");

		SchemaBuilder.FieldAssembler<Schema> fieldAssembler =
			recordBuilder.fields();

		SchemaBuilder.FieldBuilder<Schema> fieldBuilder = fieldAssembler.name(
			"gender");

		SchemaBuilder.FieldTypeBuilder<Schema> fieldTypeBuilder =
			fieldBuilder.type();

		SchemaBuilder.EnumBuilder<SchemaBuilder.EnumDefault<Schema>>
			enumBuilder = fieldTypeBuilder.enumeration("gender");

		SchemaBuilder.EnumDefault<Schema> enumDefault = enumBuilder.symbols(
			"f", "m");

		enumDefault.noDefault();

		Schema schema = fieldAssembler.endRecord();

		GenericData.Record expectedRecord = _buildRecord(
			new HashMap<String, Object>() {
				{
					put("gender", "m");
				}
			},
			schema);

		GenericData.Record actualRecord = _jsonAvroTransformer.transform(
			JSONUtil.put("gender", "m"), schema);

		Assertions.assertEquals(expectedRecord, actualRecord);
	}

	@Test
	public void testRecordWithIntDefault() {
		SchemaBuilder.RecordBuilder<Schema> recordBuilder =
			SchemaBuilder.record("user");

		SchemaBuilder.FieldAssembler<Schema> fieldAssembler =
			recordBuilder.fields();

		fieldAssembler.nullableInt("age", 42);

		Schema schema = fieldAssembler.endRecord();

		GenericData.Record expectedRecord = _buildRecord(
			new HashMap<String, Object>() {
				{
					put("age", 42);
				}
			},
			schema);

		GenericData.Record actualRecord = _jsonAvroTransformer.transform(
			JSONUtil.put("age", null), schema);

		Assertions.assertEquals(expectedRecord, actualRecord);
	}

	@Test
	public void testRecordWithIntOptional() {
		SchemaBuilder.RecordBuilder<Schema> recordBuilder =
			SchemaBuilder.record("user");

		SchemaBuilder.FieldAssembler<Schema> fieldAssembler =
			recordBuilder.fields();

		fieldAssembler.optionalInt("age");

		Schema schema = fieldAssembler.endRecord();

		GenericData.Record expectedRecord = _buildRecord(
			Collections.emptyMap(), schema);

		GenericData.Record actualRecord = _jsonAvroTransformer.transform(
			JSONUtil.put("age", null), schema);

		Assertions.assertEquals(expectedRecord, actualRecord);
	}

	@Test
	public void testRecordWithIntRequired() {
		SchemaBuilder.RecordBuilder<Schema> recordBuilder =
			SchemaBuilder.record("user");

		SchemaBuilder.FieldAssembler<Schema> fieldAssembler =
			recordBuilder.fields();

		fieldAssembler.requiredInt("age");

		Schema schema = fieldAssembler.endRecord();

		GenericData.Record expectedRecord = _buildRecord(
			new HashMap<String, Object>() {
				{
					put("age", 18);
				}
			},
			schema);

		GenericData.Record actualRecord = _jsonAvroTransformer.transform(
			JSONUtil.put("age", 18), schema);

		Assertions.assertEquals(expectedRecord, actualRecord);
	}

	@Test
	public void testRecordWithMapRequired() {
		SchemaBuilder.RecordBuilder<Schema> recordBuilder =
			SchemaBuilder.record("event");

		SchemaBuilder.FieldAssembler<Schema> fieldAssembler =
			recordBuilder.fields();

		SchemaBuilder.FieldBuilder<Schema> fieldBuilder = fieldAssembler.name(
			"context");

		SchemaBuilder.FieldTypeBuilder<Schema> fieldTypeBuilder =
			fieldBuilder.type();

		SchemaBuilder.MapBuilder<SchemaBuilder.MapDefault<Schema>> mapBuilder =
			fieldTypeBuilder.map();

		SchemaBuilder.TypeBuilder<SchemaBuilder.MapDefault<Schema>>
			typeBuilder = mapBuilder.values();

		SchemaBuilder.MapDefault<Schema> mapDefault = typeBuilder.stringType();

		mapDefault.noDefault();

		Schema schema = fieldAssembler.endRecord();

		GenericData.Record expectedRecord = _buildRecord(
			new HashMap<String, Object>() {
				{
					put(
						"context",
						new HashMap<String, Object>() {
							{
								put("title", "Liferay");
								put("url", "https://www.liferay.com/");
							}
						});
				}
			},
			schema);

		GenericData.Record actualRecord = _jsonAvroTransformer.transform(
			JSONUtil.put(
				"context",
				JSONUtil.put(
					"title", "Liferay"
				).put(
					"url", "https://www.liferay.com/"
				)),
			schema);

		Assertions.assertEquals(expectedRecord, actualRecord);
	}

	@Test
	public void testRecordWithNullDefault() {
		SchemaBuilder.RecordBuilder<Schema> recordBuilder =
			SchemaBuilder.record("event");

		SchemaBuilder.FieldAssembler<Schema> fieldAssembler =
			recordBuilder.fields();

		SchemaBuilder.FieldBuilder<Schema> fieldBuilder = fieldAssembler.name(
			"department");

		SchemaBuilder.FieldTypeBuilder<Schema> fieldTypeBuilder =
			fieldBuilder.type();

		SchemaBuilder.NullDefault<Schema> nullDefault =
			fieldTypeBuilder.nullType();

		nullDefault.nullDefault();

		Schema schema = fieldAssembler.endRecord();

		GenericData.Record expectedRecord = _buildRecord(
			Collections.emptyMap(), schema);

		GenericData.Record actualRecord = _jsonAvroTransformer.transform(
			JSONUtil.put("department", null), schema);

		Assertions.assertEquals(expectedRecord, actualRecord);
	}

	@Test
	public void testRecordWithStringDefault() {
		SchemaBuilder.RecordBuilder<Schema> recordBuilder =
			SchemaBuilder.record("user");

		SchemaBuilder.FieldAssembler<Schema> fieldAssembler =
			recordBuilder.fields();

		fieldAssembler.nullableString("company", "Liferay");

		Schema schema = fieldAssembler.endRecord();

		GenericData.Record expectedRecord = _buildRecord(
			new HashMap<String, Object>() {
				{
					put("company", "Liferay");
				}
			},
			schema);

		GenericData.Record actualRecord = _jsonAvroTransformer.transform(
			JSONUtil.put("company", null), schema);

		Assertions.assertEquals(expectedRecord, actualRecord);
	}

	@Test
	public void testRecordWithStringOptionalSet() {
		SchemaBuilder.RecordBuilder<Schema> recordBuilder =
			SchemaBuilder.record("user");

		SchemaBuilder.FieldAssembler<Schema> fieldAssembler =
			recordBuilder.fields();

		fieldAssembler.optionalString("title");

		Schema schema = fieldAssembler.endRecord();

		GenericData.Record expectedRecord = _buildRecord(
			new HashMap<String, Object>() {
				{
					put("title", "Hello World");
				}
			},
			schema);

		GenericData.Record actualRecord = _jsonAvroTransformer.transform(
			JSONUtil.put("title", "Hello World"), schema);

		Assertions.assertEquals(expectedRecord, actualRecord);
	}

	@Test
	public void testRecordWithStringOptionalUnset() {
		SchemaBuilder.RecordBuilder<Schema> recordBuilder =
			SchemaBuilder.record("user");

		SchemaBuilder.FieldAssembler<Schema> fieldAssembler =
			recordBuilder.fields();

		fieldAssembler.optionalString("title");

		Schema schema = fieldAssembler.endRecord();

		GenericData.Record expectedRecord = _buildRecord(
			Collections.emptyMap(), schema);

		GenericData.Record actualRecord = _jsonAvroTransformer.transform(
			JSONUtil.put("title", null), schema);

		Assertions.assertEquals(expectedRecord, actualRecord);
	}

	@Test
	public void testRecordWithStringRequired() {
		SchemaBuilder.RecordBuilder<Schema> recordBuilder =
			SchemaBuilder.record("user");

		SchemaBuilder.FieldAssembler<Schema> fieldAssembler =
			recordBuilder.fields();

		fieldAssembler.requiredString("name");

		Schema schema = fieldAssembler.endRecord();

		GenericData.Record expectedRecord = _buildRecord(
			new HashMap<String, Object>() {
				{
					put("name", "Joe");
				}
			},
			schema);

		GenericData.Record actualRecord = _jsonAvroTransformer.transform(
			JSONUtil.put("name", "Joe"), schema);

		Assertions.assertEquals(expectedRecord, actualRecord);
	}

	private GenericData.Record _buildRecord(
		Map<String, Object> fieldsMap, Schema schema) {

		GenericRecordBuilder genericRecordBuilder = new GenericRecordBuilder(
			schema);

		for (Map.Entry<String, Object> entry : fieldsMap.entrySet()) {
			genericRecordBuilder.set(entry.getKey(), entry.getValue());
		}

		return genericRecordBuilder.build();
	}

	private JSONAvroTransformer _jsonAvroTransformer;

}