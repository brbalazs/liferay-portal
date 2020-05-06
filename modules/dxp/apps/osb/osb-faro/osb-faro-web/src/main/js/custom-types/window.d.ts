export {};

declare global {
	interface Window {
		faroConstants: {
			dataSourceStatuses: {[key: string]: string};
			dataSourceTypes: {[key: string]: string};
			entityTypes: {[key: string]: number};
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
