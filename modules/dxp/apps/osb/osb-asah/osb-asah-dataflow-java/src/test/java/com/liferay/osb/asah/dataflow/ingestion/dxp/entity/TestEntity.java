/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp.entity;

import java.io.Serializable;

import java.util.List;
import java.util.Map;

/**
 * @author Riccardo Ferrari
 */
public class TestEntity implements Serializable {

	public boolean bool;
	public Integer integer;
	public Map<String, String> map;
	public List<Map<String, String>> mapList;
	public long number;
	public Object[] objectArray;
	public String string;
	public List<String> stringList;
	public TestEntity testEntity;
	public List<TestEntity> testEntityList;

}