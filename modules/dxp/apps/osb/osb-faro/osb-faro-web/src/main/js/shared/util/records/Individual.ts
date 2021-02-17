import Constants from '../constants';
import {Map, Record} from 'immutable';

const {entityTypes} = Constants;

interface IIndividual {
	accountNames: string[] | null;
	activitiesCount: number;
	dateCreated: string;
	demographics: Map<string, any>;
	id: string;
	lastActivityDate: string;
	name: string;
	properties: Map<string, any>;
	type: number;
}

export default class Individual
	extends Record({
		accountNames: null,
		activitiesCount: 0,
		dateCreated: null,
		demographics: Map(),
		id: null,
		lastActivityDate: null,
		name: '',
		properties: Map(),
		type: entityTypes.individual
	})
	implements IIndividual {
	accountNames: string[] | null;
	activitiesCount: number;
	dateCreated: string;
	demographics: Map<string, any>;
	id: string;
	lastActivityDate: string;
	name: string;
	properties: Map<string, any>;
	type: number;

	constructor(props = {}) {
		super(props);
	}
}
