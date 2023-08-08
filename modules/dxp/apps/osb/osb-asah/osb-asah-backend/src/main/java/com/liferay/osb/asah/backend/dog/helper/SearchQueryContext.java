/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.helper;

import com.liferay.osb.asah.backend.model.AssetType;
import com.liferay.osb.asah.backend.model.Geolocation;
import com.liferay.osb.asah.backend.model.Technology;
import com.liferay.osb.asah.common.model.Interval;
import com.liferay.osb.asah.common.model.TimeRange;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Marcellus Tavares
 */
public class SearchQueryContext {

	public SearchQueryContext() {
	}

	public SearchQueryContext(AssetType assetType) {
		_assetType = assetType;
	}

	public SearchQueryContext(String assetId, AssetType assetType) {
		_assetId = assetId;
		_assetType = assetType;
	}

	public String getAssetId() {
		return _assetId;
	}

	public AssetType getAssetType() {
		return _assetType;
	}

	public String getCanonicalUrl() {
		return _canonicalUrl;
	}

	public String getChannelId() {
		return _channelId;
	}

	public Long getChannelIdAsLong() {
		if (StringUtils.isNotBlank(_channelId)) {
			return Long.valueOf(_channelId);
		}

		return null;
	}

	public String getDataSourceId() {
		return _dataSourceId;
	}

	public String getEntityId() {
		return _entityId;
	}

	public String getEntityType() {
		return _entityType;
	}

	public String getExperienceId() {
		return _experienceId;
	}

	public Long getExperimentId() {
		return _experimentId;
	}

	public Geolocation getGeolocation() {
		return _geolocation;
	}

	public Interval getInterval() {
		return _interval;
	}

	public String getKeywords() {
		return _keywords;
	}

	public Technology getTechnology() {
		return _technology;
	}

	public String getTerms() {
		return _terms;
	}

	public TimeRange getTimeRange() {
		return _timeRange;
	}

	public String getTitle() {
		return _title;
	}

	public String getURL() {
		return _url;
	}

	public String getVariantId() {
		return _variantId;
	}

	public Boolean isActive() {
		return _active;
	}

	public Boolean isIncludeActiveSessions() {
		return _includeActiveSessions;
	}

	public Boolean isIncludePrevious() {
		return _includePrevious;
	}

	public void setActive(Boolean active) {
		_active = active;
	}

	public void setAssetId(String assetId) {
		_assetId = assetId;
	}

	public void setAssetType(AssetType assetType) {
		_assetType = assetType;
	}

	public void setCanonicalUrl(String canonicalUrl) {
		_canonicalUrl = canonicalUrl;
	}

	public void setChannelId(Long channelId) {
		if (channelId != null) {
			_channelId = channelId.toString();
		}
	}

	public void setChannelId(String channelId) {
		_channelId = channelId;
	}

	public void setCountry(String country) {
		_geolocation = Geolocation.country(country);
	}

	public void setDataSourceId(String dataSourceId) {
		_dataSourceId = dataSourceId;
	}

	public void setDeviceType(String deviceType) {
		_technology = Technology.deviceType(deviceType);
	}

	public void setEntityId(String entityId) {
		_entityId = entityId;
	}

	public void setEntityType(String entityType) {
		_entityType = entityType;
	}

	public void setExperienceId(String experienceId) {
		_experienceId = experienceId;
	}

	public void setExperimentId(Long experimentId) {
		_experimentId = experimentId;
	}

	public void setIncludeActiveSessions(Boolean includeActiveSessions) {
		_includeActiveSessions = includeActiveSessions;
	}

	public void setIncludePrevious(Boolean includePrevious) {
		_includePrevious = includePrevious;
	}

	public void setInterval(String interval) {
		if (interval != null) {
			_interval = Interval.of(interval);
		}
	}

	public void setKeywords(String keywords) {
		_keywords = keywords;
	}

	public void setRangeKey(int rangeKey) {
		_timeRange = TimeRange.of(rangeKey);
	}

	public void setTerms(String terms) {
		_terms = terms;
	}

	public void setTimeRange(TimeRange timeRange) {
		_timeRange = timeRange;
	}

	public void setTitle(String title) {
		_title = title;
	}

	public void setURL(String url) {
		_url = url;
	}

	public void setVariantId(String variantId) {
		_variantId = variantId;
	}

	private Boolean _active;
	private String _assetId;
	private AssetType _assetType = AssetType.PAGE;
	private String _canonicalUrl;
	private String _channelId;
	private String _dataSourceId;
	private String _entityId;
	private String _entityType;
	private String _experienceId;
	private Long _experimentId;
	private Geolocation _geolocation = Geolocation.any();
	private Boolean _includeActiveSessions = false;
	private Boolean _includePrevious = true;
	private Interval _interval = Interval.DAY;
	private String _keywords;
	private Technology _technology = Technology.any();
	private String _terms;
	private TimeRange _timeRange = TimeRange.LAST_30_DAYS;
	private String _title;
	private String _url;
	private String _variantId;

}