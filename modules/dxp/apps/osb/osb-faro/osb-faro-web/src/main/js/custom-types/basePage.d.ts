declare namespace BasePage {
	interface Context {
		filters: Object;
		rangeKey: {
			defaultValue: string;
			lastValue: string;
		};
		router: {
			params: {
				channelId?: string;
				groupId?: string;
				id?: string;
			};
			query: Object;
		};
	}
}
