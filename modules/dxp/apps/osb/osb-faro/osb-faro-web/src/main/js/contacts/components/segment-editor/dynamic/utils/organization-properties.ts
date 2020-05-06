import {List} from 'immutable';
import {Property} from 'shared/util/records';

const createOrganizationProperty = ({
	label,
	name,
	type
}: {
	label: string;
	name: string;
	type: string;
}): Property =>
	new Property({
		entityName: Liferay.Language.get('organization'),
		label,
		name,
		propertyKey: 'organization',
		type: `organization-${type}`
	});

const ORGANIZATION_PROPERTIES = List(
	[
		{
			label: Liferay.Language.get('date-modified'),
			name: 'modifiedDate',
			type: 'date-time'
		},
		{
			label: Liferay.Language.get('name'),
			name: 'name',
			type: 'text'
		},
		{
			label: Liferay.Language.get('hierarchy-path'),
			name: 'hierarchyPath',
			type: 'text'
		},
		{
			label: Liferay.Language.get('organization'),
			name: 'id',
			type: 'select-text'
		},
		{
			label: Liferay.Language.get('parent-organization'),
			name: 'parentId',
			type: 'select-text'
		},
		{
			label: Liferay.Language.get('type'),
			name: 'type',
			type: 'text'
		}
	].map(createOrganizationProperty)
);

export default ORGANIZATION_PROPERTIES;
