import {
	ActivityActions,
	Applications,
	ChannelPermissionTypes,
	DataSourceDisplayStatuses,
	DataSourceProgressStatuses,
	DataSourceStatuses,
	DataSourceTypes,
	EntityTypes
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
			fieldContexts: {[key: string]: string};
			fieldTypes: {[key: string]: string};
			pathThemeImages: string;
			pagination: {
				cur: number;
				delta: number;
				deltaValues: number[];
				orderAscending: string;
				orderDefault: string;
				orderDescending: string;
			};
			preferencesScopes: {[key: string]: string};
			projectLocations: {[key: string]: string};
			segmentTypes: {[key: string]: string};
			timeIntervals: {[key: string]: string};
		};
		hbspt: {
			forms: {
				create: Function;
			};
		};
		jQuery: object;
	}
}
