import {
	ActivityActions,
	Applications,
	ChannelPermissionTypes,
	EntityTypes
} from 'shared/util/constants';
export {};

declare global {
	interface Window {
		faroConstants: {
			activityActions: {[key: string]: ActivityActions};
			applications: {[key: string]: Applications};
			channelPermissionTypes: {[key: string]: ChannelPermissionTypes};
			dataSourceStatuses: {[key: string]: string};
			dataSourceTypes: {[key: string]: string};
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
