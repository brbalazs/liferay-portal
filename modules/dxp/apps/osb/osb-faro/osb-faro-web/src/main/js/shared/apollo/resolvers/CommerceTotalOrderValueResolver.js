export default () => [
	{
		__typename: 'orderTotalValue',
		currencyCode: 'EUR',
		trend: {
			__typename: 'orderTotalValueTrend',
			percentage: 100.0,
			trendClassification: 'POSITIVE'
		},
		value: '20.0'
	},
	{
		__typename: 'orderTotalValue',
		currencyCode: 'USD',
		trend: {
			__typename: 'orderTotalValueTrend',
			percentage: 20.0,
			trendClassification: 'POSITIVE'
		},
		value: '50.0'
	}
];
