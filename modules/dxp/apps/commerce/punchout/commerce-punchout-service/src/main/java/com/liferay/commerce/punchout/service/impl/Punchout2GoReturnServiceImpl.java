/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.commerce.punchout.service.impl;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.service.CPInstanceLocalService;
import com.liferay.commerce.punchout.service.PunchoutReturnService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
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

		try {
			JSONObject cartJSONObject = _jsonFactory.createJSONObject();

			cartJSONObject.put("contract_id", StringPool.BLANK);
			cartJSONObject.put(
				"discount", commerceOrder.getTotalDiscountAmount());
			cartJSONObject.put("discount_description", StringPool.BLANK);
			cartJSONObject.put("quote_id", StringPool.BLANK);
			cartJSONObject.put("shipping", commerceOrder.getShippingAmount());
			cartJSONObject.put(
				"shipping_description", commerceOrder.getShippingOptionName());
			cartJSONObject.put("tax", commerceOrder.getTaxAmount());
			cartJSONObject.put("tax_description", StringPool.BLANK);
			cartJSONObject.put("total", commerceOrder.getTotal());

			CommerceCurrency commerceCurrency =
				commerceOrder.getCommerceCurrency();

			cartJSONObject.put("currencyCode", commerceCurrency.getCode());

			JSONArray cartItemJSONArray = _jsonFactory.createJSONArray();

			for (CommerceOrderItem commerceOrderItem :
					commerceOrder.getCommerceOrderItems()) {

				JSONObject cartItemJSONObject = _jsonFactory.createJSONObject();

				cartItemJSONObject.put(
					"supplierauxid",
					commerceOrder.getCommerceOrderId() + "/" +
						commerceOrderItem.getCommerceOrderItemId());
				cartItemJSONObject.put(
					"supplierid", commerceOrderItem.getSku());

				CPDefinition cpDefinition = commerceOrderItem.getCPDefinition();

				cartItemJSONObject.put(
					"description", cpDefinition.getDescription());

				StringBundler assetCategorySB = new StringBundler();

				AssetEntry assetEntry = _assetEntryLocalService.getEntry(
					CPDefinition.class.getName(),
					cpDefinition.getCPDefinitionId());

				for (AssetCategory assetCategory : assetEntry.getCategories()) {
					assetCategorySB.append(assetCategory.getName());

					assetCategorySB.append(StringPool.COMMA);
				}

				cartItemJSONObject.put(
					"classification", assetCategorySB.toString());

				cartItemJSONObject.put("uom", "EA");

				cartItemJSONObject.put(
					"quantity", commerceOrderItem.getQuantity());
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
					"shippingAddressId",
					commerceOrderItem.getShippingAddressId());

				CPInstance cpInstance = _cpInstanceLocalService.fetchCPInstance(
					commerceOrderItem.getCPInstanceId());

				cartItemJSONObject.put("unspsc", cpInstance.getUnspsc());

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

			cartJSONObject.put("items", cartItemJSONArray);

			String cartJSON = cartJSONObject.toString();

			if (_log.isDebugEnabled()) {
				_log.debug(
					"Punchout2Go cart transfer request to " + url +
						"; cart JSON: " + cartJSON);
			}

			URL urlObj = new URL(url);

			HttpURLConnection httpURLConnection =
				(HttpURLConnection)urlObj.openConnection();

			httpURLConnection.setDoOutput(true);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty(
				"Content-type", "application/json");

			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
				httpURLConnection.getOutputStream());

			outputStreamWriter.write(cartJSON);

			outputStreamWriter.flush();

			int responseCode = httpURLConnection.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				String response = StringUtil.toLowerCase(
					StringUtil.read(httpURLConnection.getInputStream()));

				if (_log.isDebugEnabled()) {
					_log.debug(
						"JSON response received from Punchout2Go: " + response);
				}

				JSONArray jsonArray = _jsonFactory.createJSONArray(
					StringPool.OPEN_BRACKET + response +
						StringPool.CLOSE_BRACKET);

				JSONObject jsonObject = jsonArray.getJSONObject(0);

				return jsonObject.getString("redirecturl");
			}

			_log.error(
				"Punchout2Go cart transfer response code: " + responseCode);

			return null;
		}
		catch (Exception e) {
			_log.error("Punchout2Go cart transfer failed", e);

			throw e;
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		Punchout2GoReturnServiceImpl.class);

	@Reference
	private AssetEntryLocalService _assetEntryLocalService;

	@Reference
	private CPInstanceLocalService _cpInstanceLocalService;

	@Reference
	private JSONFactory _jsonFactory;

}