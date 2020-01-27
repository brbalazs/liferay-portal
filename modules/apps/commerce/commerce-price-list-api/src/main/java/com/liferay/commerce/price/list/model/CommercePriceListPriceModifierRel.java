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

package com.liferay.commerce.price.list.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.annotation.ImplementationClassName;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.util.Accessor;

/**
 * The extended model interface for the CommercePriceListPriceModifierRel service. Represents a row in the &quot;CPLPriceModifierRel&quot; database table, with each column mapped to a property of this class.
 *
 * @author Alessio Antonio Rendina
 * @see CommercePriceListPriceModifierRelModel
 * @generated
 */
@ImplementationClassName(
	"com.liferay.commerce.price.list.model.impl.CommercePriceListPriceModifierRelImpl"
)
@ProviderType
public interface CommercePriceListPriceModifierRel
	extends CommercePriceListPriceModifierRelModel, PersistedModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add methods to <code>com.liferay.commerce.price.list.model.impl.CommercePriceListPriceModifierRelImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static final Accessor<CommercePriceListPriceModifierRel, Long>
		COMMERCE_PRICE_LIST_PRICE_MODIFIER_REL_ID_ACCESSOR =
			new Accessor<CommercePriceListPriceModifierRel, Long>() {

				@Override
				public Long get(
					CommercePriceListPriceModifierRel
						commercePriceListPriceModifierRel) {

					return commercePriceListPriceModifierRel.
						getCommercePriceListPriceModifierRelId();
				}

				@Override
				public Class<Long> getAttributeClass() {
					return Long.class;
				}

				@Override
				public Class<CommercePriceListPriceModifierRel> getTypeClass() {
					return CommercePriceListPriceModifierRel.class;
				}

			};

}