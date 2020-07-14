import Constants from '../constants';
import {Map, Record} from 'immutable';

const {entityTypes} = Constants;

interface IAccount {
	activitiesCount: number;
	createTime?: string;
	engagementScore?: number;
	id?: string;
	individualCount: number;
	name: string;
	photoURL: string;
	properties: Map<string, any>;
	status?: string;
	type: string;
}

export default class Account
	extends Record({
		activitiesCount: 0,
		createTime: null,
		engagementScore: null,
		id: null,
		individualCount: 0,
		name: '',
		photoURL: '',
		properties: Map(),
		status: null,
		type: entityTypes.account
	})
	implements IAccount {
	activitiesCount: number;
	createTime?: string;
	engagementScore?: number;
	id?: string;
	individualCount: number;
	name: string;
	photoURL: string;
	properties: Map<string, any>;
	status?: string;
	type: string;

	constructor(props = {}) {
		super(props);
	}
}
