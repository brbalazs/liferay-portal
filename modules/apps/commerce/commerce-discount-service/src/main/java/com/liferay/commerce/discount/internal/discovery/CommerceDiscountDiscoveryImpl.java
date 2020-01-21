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

package com.liferay.commerce.discount.internal.discovery;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.model.CommerceAccountGroup;
import com.liferay.commerce.account.service.CommerceAccountGroupLocalService;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.model.CommerceMoney;
import com.liferay.commerce.currency.model.CommerceMoneyFactory;
import com.liferay.commerce.discount.CommerceDiscountLevel;
import com.liferay.commerce.discount.CommerceDiscountValue;
import com.liferay.commerce.discount.constants.CommerceDiscountConstants;
import com.liferay.commerce.discount.discovery.CommerceDiscountDiscovery;
import com.liferay.commerce.discount.model.CommerceDiscount;
import com.liferay.commerce.discount.model.CommerceDiscountRule;
import com.liferay.commerce.discount.rule.type.CommerceDiscountRuleType;
import com.liferay.commerce.discount.rule.type.CommerceDiscountRuleTypeRegistry;
import com.liferay.commerce.discount.service.CommerceDiscountLocalService;
import com.liferay.commerce.discount.service.CommerceDiscountRuleLocalService;
import com.liferay.commerce.discount.target.CommerceDiscountTarget;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.price.CommercePriceValue;
import com.liferay.commerce.price.list.model.CommercePriceListDiscountRel;
import com.liferay.commerce.price.list.service.CommercePriceListDiscountRelLocalService;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.service.CPInstanceLocalService;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Riccardo Alberti
 */
@Component(service = CommerceDiscountDiscovery.class)
public class CommerceDiscountDiscoveryImpl
	implements CommerceDiscountDiscovery {

	@Override
	public BigDecimal applyCommerceDiscounts(
		BigDecimal commercePrice,
		CommerceDiscountLevel[] commerceDiscountLevels) {

		if (commerceDiscountLevels != null) {

			// apply the algorithm for discounts

		}

		return commercePrice;
	}

	@Override
	public List<CommerceDiscount> getOrderCommerceDiscount(
			long commerceAccountId, long[] commerceAccountGroupIds,
			long commerceChannelId, String commerceDiscountTargetType)
		throws PortalException {

		return _commerceDiscountLocalService.findByA_A_C_or_U_Order(
			commerceAccountId, commerceAccountGroupIds, commerceChannelId,
			commerceDiscountTargetType);
	}

	@Override
	public List<CommerceDiscount> getOrderCommerceDiscountByHierarchy(
			CommerceContext commerceContext,
			CommerceDiscountTarget.Type commerceDiscountTargetType)
		throws PortalException {

		CommerceAccount commerceAccount = commerceContext.getCommerceAccount();

		long commerceAccountId = 0;

		if (commerceAccount != null) {
			commerceAccountId = commerceAccount.getCommerceAccountId();
		}

		List<CommerceAccountGroup> commerceAccountGroups =
			_commerceAccountGroupLocalService.
				getCommerceAccountGroupsByCommerceAccountId(
					commerceAccount.getCommerceAccountId());

		Stream<CommerceAccountGroup> stream = commerceAccountGroups.stream();

		long[] commerceAccountGroupIds = stream.mapToLong(
			CommerceAccountGroup::getCommerceAccountGroupId
		).toArray();

		return getOrderCommerceDiscountByHierarchy(
			commerceAccountId, commerceAccountGroupIds,
			commerceContext.getCommerceChannelId(), commerceDiscountTargetType);
	}

	@Override
	public List<CommerceDiscount> getOrderCommerceDiscountByHierarchy(
			long commerceAccountId, long[] commerceAccountGroupIds,
			long commerceChannelId,
			CommerceDiscountTarget.Type commerceDiscountTargetType)
		throws PortalException {

		List<CommerceDiscount> commerceDiscounts =
			_commerceDiscountLocalService.findByA_C_C_Order(
				commerceAccountId, commerceDiscountTargetType.toString());

		if (commerceDiscounts != null) {
			return commerceDiscounts;
		}

		commerceDiscounts = _commerceDiscountLocalService.findByAG_C_C_Order(
			commerceAccountGroupIds, commerceDiscountTargetType.toString());

		if (commerceDiscounts != null) {
			return commerceDiscounts;
		}

		commerceDiscounts = _commerceDiscountLocalService.findByC_C_C_Order(
			commerceChannelId, commerceDiscountTargetType.toString());

		if (commerceDiscounts != null) {
			return commerceDiscounts;
		}

		return _commerceDiscountLocalService.findByUnqualifiedOrder(
			commerceDiscountTargetType.toString());
	}

	@Override
	public CommerceDiscountValue getOrderShippingCommerceDiscountValue(
			CommerceOrder commerceOrder, BigDecimal shippingAmount,
			CommerceContext commerceContext)
		throws PortalException {

		if (commerceOrder == null) {
			return null;
		}

		return _getCommerceDiscountValue(
			shippingAmount, commerceContext,
			CommerceDiscountTarget.Type.APPLY_TO_SHIPPING);
	}

	@Override
	public CommerceDiscountValue getOrderSubtotalCommerceDiscountValue(
			CommerceOrder commerceOrder, BigDecimal subtotalAmount,
			CommerceContext commerceContext)
		throws PortalException {

		if (commerceOrder == null) {
			return null;
		}

		return _getCommerceDiscountValue(
			subtotalAmount, commerceContext,
			CommerceDiscountTarget.Type.APPLY_TO_SUBTOTAL);
	}

	@Override
	public CommerceDiscountValue getOrderTotalCommerceDiscountValue(
			CommerceOrder commerceOrder, BigDecimal totalAmount,
			CommerceContext commerceContext)
		throws PortalException {

		if (commerceOrder == null) {
			return null;
		}

		return _getCommerceDiscountValue(
			totalAmount, commerceContext,
			CommerceDiscountTarget.Type.APPLY_TO_TOTAL);
	}

	@Override
	public List<CommerceDiscount> getProductCommerceDiscount(
			long commerceAccountId, long[] commerceAccountGroupIds,
			long commerceChannelId, long cpDefinitionId)
		throws PortalException {

		return _commerceDiscountLocalService.findByA_A_C_or_U_Product(
			commerceAccountId, commerceAccountGroupIds, commerceChannelId,
			cpDefinitionId);
	}

	@Override
	public List<CommerceDiscount> getProductCommerceDiscountByHierarchy(
			CommerceContext commerceContext, long cpDefinitionId)
		throws PortalException {

		CommerceAccount commerceAccount = commerceContext.getCommerceAccount();

		long commerceAccountId = 0;

		if (commerceAccount != null) {
			commerceAccountId = commerceAccount.getCommerceAccountId();
		}

		List<CommerceAccountGroup> commerceAccountGroups =
			_commerceAccountGroupLocalService.
				getCommerceAccountGroupsByCommerceAccountId(
					commerceAccount.getCommerceAccountId());

		Stream<CommerceAccountGroup> stream = commerceAccountGroups.stream();

		long[] commerceAccountGroupIds = stream.mapToLong(
			CommerceAccountGroup::getCommerceAccountGroupId
		).toArray();

		return getProductCommerceDiscountByHierarchy(
			commerceAccountId, commerceAccountGroupIds,
			commerceContext.getCommerceChannelId(), cpDefinitionId);
	}

	@Override
	public List<CommerceDiscount> getProductCommerceDiscountByHierarchy(
			long commerceAccountId, long[] commerceAccountGroupIds,
			long commerceChannelId, long cpDefinitionId)
		throws PortalException {

		List<CommerceDiscount> commerceDiscounts =
			_commerceDiscountLocalService.findByA_C_C_Product(
				commerceAccountId, cpDefinitionId);

		if (commerceDiscounts != null) {
			return commerceDiscounts;
		}

		commerceDiscounts = _commerceDiscountLocalService.findByAG_C_C_Product(
			commerceAccountGroupIds, cpDefinitionId);

		if (commerceDiscounts != null) {
			return commerceDiscounts;
		}

		commerceDiscounts = _commerceDiscountLocalService.findByC_C_C_Product(
			commerceChannelId, cpDefinitionId);

		if (commerceDiscounts != null) {
			return commerceDiscounts;
		}

		return _commerceDiscountLocalService.findByUnqualifiedProduct(
			cpDefinitionId);
	}

	@Override
	public CommerceDiscountLevel[] getProductCommerceDiscountLevels(
			long commercePriceListId, BigDecimal commercePrice, int quantity,
			CommerceContext commerceContext, long cpInstanceId)
		throws PortalException {

		CPInstance cpInstance = _cpInstanceLocalService.getCPInstance(
			cpInstanceId);

		List<CommercePriceListDiscountRel> commercePriceListDiscountRels =
			_commercePriceListDiscountRelLocalService.
				getCommercePriceListDiscountRels(commercePriceListId);

		if (commercePriceListDiscountRels != null) {
			Stream<CommercePriceListDiscountRel> stream =
				commercePriceListDiscountRels.stream();

			long[] commerceDiscountIds = stream.mapToLong(
				CommercePriceListDiscountRel::getCommerceDiscountId
			).toArray();

			List<CommerceDiscount> commerceDiscounts =
				_commerceDiscountLocalService.findPriceListDiscountProduct(
					commerceDiscountIds, cpInstance.getCPDefinitionId());

			return _getCommerceDiscountLevels(
				commercePrice, quantity, commerceContext, commerceDiscounts);
		}

		List<CommerceDiscount> commerceDiscounts =
			getProductCommerceDiscountByHierarchy(
				commerceContext, cpInstance.getCPDefinitionId());

		return _getCommerceDiscountLevels(
			commercePrice, quantity, commerceContext, commerceDiscounts);
	}

	@Override
	public CommerceDiscountValue getProductCommerceDiscountValue(
		BigDecimal finalPrice, BigDecimal finalDiscountedPrice,
		List<CommercePriceValue> finalPriceValues,
		CommerceCurrency commerceCurrency) {

		return null;
	}

	private CommerceDiscountLevel _getCommerceDiscountLevel(
		CommerceDiscountLevel currentDiscountLevel, BigDecimal commercePrice,
		int quantity, CommerceCurrency commerceCurrency,
		long commerceDiscountId, BigDecimal commerceDiscountValue,
		boolean isUsePercentage) {

		BigDecimal discountAmount = BigDecimal.ZERO;

		if (isUsePercentage) {
			discountAmount = commercePrice.multiply(commerceDiscountValue);
			discountAmount = discountAmount.divide(_ONE_HUNDRED);
		}
		else {
			discountAmount = commerceDiscountValue;
		}

		CommerceMoney amount = _commerceMoneyFactory.create(
			commerceCurrency,
			discountAmount.multiply(new BigDecimal(quantity)));

		if (currentDiscountLevel == null) {
			return new CommerceDiscountLevel(
				commerceDiscountId, isUsePercentage, commerceDiscountValue,
				amount.getPrice());
		}

		BigDecimal currentDiscountAmount =
			currentDiscountLevel.getDiscountAmount();

		if (currentDiscountAmount.compareTo(amount.getPrice()) < 0) {
			return new CommerceDiscountLevel(
				commerceDiscountId, isUsePercentage, commerceDiscountValue,
				amount.getPrice());
		}

		return currentDiscountLevel;
	}

	private CommerceDiscountLevel[] _getCommerceDiscountLevels(
			BigDecimal commercePrice, int quantity,
			CommerceContext commerceContext,
			List<CommerceDiscount> commerceDiscounts)
		throws PortalException {

		String couponCode = null;

		CommerceOrder commerceOrder = commerceContext.getCommerceOrder();

		if (commerceOrder != null) {
			couponCode = commerceOrder.getCouponCode();
		}

		CommerceCurrency commerceCurrency =
			commerceContext.getCommerceCurrency();

		CommerceDiscountLevel[] levels = new CommerceDiscountLevel[4];

		for (CommerceDiscount commerceDiscount : commerceDiscounts) {
			String discountCouponCode = commerceDiscount.getCouponCode();

			if ((discountCouponCode != null) &&
				!Objects.equals(couponCode, discountCouponCode)) {

				continue;
			}

			if (_isValidDiscount(commerceContext, commerceDiscount)) {
				String discountLevel = commerceDiscount.getLevel();

				if (discountLevel.isEmpty()) {
					if (commerceDiscount.isUsePercentage()) {
						levels[0] = _getCommerceDiscountLevel(
							levels[0], commercePrice, quantity,
							commerceCurrency,
							commerceDiscount.getCommerceDiscountId(),
							commerceDiscount.getLevel1(),
							commerceDiscount.isUsePercentage());

						levels[1] = _getCommerceDiscountLevel(
							levels[1], commercePrice, quantity,
							commerceCurrency,
							commerceDiscount.getCommerceDiscountId(),
							commerceDiscount.getLevel1(),
							commerceDiscount.isUsePercentage());

						levels[2] = _getCommerceDiscountLevel(
							levels[2], commercePrice, quantity,
							commerceCurrency,
							commerceDiscount.getCommerceDiscountId(),
							commerceDiscount.getLevel1(),
							commerceDiscount.isUsePercentage());

						levels[3] = _getCommerceDiscountLevel(
							levels[3], commercePrice, quantity,
							commerceCurrency,
							commerceDiscount.getCommerceDiscountId(),
							commerceDiscount.getLevel1(),
							commerceDiscount.isUsePercentage());
					}
					else {
						levels[0] = _getCommerceDiscountLevel(
							levels[0], commercePrice, quantity,
							commerceCurrency,
							commerceDiscount.getCommerceDiscountId(),
							commerceDiscount.getLevel1(),
							commerceDiscount.isUsePercentage());
					}
				}

				if (discountLevel.equals(CommerceDiscountConstants.LEVEL1)) {
					levels[0] = _getCommerceDiscountLevel(
						levels[0], commercePrice, quantity, commerceCurrency,
						commerceDiscount.getCommerceDiscountId(),
						commerceDiscount.getLevel1(),
						commerceDiscount.isUsePercentage());
				}
				else if (discountLevel.equals(
							CommerceDiscountConstants.LEVEL2)) {

					levels[1] = _getCommerceDiscountLevel(
						levels[1], commercePrice, quantity, commerceCurrency,
						commerceDiscount.getCommerceDiscountId(),
						commerceDiscount.getLevel2(),
						commerceDiscount.isUsePercentage());
				}
				else if (discountLevel.equals(
							CommerceDiscountConstants.LEVEL3)) {

					levels[2] = _getCommerceDiscountLevel(
						levels[2], commercePrice, quantity, commerceCurrency,
						commerceDiscount.getCommerceDiscountId(),
						commerceDiscount.getLevel3(),
						commerceDiscount.isUsePercentage());
				}
				else {
					levels[3] = _getCommerceDiscountLevel(
						levels[3], commercePrice, quantity, commerceCurrency,
						commerceDiscount.getCommerceDiscountId(),
						commerceDiscount.getLevel4(),
						commerceDiscount.isUsePercentage());
				}
			}
		}

		return levels;
	}

	private CommerceDiscountValue _getCommerceDiscountValue(
			BigDecimal amount, CommerceContext commerceContext,
			CommerceDiscountTarget.Type discountType)
		throws PortalException {

		List<CommerceDiscount> commerceDiscounts =
			getOrderCommerceDiscountByHierarchy(commerceContext, discountType);

		CommerceDiscountLevel[] commerceDiscountLevels =
			_getCommerceDiscountLevels(
				amount, 1, commerceContext, commerceDiscounts);

		BigDecimal discountedAmount = applyCommerceDiscounts(
			amount, commerceDiscountLevels);

		BigDecimal currentDiscountAmount = amount.subtract(discountedAmount);

		CommerceCurrency commerceCurrency =
			commerceContext.getCommerceCurrency();

		RoundingMode roundingMode = RoundingMode.valueOf(
			commerceCurrency.getRoundingMode());

		CommerceMoney discountAmount = _commerceMoneyFactory.create(
			commerceCurrency, currentDiscountAmount);

		return new CommerceDiscountValue(
			0, discountAmount,
			_getDiscountPercentage(discountedAmount, amount, roundingMode),
			_getValues(commerceDiscountLevels));
	}

	private BigDecimal _getDiscountPercentage(
		BigDecimal discountedAmount, BigDecimal amount,
		RoundingMode roundingMode) {

		double actualPrice = discountedAmount.doubleValue();
		double originalPrice = amount.doubleValue();

		double percentage = actualPrice / originalPrice;

		BigDecimal discountPercentage = new BigDecimal(percentage);

		discountPercentage = discountPercentage.multiply(_ONE_HUNDRED);

		MathContext mathContext = new MathContext(
			discountPercentage.precision(), roundingMode);

		return _ONE_HUNDRED.subtract(discountPercentage, mathContext);
	}

	private BigDecimal[] _getValues(
		CommerceDiscountLevel[] commerceDiscountLevels) {

		BigDecimal[] values = {
			BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO
		};

		if (commerceDiscountLevels[0] != null) {
			values[0] = commerceDiscountLevels[0].getDiscountValue();
		}

		if (commerceDiscountLevels[1] != null) {
			values[1] = commerceDiscountLevels[1].getDiscountValue();
		}

		if (commerceDiscountLevels[2] != null) {
			values[2] = commerceDiscountLevels[2].getDiscountValue();
		}

		if (commerceDiscountLevels[3] != null) {
			values[3] = commerceDiscountLevels[3].getDiscountValue();
		}

		return values;
	}

	private boolean _isValidDiscount(
			CommerceContext commerceContext, CommerceDiscount commerceDiscount)
		throws PortalException {

		List<CommerceDiscountRule> commerceDiscountRules =
			_commerceDiscountRuleLocalService.getCommerceDiscountRules(
				commerceDiscount.getCommerceDiscountId(), QueryUtil.ALL_POS,
				QueryUtil.ALL_POS, null);

		for (CommerceDiscountRule commerceDiscountRule :
				commerceDiscountRules) {

			CommerceDiscountRuleType commerceDiscountRuleType =
				_commerceDiscountRuleTypeRegistry.getCommerceDiscountRuleType(
					commerceDiscountRule.getType());

			boolean commerceDiscountRuleTypeEvaluation =
				commerceDiscountRuleType.evaluate(
					commerceDiscountRule, commerceContext);

			if (!commerceDiscountRuleTypeEvaluation &&
				commerceDiscount.isRulesConjunction()) {

				return false;
			}
			else if (commerceDiscountRuleTypeEvaluation &&
					 !commerceDiscount.isRulesConjunction()) {

				return true;
			}
		}

		return commerceDiscount.isRulesConjunction();
	}

	private static final BigDecimal _ONE_HUNDRED = BigDecimal.valueOf(100);

	@Reference
	private CommerceAccountGroupLocalService _commerceAccountGroupLocalService;

	@Reference
	private CommerceDiscountLocalService _commerceDiscountLocalService;

	@Reference
	private CommerceDiscountRuleLocalService _commerceDiscountRuleLocalService;

	@Reference
	private CommerceDiscountRuleTypeRegistry _commerceDiscountRuleTypeRegistry;

	@Reference
	private CommerceMoneyFactory _commerceMoneyFactory;

	@Reference
	private CommercePriceListDiscountRelLocalService
		_commercePriceListDiscountRelLocalService;

	@Reference
	private CPInstanceLocalService _cpInstanceLocalService;

}