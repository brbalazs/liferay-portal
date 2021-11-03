import {OrderByDirections} from '../constants';
import {Record} from 'immutable';

interface IOrderParams {
	field: string;
	sortOrder: OrderByDirections;
}

export default class OrderParams
	extends Record({
		field: null,
		sortOrder: null
	})
	implements IOrderParams {
	field: string;
	sortOrder: OrderByDirections; // TODO: Need to make sure using this doesn't break everything

	constructor(props = {}) {
		super(props);
	}
}
