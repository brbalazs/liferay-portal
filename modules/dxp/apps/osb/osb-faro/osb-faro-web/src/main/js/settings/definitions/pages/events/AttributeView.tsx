import BasePage from 'settings/components/BasePage';
import Label from 'shared/components/Label';
import React from 'react';
import Table from 'shared/components/table';
import {DateCell} from 'shared/components/table/cell-components';
import {getDefinitions, getEvents} from 'shared/util/breadcrumbs';
import {HasModal} from 'shared/types';
import {Routes, setUriQueryValues, toRoute} from 'shared/util/router';
import {useParams} from 'react-router-dom';

// TODO: LRAC-7479 Use the graphql query instead of mocked data
const MOCKED_ITEMS = [
	{lastSeen: new Date('2011-10-10T14:48:00'), sampleData: 'IBM'},
	{lastSeen: new Date('2011-10-10T14:48:00'), sampleData: 'Facebook'},
	{lastSeen: new Date('2011-10-10T14:48:00'), sampleData: 'ABC'}
];

const TableWithData = () => (
	<Table
		columns={[
			{
				accessor: 'sampleData',
				label: Liferay.Language.get('sample-data'),
				sortable: false
			},
			{
				accessor: 'lastSeen',
				cellRenderer: ({data}) => (
					<DateCell
						className='table-column-text-end'
						data={data}
						datePath='lastSeen'
					/>
				),
				className: 'table-column-text-end',
				label: Liferay.Language.get('last-seen'),
				sortable: false
			}
		]}
		items={MOCKED_ITEMS}
		rowIdentifier='sampleData'
	/>
);
interface IAttributeViewProps
	extends React.HTMLAttributes<HTMLElement>,
		HasModal {
	groupId: string;
}

const AttributeView: React.FC<IAttributeViewProps> = ({groupId}) => {
	const {attributeId} = useParams();
	// TODO: LRAC-7479 Use the graphql query instead of mocked data
	const attribute = {
		dataType: 'string',
		description: 'somedescription',
		displayName: 'organization',
		id: 'myid',
		name: 'company'
	};

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
		<BasePage
			breadcrumbItems={[
				getDefinitions({groupId}),
				getEvents({groupId}),
				{active: true, label: attribute.name}
			]}
			groupId={groupId}
			pageActions={viewAttributePageActions}
			pageDescription={
				attribute.description ? (
					<>
						<div>{attribute.description}</div>

						<Label display='primary' uppercase>
							{attribute.dataType}
						</Label>
					</>
				) : (
					<div className='no-description'>
						{Liferay.Language.get('no-description')}
					</div>
				)
			}
			pageTitle={attribute.name}
			subTitle={attribute.displayName}
		>
			<TableWithData />
		</BasePage>
	);
};

export default AttributeView;
