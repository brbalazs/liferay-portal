import BasePage from 'settings/components/BasePage';
import Label from 'shared/components/Label';
import React from 'react';
import {getDefinitions, getEvents} from 'shared/util/breadcrumbs';
import {HasModal} from 'shared/types';
import {Routes, setUriQueryValues, toRoute} from 'shared/util/router';
import {useParams} from 'react-router-dom';

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
			<div>{`CARD GOES HERE ${attributeId}`}</div>
		</BasePage>
	);
};

export default AttributeView;
