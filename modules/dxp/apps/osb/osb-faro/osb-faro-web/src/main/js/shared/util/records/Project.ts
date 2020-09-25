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
	recommendationsEnabled?: boolean;
	serverLocation: string;
	state?: string;
	stateStartDate?: number;
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
		recommendationsEnabled: false,
		serverLocation: null,
		state: null,
		stateStartDate: null,
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
	recommendationsEnabled?: boolean;
	serverLocation: string;
	state?: string;
	stateStartDate?: number;
	userId?: number;

	constructor(props: IProject) {
		super(props);
	}
}
