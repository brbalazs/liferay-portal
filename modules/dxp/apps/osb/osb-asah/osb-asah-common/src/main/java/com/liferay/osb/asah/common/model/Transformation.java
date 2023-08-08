/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Map;
import java.util.Objects;

/**
 * @author Rachael Koestartyo
 */
public class Transformation {

	public Transformation() {
	}

	public Transformation(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	public Transformation(Map<String, Object> terms, Integer totalElements) {
		_term = new Term(terms);

		_totalElements = totalElements;
	}

	public Transformation(Term term, Integer totalElements) {
		_term = term;
		_totalElements = totalElements;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Transformation)) {
			return false;
		}

		Transformation transformation = (Transformation)obj;

		if (Objects.equals(_term, transformation._term) &&
			Objects.equals(_totalElements, transformation._totalElements)) {

			return true;
		}

		return false;
	}

	public Term getTerm() {
		return _term;
	}

	public Integer getTotalElements() {
		return _totalElements;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_term, _totalElements);
	}

	public void setTerm(Term term) {
		_term = term;
	}

	public void setTotalElements(Integer totalElements) {
		_totalElements = totalElements;
	}

	public static class Term {

		public Term() {
		}

		public Term(Map<String, Object> termsMap) {
			_termsMap = termsMap;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (!(obj instanceof Term)) {
				return false;
			}

			Term term = (Term)obj;

			if (Objects.equals(_termsMap, term._termsMap)) {
				return true;
			}

			return false;
		}

		public Map<String, Object> getTermsMap() {
			return _termsMap;
		}

		@Override
		public int hashCode() {
			return Objects.hash(_termsMap);
		}

		public void setTermsMap(Map<String, Object> termsMap) {
			_termsMap = termsMap;
		}

		private Map<String, Object> _termsMap;

	}

	private Term _term;
	private Integer _totalElements;

}