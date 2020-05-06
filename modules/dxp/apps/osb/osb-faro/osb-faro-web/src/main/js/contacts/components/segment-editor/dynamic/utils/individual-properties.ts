import {Property} from 'shared/util/records';

const createIndividualProperty = ({
	label,
	name,
	type
}: {
	label: string;
	name: string;
	type: string;
}): Property =>
	new Property({
		entityName: Liferay.Language.get('individual'),
		label,
		name,
		propertyKey: 'individual',
		type
	});

const INDIVIDUAL_PROPERTIES = [
	{
		label: Liferay.Language.get('site-membership'),
		name: 'groupIds',
		type: 'select-text'
	},
	{
		label: Liferay.Language.get('role'),
		name: 'roleIds',
		type: 'select-text'
	},
	{
		label: Liferay.Language.get('team'),
		name: 'teamIds',
		type: 'select-text'
	},
	{
		label: Liferay.Language.get('user-group'),
		name: 'userGroupIds',
		type: 'select-text'
	},
	{
		label: Liferay.Language.get('dxp-user'),
		name: 'userId',
		type: 'select-text'
	}
].map(createIndividualProperty);

export default INDIVIDUAL_PROPERTIES;
