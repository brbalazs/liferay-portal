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

package com.liferay.commerce.pricing.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.annotation.ImplementationClassName;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.util.Accessor;

/**
 * The extended model interface for the CommercePricingClassRel service. Represents a row in the &quot;CommercePricingClassRel&quot; database table, with each column mapped to a property of this class.
 *
 * @author Riccardo Alberti
 * @see CommercePricingClassRelModel
 * @generated
 */
@ImplementationClassName(
	"com.liferay.commerce.pricing.model.impl.CommercePricingClassRelImpl"
)
@ProviderType
public interface CommercePricingClassRel
	extends CommercePricingClassRelModel, PersistedModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add methods to <code>com.liferay.commerce.pricing.model.impl.CommercePricingClassRelImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static final Accessor<CommercePricingClassRel, Long>
		COMMERCE_PRICING_CLASS_REL_ID_ACCESSOR =
			new Accessor<CommercePricingClassRel, Long>() {

				@Override
				public Long get(
					CommercePricingClassRel commercePricingClassRel) {

					return commercePricingClassRel.
						getCommercePricingClassRelId();
				}

				@Override
				public Class<Long> getAttributeClass() {
					return Long.class;
				}

				@Override
				public Class<CommercePricingClassRel> getTypeClass() {
					return CommercePricingClassRel.class;
				}

			};

}