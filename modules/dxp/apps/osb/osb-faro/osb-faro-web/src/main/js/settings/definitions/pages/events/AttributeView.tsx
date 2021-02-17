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

	const attribute = {
		dataTyPe: 'string',
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
				{active: true, label: attribute.displayName}
			]}
			groupId={groupId}
			pageActions={viewAttributePageActions}
			pageDescription={
				<>
					<div>{attribute.description}</div>

					<Label display='primary' uppercase>
						{attribute.dataTyPe}
					</Label>
				</>
			}
			pageTitle={attribute.name}
			subTitle={attribute.displayName}
		>
			<div>{`CARD GOES HERE ${attributeId}`}</div>
		</BasePage>
	);
};

export default AttributeView;
