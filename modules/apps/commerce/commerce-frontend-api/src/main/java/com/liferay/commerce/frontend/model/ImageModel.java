/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.commerce.frontend.model;

/**
 * @author Marco Leo
 */
public class ImageModel {

	public String getAlt() {
		return _alt;
	}

	public String getShape() {
		return _shape;
	}

	public String getSize() {
		return _size;
	}

	public String getUrl() {
		return _url;
	}

	public void setAlt(String alt) {
		_alt = alt;
	}

	public void setShape(String shape) {
		_shape = shape;
	}

	public void setSize(String size) {
		_size = size;
	}

	public void setUrl(String url) {
		_url = url;
	}

	private String _alt;
	private String _shape;
	private String _size;
	private String _url;

}