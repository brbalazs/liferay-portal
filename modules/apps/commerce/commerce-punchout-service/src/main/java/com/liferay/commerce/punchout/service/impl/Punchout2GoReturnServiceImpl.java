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

package com.liferay.commerce.punchout.service.impl;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.punchout.service.PunchoutReturnService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;

import java.io.OutputStreamWriter;

import java.net.HttpURLConnection;
import java.net.URL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jaclyn Ong
 */
@Component(
	immediate = true, property = "service.ranking:Integer=100",
	service = PunchoutReturnService.class
)
public class Punchout2GoReturnServiceImpl implements PunchoutReturnService {

	public String returnToPunchoutVendor(
			CommerceOrder commerceOrder, String url)
		throws Exception {

		URL urlObj = new URL(url);

		HttpURLConnection httpURLConnection =
			(HttpURLConnection)urlObj.openConnection();

		httpURLConnection.setDoOutput(true);
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setRequestProperty(
			"Content-type", "application/json");

		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
			httpURLConnection.getOutputStream());

		JSONObject cartJSONObject = _jsonFactory.createJSONObject();

		cartJSONObject.put("contract_id", StringPool.BLANK);
		cartJSONObject.put("discount", commerceOrder.getTotalDiscountAmount());
		cartJSONObject.put("discount_description", StringPool.BLANK);
		cartJSONObject.put("quote_id", StringPool.BLANK);
		cartJSONObject.put("shipping", commerceOrder.getShippingAmount());
		cartJSONObject.put(
			"shipping_description", commerceOrder.getShippingOptionName());
		cartJSONObject.put("tax", commerceOrder.getTaxAmount());
		cartJSONObject.put("tax_description", StringPool.BLANK);
		cartJSONObject.put("total", commerceOrder.getTotal());

		JSONArray cartItemJSONArray = _jsonFactory.createJSONArray();

		for (CommerceOrderItem commerceOrderItem :
				commerceOrder.getCommerceOrderItems()) {

			JSONObject cartItemJSONObject = _jsonFactory.createJSONObject();

			cartItemJSONObject.put(
				"supplierauxid",
				commerceOrder.getCommerceOrderId() + "/" +
					commerceOrderItem.getCommerceOrderItemId());
			cartItemJSONObject.put("supplierid", commerceOrderItem.getSku());

			CPDefinition cpDefinition = commerceOrderItem.getCPDefinition();

			cartItemJSONObject.put(
				"description", cpDefinition.getDescription());

			AssetEntry assetEntry = _assetEntryLocalService.getEntry(
				CPDefinition.class.getName(), cpDefinition.getCPDefinitionId());

			StringBundler assetCategorySB = new StringBundler();

			for (AssetCategory assetCategory : assetEntry.getCategories()) {
				assetCategorySB.append(assetCategory.getName());

				assetCategorySB.append(StringPool.COMMA);
			}

			cartItemJSONObject.put(
				"classification", assetCategorySB.toString());

			cartItemJSONObject.put("uom", "EA");

			cartItemJSONObject.put("quantity", commerceOrderItem.getQuantity());
			cartItemJSONObject.put(
				"unitprice", commerceOrderItem.getUnitPrice());

			cartItemJSONObject.put(
				"commercePriceListId",
				commerceOrderItem.getCommercePriceListId());
			cartItemJSONObject.put(
				"deliveryGroup", commerceOrderItem.getDeliveryGroup());
			cartItemJSONObject.put(
				"discountAmount", commerceOrderItem.getDiscountAmount());
			cartItemJSONObject.put(
				"externalReferenceCode",
				commerceOrderItem.getExternalReferenceCode());
			cartItemJSONObject.put(
				"finalPrice", commerceOrderItem.getFinalPrice());
			cartItemJSONObject.put(
				"parentCommerceOrderItemId",
				commerceOrderItem.getParentCommerceOrderItemId());
			cartItemJSONObject.put(
				"printedNote", commerceOrderItem.getPrintedNote());
			cartItemJSONObject.put(
				"promoPrice", commerceOrderItem.getPromoPrice());
			cartItemJSONObject.put(
				"shippedQuantity", commerceOrderItem.getShippedQuantity());
			cartItemJSONObject.put(
				"shippingAddressId", commerceOrderItem.getShippingAddressId());

			DateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss'Z'");

			if (commerceOrderItem.getCreateDate() != null) {
				String createDateString = dateFormat.format(
					commerceOrderItem.getCreateDate());

				cartItemJSONObject.put("createDate", createDateString);
			}

			if (commerceOrderItem.getModifiedDate() != null) {
				String modifiedDateString = dateFormat.format(
					commerceOrderItem.getModifiedDate());

				cartItemJSONObject.put("modifiedDate", modifiedDateString);
			}

			if (commerceOrderItem.getRequestedDeliveryDate() != null) {
				String requestedDeliveryDateString = dateFormat.format(
					commerceOrderItem.getRequestedDeliveryDate());

				cartItemJSONObject.put(
					"requestedDeliveryDate", requestedDeliveryDateString);
			}

			cartItemJSONArray.put(cartItemJSONObject);
		}

		String cartItemJSON = cartItemJSONArray.toString();

		cartJSONObject.put("items", cartItemJSON);

		String cartJSON = cartJSONObject.toString();

		outputStreamWriter.write(cartJSON);

		outputStreamWriter.flush();

		int responseCode = httpURLConnection.getResponseCode();

		if (responseCode == HttpURLConnection.HTTP_OK) {
			String json = StringUtil.toLowerCase(
				StringUtil.read(httpURLConnection.getInputStream()));

			JSONArray jsonArray = _jsonFactory.createJSONArray(
				StringPool.OPEN_BRACKET + json + StringPool.CLOSE_BRACKET);

			JSONObject jsonObject = jsonArray.getJSONObject(0);

			return jsonObject.getString("redirecturl");
		}

		return null;
	}

	@Reference
	private AssetEntryLocalService _assetEntryLocalService;

	@Reference
	private JSONFactory _jsonFactory;

}