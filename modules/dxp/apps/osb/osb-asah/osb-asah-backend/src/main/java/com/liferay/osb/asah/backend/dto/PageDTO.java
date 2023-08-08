/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Inácio Nery
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageDTO<T> {

	public PageDTO() {
	}

	public PageDTO(List<T> results, Long total) {
		_results = results;
		_total = total;
	}

	public PageDTO(
		String contentKey, T contentValue, Integer number, Integer size,
		Long totalElements, Integer totalPages) {

		_content = Collections.singletonMap(contentKey, contentValue);

		_page = new Page(number, size, totalElements, totalPages);
	}

	@JsonAnyGetter
	public Map<String, T> getContent() {
		return _content;
	}

	@JsonProperty("page")
	public Page getPage() {
		return _page;
	}

	@JsonProperty("results")
	public List<T> getResults() {
		return _results;
	}

	@JsonProperty("total")
	public long getTotal() {
		return _total;
	}

	private Map<String, T> _content;
	private Page _page;
	private List<T> _results;
	private long _total;

	private static class Page {

		public Page() {
		}

		public Page(
			Integer number, Integer size, Long totalElements,
			Integer totalPages) {

			_number = number;
			_size = size;
			_totalElements = totalElements;
			_totalPages = totalPages;
		}

		@JsonProperty("number")
		public Integer getNumber() {
			return _number;
		}

		@JsonProperty("size")
		public Integer getSize() {
			return _size;
		}

		@JsonProperty("totalElements")
		public Long getTotalElements() {
			return _totalElements;
		}

		@JsonProperty("totalPages")
		public Integer getTotalPages() {
			return _totalPages;
		}

		private Integer _number;
		private Integer _size;
		private Long _totalElements;
		private Integer _totalPages;

	}

}