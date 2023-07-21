/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.workflow.kaleo.definition.internal.export.builder;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.workflow.kaleo.definition.Definition;
import com.liferay.portal.workflow.kaleo.definition.Node;
import com.liferay.portal.workflow.kaleo.definition.Transition;
import com.liferay.portal.workflow.kaleo.model.KaleoDefinition;
import com.liferay.portal.workflow.kaleo.model.KaleoNode;
import com.liferay.portal.workflow.kaleo.model.KaleoTransition;
import com.liferay.portal.workflow.kaleo.service.KaleoDefinitionLocalService;
import com.liferay.portal.workflow.kaleo.service.KaleoNodeLocalService;
import com.liferay.portal.workflow.kaleo.service.KaleoTransitionLocalService;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(immediate = true, service = DefinitionBuilder.class)
public class DefaultDefinitionBuilder implements DefinitionBuilder {

	@Override
	public Definition buildDefinition(long kaleoDefinitionId)
		throws PortalException {

		KaleoDefinition kaleoDefinition =
			_kaleoDefinitionLocalService.getKaleoDefinition(kaleoDefinitionId);

		return doBuildDefinition(kaleoDefinition);
	}

	@Override
	public Definition buildDefinition(long companyId, String name, int version)
		throws PortalException {

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setCompanyId(companyId);

		KaleoDefinition kaleoDefinition =
			_kaleoDefinitionLocalService.getKaleoDefinition(
				name, serviceContext);

		return doBuildDefinition(kaleoDefinition);
	}

	protected Definition doBuildDefinition(KaleoDefinition kaleoDefinition)
		throws PortalException {

		Definition definition = new Definition(
			kaleoDefinition.getName(), kaleoDefinition.getDescription(),
			kaleoDefinition.getContent(), kaleoDefinition.getVersion());

		List<KaleoNode> kaleoNodes =
			_kaleoNodeLocalService.getKaleoDefinitionVersionKaleoNodes(
				kaleoDefinition.getKaleoDefinitionId());

		for (KaleoNode kaleoNode : kaleoNodes) {
			NodeBuilder nodeBuilder = _nodeBuilderRegistry.getNodeBuilder(
				kaleoNode.getType());

			Node node = nodeBuilder.buildNode(kaleoNode);

			definition.addNode(node);
		}

		List<KaleoTransition> kaleoTransitions =
			_kaleoTransitionLocalService.
				getKaleoDefinitionVersionKaleoTransitions(
					kaleoDefinition.getKaleoDefinitionId());

		for (KaleoTransition kaleoTransition : kaleoTransitions) {
			String sourceNodeName = kaleoTransition.getSourceKaleoNodeName();

			Node sourceNode = definition.getNode(sourceNodeName);

			String targetNodeName = kaleoTransition.getTargetKaleoNodeName();

			Node targetNode = definition.getNode(targetNodeName);

			Transition transition = new Transition(
				kaleoTransition.getName(), sourceNode, targetNode,
				kaleoTransition.isDefaultTransition());

			sourceNode.addOutgoingTransition(transition);

			targetNode.addIncomingTransition(transition);
		}

		return definition;
	}

	@Reference
	private KaleoDefinitionLocalService _kaleoDefinitionLocalService;

	@Reference
	private KaleoNodeLocalService _kaleoNodeLocalService;

	@Reference
	private KaleoTransitionLocalService _kaleoTransitionLocalService;

	@Reference
	private NodeBuilderRegistry _nodeBuilderRegistry;

}