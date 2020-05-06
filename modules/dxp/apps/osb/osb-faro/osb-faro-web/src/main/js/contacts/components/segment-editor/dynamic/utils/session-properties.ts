import {List} from 'immutable';
import {Property} from 'shared/util/records';

export const DEVICE_OPTIONS = [
	{label: Liferay.Language.get('desktop'), value: 'Desktop'},
	{
		label: Liferay.Language.get('smartphone'),
		value: 'Smartphone'
	},
	{label: Liferay.Language.get('tablet'), value: 'Tablet'}
];

const createSessionProperty = ({
	label,
	name,
	type
}: {
	label: string;
	name: string;
	type: string;
}) =>
	new Property({
		entityName: Liferay.Language.get('session'),
		label,
		name,
		propertyKey: 'session',
		type: `session-${type}`
	});

const SESSION_PROPERTIES = List(
	[
		{
			label: Liferay.Language.get('browser'),
			name: 'context/browserName',
			type: 'text'
		},
		{
			label: Liferay.Language.get('device'),
			name: 'context/deviceType',
			options: DEVICE_OPTIONS,
			type: 'text'
		},
		{
			label: Liferay.Language.get('geolocation'),
			name: 'context/country',
			type: 'geolocation'
		},
		{
			label: Liferay.Language.get('referrer'),
			name: 'context/referrer',
			type: 'text'
		},
		{
			label: Liferay.Language.get('date-&-time'),
			name: 'completeDate',
			type: 'date-time'
		},
		{
			label: Liferay.Language.get('url'),
			name: 'context/url',
			type: 'text'
		}
	].map(createSessionProperty)
);

export default SESSION_PROPERTIES;
