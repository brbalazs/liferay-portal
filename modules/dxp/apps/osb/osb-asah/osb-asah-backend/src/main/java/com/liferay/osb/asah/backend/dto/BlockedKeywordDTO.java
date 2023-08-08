/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.BlockedKeyword;
import com.liferay.osb.asah.common.util.ListUtil;
import com.liferay.osb.asah.common.util.StringUtil;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.ListUtils;

/**
 * @author André Miranda
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("blocked-keywords")
public class BlockedKeywordDTO {

	public BlockedKeywordDTO() {
	}

	public BlockedKeywordDTO(BlockedKeyword blockedKeyword) {
		this(blockedKeyword, null);
	}

	public BlockedKeywordDTO(BlockedKeyword blockedKeyword, Boolean duplicate) {
		_duplicate = duplicate;

		_createDate = blockedKeyword.getCreateDate();
		_id = StringUtil.get(blockedKeyword.getId(), null);
		_keyword = blockedKeyword.getKeyword();
	}

	public BlockedKeywordDTO(List<BlockedKeyword> blockedKeywords) {
		_blockedKeywordDTOs = ListUtil.map(
			blockedKeywords, BlockedKeywordDTO::new);
	}

	public BlockedKeywordDTO(
		List<BlockedKeyword> duplicateBlockedKeywords,
		List<BlockedKeyword> addedBlockedKeywords) {

		_blockedKeywordDTOs = ListUtils.union(
			ListUtil.map(
				duplicateBlockedKeywords,
				blockedKeyword -> new BlockedKeywordDTO(blockedKeyword, true)),
			ListUtil.map(
				addedBlockedKeywords,
				blockedKeyword -> new BlockedKeywordDTO(
					blockedKeyword, false)));
		_succeeded = !addedBlockedKeywords.isEmpty();
	}

	@JsonProperty("blocked-keywords")
	public List<BlockedKeywordDTO> getBlockedKeywordDTOs() {
		return _blockedKeywordDTOs;
	}

	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	@JsonProperty("createDate")
	public Date getCreateDate() {
		if (_createDate == null) {
			return null;
		}

		return new Date(_createDate.getTime());
	}

	@JsonProperty("duplicate")
	public Boolean getDuplicate() {
		return _duplicate;
	}

	@JsonProperty("id")
	public String getId() {
		return _id;
	}

	@JsonProperty("keyword")
	public String getKeyword() {
		return _keyword;
	}

	@JsonProperty("succeeded")
	public Boolean getSucceeded() {
		return _succeeded;
	}

	private List<BlockedKeywordDTO> _blockedKeywordDTOs;
	private Date _createDate;
	private Boolean _duplicate;
	private String _id;
	private String _keyword;
	private Boolean _succeeded;

}