declare namespace BasePage {
	interface Context {
		filters: Object;
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
