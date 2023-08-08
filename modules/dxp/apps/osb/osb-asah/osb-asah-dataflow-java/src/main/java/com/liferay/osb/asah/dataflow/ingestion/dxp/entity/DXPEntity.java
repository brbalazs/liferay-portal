/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp.entity;

import java.util.List;

import org.apache.beam.sdk.schemas.JavaFieldSchema;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;

/**
 * @author Rachael Koestartyo
 */
@DefaultSchema(JavaFieldSchema.class)
public class DXPEntity extends BaseDXPEntity {

	public String classPK;
	public List<Field> expandoFields;
	public List<Field> fields;
	public String id;
	public String modifiedDate;
	public String type;

}