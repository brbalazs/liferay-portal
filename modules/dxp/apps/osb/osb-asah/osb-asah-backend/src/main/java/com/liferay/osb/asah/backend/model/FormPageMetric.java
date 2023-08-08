/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import com.liferay.osb.asah.backend.model.util.MetricUtil;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author Marcellus Tavares
 */
public class FormPageMetric {

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof FormPageMetric)) {
			return false;
		}

		FormPageMetric formPageMetric = (FormPageMetric)obj;

		if (Objects.equals(
				_formFieldMetrics, formPageMetric._formFieldMetrics) &&
			Objects.equals(
				_pageAbandonmentsMetric,
				formPageMetric._pageAbandonmentsMetric) &&
			Objects.equals(_pageIndex, formPageMetric._pageIndex) &&
			Objects.equals(_pageName, formPageMetric._pageName) &&
			Objects.equals(_pageViewsMetric, formPageMetric._pageViewsMetric)) {

			return true;
		}

		return false;
	}

	public Set<Metric> getAvailableMetrics() {
		return MetricUtil.getAvailableMetrics(this);
	}

	public List<FormFieldMetric> getFormFieldMetrics() {
		return _formFieldMetrics;
	}

	public Metric getPageAbandonmentsMetric() {
		return _pageAbandonmentsMetric;
	}

	public String getPageIndex() {
		return _pageIndex;
	}

	public String getPageName() {
		return _pageName;
	}

	public Metric getPageViewsMetric() {
		return _pageViewsMetric;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_formFieldMetrics, _pageAbandonmentsMetric, _pageIndex, _pageName,
			_pageViewsMetric);
	}

	public void setFormFieldMetrics(List<FormFieldMetric> formFieldMetrics) {
		_formFieldMetrics = formFieldMetrics;
	}

	public void setPageAbandonmentsMetric(Metric pageAbandonmentsMetric) {
		_pageAbandonmentsMetric = pageAbandonmentsMetric;
	}

	public void setPageIndex(String pageIndex) {
		_pageIndex = pageIndex;
	}

	public void setPageName(String pageName) {
		_pageName = pageName;
	}

	public void setPageViewsMetric(Metric pageViewsMetric) {
		_pageViewsMetric = pageViewsMetric;
	}

	private List<FormFieldMetric> _formFieldMetrics;
	private Metric _pageAbandonmentsMetric;
	private String _pageIndex;
	private String _pageName;
	private Metric _pageViewsMetric;

}