package com.liferay.commerce.google.merchant.xml.util;

import com.liferay.commerce.google.merchant.xml.ProductFeedXMLGenerator;
import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.portal.kernel.exception.PortalException;

import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author Kayleen Lim
 *
 * Util class for generating XML for Google Merchant Center feed in Atom 1.0
 * XML format
 */
public class ProductFeedXMLGeneratorUtil {

	public static String getCommerceChannelProductsXML(long commerceChannelId)
		throws PortalException {

		return _serviceTracker.getService(
		).getCommerceChannelProductsXML(
			commerceChannelId
		);
	}

	private static final ServiceTracker<?, ProductFeedXMLGenerator>
		_serviceTracker = ServiceTrackerFactory.open(
			FrameworkUtil.getBundle(ProductFeedXMLGeneratorUtil.class),
			ProductFeedXMLGenerator.class);

}