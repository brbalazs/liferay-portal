import BasePage from 'settings/components/BasePage';
import Card from 'shared/components/Card';
import EVENT_ATTRIBUTE_DEFINITION_QUERY, {
	EVENT_ATTRIBUTE_DEFINITION_WITH_RECENT_VALUES_QUERY,
	EventAttributeDefinitionData,
	EventAttributeDefinitionVariables,
	UPDATE_EVENT_ATTRIBUTE_DEFINITION
} from 'event-analysis/queries/EventAttributeDefinitionQuery';
import Label from 'shared/components/Label';
import NoResultsDisplay from 'shared/components/NoResultsDisplay';
import React from 'react';
import Table from 'shared/components/table';
import {Attribute} from 'event-analysis/utils/types';
import {close, modalTypes, open} from 'shared/actions/modals';
import {connect} from 'react-redux';
import {DateCell} from 'shared/components/table/cell-components';
import {getDefinitions, getEventAttributes} from 'shared/util/breadcrumbs';
import {HasModal, Modal} from 'shared/types';
import {SafeResults} from 'shared/hoc/util';
import {useQuery} from '@apollo/react-hooks';

interface IAttributeViewProps
	extends React.HTMLAttributes<HTMLElement>,
		HasModal {
	attributeId: string;
	close: Modal.close;
	groupId: string;
	open: Modal.open;
}

const AttributeView: React.FC<IAttributeViewProps> = ({
	attributeId,
	close,
	groupId,
	open
}) => {
	const result = useQuery<
		EventAttributeDefinitionData,
		EventAttributeDefinitionVariables
	>(EVENT_ATTRIBUTE_DEFINITION_WITH_RECENT_VALUES_QUERY, {
		variables: {id: attributeId}
	});

	const viewAttributePageActions = [
		{
			label: Liferay.Language.get('edit'),
			onClick: () =>
				open(modalTypes.EDIT_ATTRIBUTE_EVENT_MODAL, {
					id: attributeId,
					mutation: UPDATE_EVENT_ATTRIBUTE_DEFINITION,
					onCancel: close,
					query: EVENT_ATTRIBUTE_DEFINITION_QUERY,
					showTypecast: true
				})
		}
	];

	return (
		<SafeResults {...result}>
			{({
				eventAttributeDefinition: {
					dataType,
					description,
					displayName,
					name,
					recentValues
				}
			}: {
				eventAttributeDefinition: Attribute;
			}) => (
				<BasePage
					breadcrumbItems={[
						getDefinitions({groupId}),
						getEventAttributes({groupId}),
						{active: true, label: name}
					]}
					groupId={groupId}
					pageActions={viewAttributePageActions}
					pageDescription={
						<>
							{description ? (
								<div>{description}</div>
							) : (
								<div className='no-description'>
									{Liferay.Language.get('no-description')}
								</div>
							)}

							<Label display='primary' uppercase>
								{dataType}
							</Label>
						</>
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
									className:
										'table-cell-expand-small text-truncate',
									label: Liferay.Language.get('sample-data'),
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
									label: Liferay.Language.get('last-seen'),
									sortable: false
								}
							]}
							items={recentValues}
							rowIdentifier='sampleData'
						/>
					)}
				</BasePage>
			)}
		</SafeResults>
	);
};

export default connect(null, {close, open})(AttributeView);
