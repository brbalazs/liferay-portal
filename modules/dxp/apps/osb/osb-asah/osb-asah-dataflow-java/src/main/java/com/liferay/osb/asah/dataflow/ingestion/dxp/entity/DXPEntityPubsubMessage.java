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

package com.liferay.osb.asah.dataflow.ingestion.dxp.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import com.liferay.osb.asah.dataflow.common.ObjectMapperUtil;

import java.io.IOException;
import java.io.Serializable;

import java.nio.charset.StandardCharsets;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;

/**
 * @author Riccardo Ferrari
 */
public class DXPEntityPubsubMessage implements Serializable {

	public DXPEntityPubsubMessage() {
	}

	public DXPEntityPubsubMessage(
		Map<String, String> attributes, String payload) {

		_attributes = new Attributes(attributes);
		_payload = payload;
	}

	public DXPEntityPubsubMessage(PubsubMessage pubsubMessage) {
		_attributes = new Attributes(pubsubMessage.getAttributeMap());
		_payload = new String(
			pubsubMessage.getPayload(), StandardCharsets.UTF_8);
	}

	public Attributes getAttributes() {
		return _attributes;
	}

	public String getPayload() {
		return _payload;
	}

	public void setAttributes(Attributes attributes) {
		_attributes = attributes;
	}

	public void setPayload(String payload) {
		_payload = payload;
	}

	public static class Attributes extends HashMap<String, String> {

		public Attributes() {
		}

		public Attributes(Map<String, String> attributes) {
			if (attributes != null) {
				putAll(attributes);
			}
		}

		public Map<Long, Long> getCommerceChannelIdChannelIds()
			throws Exception {

			String commerceChannelIdChannelIdsString = getOrDefault(
				"commerceChannelIdChannelIds", null);

			if (commerceChannelIdChannelIdsString == null) {
				return Collections.emptyMap();
			}

			return ObjectMapperUtil.readValue(
				CommerceChannelIdChannelIdMap.class,
				commerceChannelIdChannelIdsString);
		}

		public long getCount() {
			return Long.parseLong(getOrDefault("count", "0"));
		}

		public String getDataSourceId() {
			return get("dataSourceId");
		}

		public String getProjectId() {
			return get("projectId");
		}

		public String getResourceName() {
			return get("resourceName");
		}

		public Set<String> getSuppressedEmailAddresses() throws Exception {
			String suppressedEmailAddressesString = getOrDefault(
				"suppressedEmailAddresses", null);

			if (suppressedEmailAddressesString == null) {
				return Collections.emptySet();
			}

			return ObjectMapperUtil.readValue(
				Set.class, suppressedEmailAddressesString);
		}

		public String getUploadTime() {
			return get("uploadTime");
		}

		public String getUploadType() {
			return get("uploadType");
		}

		public boolean isLast() {
			return Boolean.parseBoolean(getOrDefault("last", "false"));
		}

	}

	@JsonDeserialize(using = CommerceChannelIdChannelIdMapDeserializer.class)
	public static class CommerceChannelIdChannelIdMap
		extends HashMap<Long, Long> {
	}

	public static class CommerceChannelIdChannelIdMapDeserializer
		extends StdDeserializer<CommerceChannelIdChannelIdMap> {

		public CommerceChannelIdChannelIdMapDeserializer() {
			this(CommerceChannelIdChannelIdMap.class);
		}

		@Override
		public CommerceChannelIdChannelIdMap deserialize(
				JsonParser jsonParser,
				DeserializationContext deserializationContext)
			throws IOException {

			CommerceChannelIdChannelIdMap commerceChannelIdChannelIdMap =
				new CommerceChannelIdChannelIdMap();

			JsonNode jsonNode = deserializationContext.readTree(jsonParser);

			Iterator<String> iterator = jsonNode.fieldNames();

			while (iterator.hasNext()) {
				String commerceChannelIdAsString = iterator.next();

				JsonNode valueJsonNode = jsonNode.get(
					commerceChannelIdAsString);

				commerceChannelIdChannelIdMap.put(
					Long.valueOf(commerceChannelIdAsString),
					valueJsonNode.asLong());
			}

			return commerceChannelIdChannelIdMap;
		}

		protected CommerceChannelIdChannelIdMapDeserializer(Class<?> clazz) {
			super(clazz);
		}

	}

	private Attributes _attributes;
	private String _payload;

}