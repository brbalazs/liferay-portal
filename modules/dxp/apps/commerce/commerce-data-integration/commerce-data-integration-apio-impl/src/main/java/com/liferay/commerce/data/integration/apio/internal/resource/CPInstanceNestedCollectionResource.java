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

package com.liferay.commerce.data.integration.apio.internal.resource;

import static com.liferay.portal.apio.idempotent.Idempotent.idempotent;

import com.liferay.apio.architect.pagination.PageItems;
import com.liferay.apio.architect.pagination.Pagination;
import com.liferay.apio.architect.representor.Representor;
import com.liferay.apio.architect.resource.NestedCollectionResource;
import com.liferay.apio.architect.routes.ItemRoutes;
import com.liferay.apio.architect.routes.NestedCollectionRoutes;
import com.liferay.commerce.data.integration.apio.identifiers.CPDefinitionIdentifier;
import com.liferay.commerce.data.integration.apio.identifiers.CPInstanceIdentifier;
import com.liferay.commerce.data.integration.apio.internal.form.CPInstanceUpserterForm;
import com.liferay.commerce.data.integration.apio.internal.util.CPInstanceHelper;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.service.CPInstanceService;
import com.liferay.portal.apio.permission.HasPermission;
import com.liferay.portal.apio.user.CurrentUser;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rodrigo Guedes de Souza
 */
@Component(immediate = true)
public class CPInstanceNestedCollectionResource
	implements NestedCollectionResource
		<CPInstance, Long, CPInstanceIdentifier, Long, CPDefinitionIdentifier> {

	@Override
	public NestedCollectionRoutes<CPInstance, Long, Long> collectionRoutes(
		NestedCollectionRoutes.Builder<CPInstance, Long, Long> builder) {

		return builder.addGetter(
			this::_getPageItems
		).addCreator(
			this::_addCPInstance, CurrentUser.class,
			_hasPermission.forAddingIn(CPDefinitionIdentifier.class),
			CPInstanceUpserterForm::buildForm
		).build();
	}

	@Override
	public String getName() {
		return "commerce-product-instance";
	}

	@Override
	public ItemRoutes<CPInstance, Long> itemRoutes(
		ItemRoutes.Builder<CPInstance, Long> builder) {

		return builder.addGetter(
			_cpInstanceService::getCPInstance
		).addRemover(
			idempotent(_cpInstanceService::deleteCPInstance),
			_hasPermission::forDeleting
		).build();
	}

	@Override
	public Representor<CPInstance> representor(
		Representor.Builder<CPInstance, Long> builder) {

		return builder.types(
			"CommerceProductInstance"
		).identifier(
			CPInstance::getCPInstanceId
		).addBidirectionalModel(
			"commerceProductDefinition", "commerceProductInstances",
			CPDefinitionIdentifier.class, CPInstance::getCPDefinitionId
		).addDate(
			"dateCreated", CPInstance::getCreateDate
		).addDate(
			"dateModified", CPInstance::getModifiedDate
		).addString(
			"externalReferenceCode", CPInstance::getExternalReferenceCode
		).addString(
			"sku", CPInstance::getSku
		).build();
	}

	private CPInstance _addCPInstance(
			Long cpDefinitionId, CPInstanceUpserterForm cpInstanceUpserterForm,
			User currentUser)
		throws PortalException {

		return _cpInstanceHelper.upsertCPInstance(
			cpDefinitionId, cpInstanceUpserterForm.getSku(),
			cpInstanceUpserterForm.getGtin(),
			cpInstanceUpserterForm.getManufacturerPartNumber(),
			cpInstanceUpserterForm.getPurchasable(),
			cpInstanceUpserterForm.getWidth(),
			cpInstanceUpserterForm.getHeight(),
			cpInstanceUpserterForm.getDepth(),
			cpInstanceUpserterForm.getWeight(),
			cpInstanceUpserterForm.getCost(), cpInstanceUpserterForm.getPrice(),
			cpInstanceUpserterForm.getPromoPrice(),
			cpInstanceUpserterForm.getPublished(),
			cpInstanceUpserterForm.getDisplayDate(),
			cpInstanceUpserterForm.getExpirationDate(),
			cpInstanceUpserterForm.getNeverExpire(),
			cpInstanceUpserterForm.getExternalReferenceCode(), currentUser);
	}

	private PageItems<CPInstance> _getPageItems(
			Pagination pagination, Long cpDefinitionId)
		throws PortalException {

		List<CPInstance> cpInstances =
			_cpInstanceService.getCPDefinitionInstances(
				cpDefinitionId, WorkflowConstants.STATUS_ANY,
				pagination.getStartPosition(), pagination.getEndPosition(),
				null);

		int total = _cpInstanceService.getCPDefinitionInstancesCount(
			cpDefinitionId, WorkflowConstants.STATUS_APPROVED);

		return new PageItems<>(cpInstances, total);
	}

	@Reference
	private CPInstanceHelper _cpInstanceHelper;

	@Reference
	private CPInstanceService _cpInstanceService;

	@Reference(
		target = "(model.class.name=com.liferay.commerce.product.model.CPInstance)"
	)
	private HasPermission<Long> _hasPermission;

}