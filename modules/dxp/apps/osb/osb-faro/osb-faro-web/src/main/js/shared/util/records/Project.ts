import {Map, Record} from 'immutable';

interface IProject {
	accountKey?: string;
	accountName?: string;
	corpProjectName?: string;
	corpProjectUuid?: string;
	faroSubscription?: Map<string, any>;
	friendlyURL?: string;
	groupId?: number;
	name?: string;
	serverLocation: string;
	state?: string;
	userId?: number;
}

export default class Project
	extends Record({
		accountKey: null,
		accountName: '',
		corpProjectName: '',
		corpProjectUuid: null,
		faroSubscription: Map(),
		friendlyURL: null,
		groupId: null,
		name: '',
		serverLocation: null,
		state: null,
		userId: null
	})
	implements IProject {
	accountKey?: string;
	accountName?: string;
	corpProjectName?: string;
	corpProjectUuid?: string;
	faroSubscription?: Map<string, any>;
	friendlyURL?: string;
	groupId?: number;
	name?: string;
	serverLocation: string;
	state?: string;
	userId?: number;

	constructor(props: IProject) {
		super(props);
	}
}
