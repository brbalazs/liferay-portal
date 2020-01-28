package com.liferay.commerce.google.merchant.internal.xml;

import com.ctc.wstx.api.WstxOutputProperties;
import com.ctc.wstx.stax.WstxInputFactory;
import com.ctc.wstx.stax.WstxOutputFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import com.liferay.commerce.google.merchant.internal.xml.model.Feed;
import com.liferay.commerce.google.merchant.internal.xml.model.Link;
import com.liferay.commerce.product.catalog.CPCatalogEntry;
import com.liferay.commerce.product.catalog.CPQuery;
import com.liferay.commerce.product.data.source.CPDataSourceResult;
import com.liferay.commerce.product.exception.InvalidCommerceChannelTypeException;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.model.CommerceChannelConstants;
import com.liferay.commerce.product.service.CommerceChannelLocalService;
import com.liferay.commerce.product.util.CPDefinitionHelper;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.io.Serializable;

import java.sql.Timestamp;

import java.time.Instant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Kayleen Lim
 *
 * Implementation for generating XML for Google Merchant Center feed in
 * Atom 1.0 XML format
 */
@Component(immediate = true, service = ProductFeedXMLGenerator.class)
public class ProductFeedXMLGenerator {

	public String generateProductFeedXML(long commerceChannelId)
		throws PortalException {

		CommerceChannel commerceChannel =
			_commerceChannelLocalService.getCommerceChannel(commerceChannelId);

		if (!CommerceChannelConstants.CHANNEL_TYPE_SITE.equals(
				commerceChannel.getType())) {

			throw new InvalidCommerceChannelTypeException(
				String.format(
					"Cannot generate products XML for channel with ID, %s, " +
						"because channel must be site type channel",
					commerceChannelId));
		}

		Feed feed = new Feed();

		feed.setTitle(commerceChannel.getName());

		long siteGroupId = commerceChannel.getSiteGroupId();

		Group group = _groupLocalService.getGroup(siteGroupId);

		String href = _portal.getLayoutSetDisplayURL(
			group.getPublicLayoutSet(), false);

		Link link = new Link(href);

		feed.setLink(link);

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		Instant instant = timestamp.toInstant();

		String updated = instant.toString();

		feed.setUpdated(updated);

		List<CPCatalogEntry> cpCatalogEntries = _getCPCatalogEntriesByChannel(
			commerceChannel);

		for (CPCatalogEntry cpCatalogEntry : cpCatalogEntries) {
			//TODO COMMERCE-2690 add XML for a product here
		}

		try {
			XMLInputFactory xmlInputFactory = new WstxInputFactory();

			XMLOutputFactory xmlOutputFactory = new WstxOutputFactory();

			xmlOutputFactory.setProperty(
				WstxOutputProperties.P_OUTPUT_VALIDATE_ATTR, true);

			XmlFactory xmlFactory = new XmlFactory(
				xmlInputFactory, xmlOutputFactory);

			XmlMapper xmlMapper = new XmlMapper(xmlFactory);

			return xmlMapper.writeValueAsString(feed);
		}
		catch (JsonProcessingException jpe) {
			throw new PortalException(jpe);
		}
	}

	private List<CPCatalogEntry> _getCPCatalogEntriesByChannel(
			CommerceChannel commerceChannel)
		throws PortalException {

		Map<String, Serializable> attributes = new HashMap<>();

		long commerceChannelGroupId = commerceChannel.getGroupId();

		attributes.put(Field.STATUS, WorkflowConstants.STATUS_APPROVED);
		attributes.put("commerceChannelGroupId", commerceChannelGroupId);

		SearchContext searchContext = new SearchContext();

		searchContext.setAttributes(attributes);
		searchContext.setCompanyId(commerceChannel.getCompanyId());

		CPQuery cpQuery = new CPQuery();

		CPDataSourceResult cpDataSourceResult = _cpDefinitionHelper.search(
			commerceChannelGroupId, searchContext, cpQuery, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS);

		return cpDataSourceResult.getCPCatalogEntries();
	}

	@Reference
	private CommerceChannelLocalService _commerceChannelLocalService;

	@Reference
	private CPDefinitionHelper _cpDefinitionHelper;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private Portal _portal;

}