package com.liferay.commerce.google.merchant.xml;

import com.liferay.commerce.google.merchant.xml.model.Feed;
import com.liferay.commerce.google.merchant.xml.model.Link;
import com.liferay.commerce.product.exception.InvalidCommerceChannelTypeException;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.model.CommerceChannelConstants;
import com.liferay.commerce.product.model.CommerceChannelRel;
import com.liferay.commerce.product.service.CommerceChannelLocalService;
import com.liferay.commerce.product.service.CommerceChannelRelLocalService;
import com.liferay.portal.kernel.dao.orm.Criterion;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.ClassName;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;

import java.io.StringWriter;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.liferay.portal.kernel.util.Portal;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Kayleen Lim
 *
 * Implementation for generating XML for Google Merchant Center feed in
 * Atom 1.0 XML format
 */
@Component(
	immediate = true,
	service = ProductFeedXMLGenerator.class
)
public class ProductFeedXMLGenerator {

	public String getCommerceChannelProductsXML(long commerceChannelId)
		throws PortalException {

		CommerceChannel commerceChannel =
			_commerceChannelLocalService.getCommerceChannel(commerceChannelId);

		if (!CommerceChannelConstants.CHANNEL_TYPE_SITE.equals(
			commerceChannel.getType())) {
			throw new InvalidCommerceChannelTypeException(
				String.format(
					"Cannot generate products XML for channel with ID, %s, " +
					"because channel must be site type channel",
					commerceChannelId)
			);
		}

		Feed feed = new Feed();

		feed.setTitle(commerceChannel.getName());

		//TODO COMMERCE-2690 get correct site URL from group
		long groupId = commerceChannel.getSiteGroupId();
		Group group = _groupLocalService.getGroup(groupId);

		String href = _portal.getLayoutSetDisplayURL(group.getPublicLayoutSet(), false);

		Link link = new Link(href);

		feed.setLink(link);

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Instant instant = timestamp.toInstant();
		String updated = instant.toString();

		feed.setUpdated(updated);

		DynamicQuery dynamicQuery =
			_commerceChannelRelLocalService.dynamicQuery();

		Property property = PropertyFactoryUtil.forName("classNameId");
		ClassName className = _classNameLocalService.getClassName(
			CPDefinition.class.getName());
		Criterion criterion = property.eq(className.getClassNameId());

		dynamicQuery.add(criterion);

		List<CommerceChannelRel> commerceChannelRels =
			_commerceChannelRelLocalService.dynamicQuery(dynamicQuery);

		for (CommerceChannelRel commerceChannelRel : commerceChannelRels) {
			//TODO COMMERCE-2690 add XML for a product here
		}

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Feed.class);

			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			StringWriter sw = new StringWriter();
			jaxbMarshaller.marshal(feed, sw);

			return sw.toString();
		} catch (JAXBException jaxbe) {
			throw new PortalException(jaxbe);
		}
	}

	@Reference
	private ClassNameLocalService _classNameLocalService;

	@Reference
	private CommerceChannelLocalService _commerceChannelLocalService;

	@Reference
	private CommerceChannelRelLocalService _commerceChannelRelLocalService;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private Portal _portal;

}