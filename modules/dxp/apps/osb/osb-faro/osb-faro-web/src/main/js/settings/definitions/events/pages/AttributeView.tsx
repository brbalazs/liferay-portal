import BasePage from 'settings/components/BasePage';
import Card from 'shared/components/Card';
import Label from 'shared/components/Label';
import NoResultsDisplay from 'shared/components/NoResultsDisplay';
import React from 'react';
import Table from 'shared/components/table';
import {Attribute} from 'event-analysis/utils/types';
import {DateCell} from 'shared/components/table/cell-components';
import {
	EVENT_ATTRIBUTE_DEFINITION_WITH_RECENT_VALUES_QUERY,
	EventAttributeDefinitionData,
	EventAttributeDefinitionVariables
} from 'event-analysis/queries/EventAttributeDefinitionQuery';
import {getDefinitions, getEvents} from 'shared/util/breadcrumbs';
import {HasModal} from 'shared/types';
import {Routes, setUriQueryValues, toRoute} from 'shared/util/router';
import {SafeResults} from 'shared/hoc/util';
import {useParams} from 'react-router-dom';
import {useQuery} from '@apollo/react-hooks';

interface IAttributeViewProps
	extends React.HTMLAttributes<HTMLElement>,
		HasModal {
	groupId: string;
}

const AttributeView: React.FC<IAttributeViewProps> = ({groupId}) => {
	const {attributeId} = useParams();

	const result = useQuery<
		EventAttributeDefinitionData,
		EventAttributeDefinitionVariables
	>(EVENT_ATTRIBUTE_DEFINITION_WITH_RECENT_VALUES_QUERY, {
		variables: {id: attributeId}
	});

	const viewAttributePageActions = [
		{
			href: setUriQueryValues(
				{edit: true},
				toRoute(Routes.SETTINGS_DEFINITIONS_ATTRIBUTES_VIEW, {
					attributeId,
					groupId
				})
			),
			label: Liferay.Language.get('edit')
		}
	];

	return (
		<SafeResults {...result}>
			{({
				eventAttributeDefinition
			}: {
				eventAttributeDefinition: Attribute;
			}) => {
				const {
					dataType,
					description,
					displayName,
					name,
					recentValues
				} = eventAttributeDefinition;

				return (
					<BasePage
						breadcrumbItems={[
							getDefinitions({groupId}),
							getEvents({groupId}),
							{active: true, label: name}
						]}
						groupId={groupId}
						pageActions={viewAttributePageActions}
						pageDescription={
							description ? (
								<>
									<div>{description}</div>

									<Label display='primary' uppercase>
										{dataType}
									</Label>
								</>
							) : (
								<div className='no-description'>
									{Liferay.Language.get('no-description')}
								</div>
							)
						}
						pageTitle={name}
						subTitle={displayName}
					>
						{!recentValues.length && (
							<Card>
								<NoResultsDisplay spacer />
							</Card>
						)}

						{!!recentValues.length && (
							<Table
								columns={[
									{
										accessor: 'value',
										label: Liferay.Language.get(
											'sample-data'
										),
										sortable: false
									},
									{
										accessor: 'lastSeenDate',
										cellRenderer: ({data}) => (
											<DateCell
												className='table-column-text-end'
												data={data}
												datePath='lastSeenDate'
											/>
										),
										className: 'table-column-text-end',
										label: Liferay.Language.get(
											'last-seen'
										),
										sortable: false
									}
								]}
								items={recentValues}
								rowIdentifier='sampleData'
							/>
						)}
					</BasePage>
				);
			}}
		</SafeResults>
	);
};

export default AttributeView;
