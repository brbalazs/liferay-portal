export type Router = {
	params: object;
	query: object;
};

export type Context = {
	filters: object;
	rangeKey: string;
	router: Router;
};
