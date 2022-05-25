export type Currencies = {
	[key: string]: {
		value: string;
		trend: {
			trendClassification: 'POSITIVE' | 'NEGATIVE' | 'NEUTRAL';
			percentage: number;
		};
	};
};
