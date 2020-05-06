import {Map, Record} from 'immutable';

interface IDataSource {
	credentials?: Map<string, any>;
	dateCreated?: number;
	disabled?: boolean;
	event?: string;
	fileName?: string;
	id?: string;
	lastSyncDate?: number;
	name?: string;
	properties?: Map<string, any>;
	provider?: Map<string, any>;
	providerType?: string;
	state?: string;
	status?: string;
	type?: number;
	url?: string;
}

export default class DataSource
	extends Record({
		contactsSelected: false,
		credentials: Map(),
		dateCreated: 0,
		disabled: false,
		event: null,
		fileName: null,
		id: null,
		lastSyncDate: null,
		name: '',
		properties: null,
		provider: null,
		providerType: '',
		sitesSelected: false,
		state: null,
		status: null,
		type: 1,
		url: null
	})
	implements IDataSource {
	contactsSelected: boolean;
	credentials?: Map<string, any>;
	dateCreated?: number;
	disabled?: boolean;
	event?: string;
	fileName?: string;
	id?: string;
	lastSyncDate?: number;
	name?: string;
	properties?: Map<string, any>;
	provider?: Map<string, any>;
	providerType?: string;
	sitesSelected: boolean;
	state?: string;
	status?: string;
	type?: number;
	url?: string;

	constructor(props: IDataSource) {
		super(props);
	}
}
