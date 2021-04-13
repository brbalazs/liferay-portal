import {
	ActivityActions,
	Applications,
	ChannelPermissionTypes,
	DataSourceDisplayStatuses,
	DataSourceProgressStatuses,
	DataSourceStatuses,
	DataSourceTypes,
	EntityTypes,
	FieldContexts,
	FieldOwnerTypes,
	FieldTypes,
	PreferencesScopes,
	ProjectStates
} from 'shared/util/constants';
export {};

declare global {
	interface Window {
		faroConstants: {
			activityActions: {[key: string]: ActivityActions};
			applications: {[key: string]: Applications};
			channelPermissionTypes: {[key: string]: ChannelPermissionTypes};
			dataSourceDisplayStatuses: {
				[key: string]: DataSourceDisplayStatuses;
			};
			dataSourceProgressStatuses: {
				[key: string]: DataSourceProgressStatuses;
			};
			dataSourceStatuses: {[key: string]: DataSourceStatuses};
			dataSourceTypes: {[key: string]: DataSourceTypes};
			entityTypes: {[key: string]: EntityTypes};
			faroURL: string;
			fieldContexts: {[key: string]: FieldContexts};
			fieldOwnerTypes: {[key: string]: FieldOwnerTypes};
			fieldTypes: {[key: string]: FieldTypes};
			locale: string;
			pagination: {
				cur: number;
				delta: number;
				deltaValues: number[];
				orderAscending: string;
				orderDefault: string;
				orderDescending: string;
			};
			pathThemeImages: string;
			portletNamespace: string;
			preferencesScopes: {[key: string]: PreferencesScopes};
			projectLocations: {[key: string]: string};
			projectStates: {[key: string]: ProjectStates};
			segmentTypes: {[key: string]: string};
			timeIntervals: {[key: string]: string};
			userName: string;
		};
		hbspt: {
			forms: {
				create: Function;
			};
		};
		jQuery: object;
	}
}
