import {Record} from 'immutable';

interface IProperty {
	entityName: string;
	entityType?: string;
	id?: string;
	label: string;
	name: string;
	options?: {label: string; value: string}[];
	propertyKey: string;
	type: string;
}

export default class Property
	extends Record({
		entityName: '',
		entityType: '',
		id: null,
		label: '',
		name: '',
		options: [],
		propertyKey: '',
		type: ''
	})
	implements IProperty {
	entityName: string;
	entityType: string;
	id: string;
	label: string;
	name: string;
	options?: {label: string; value: string}[];
	propertyKey: string;
	type: string;

	constructor(props: IProperty) {
		super(props);
	}
}
