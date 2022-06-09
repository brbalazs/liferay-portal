export default () => [
	{
		__typename: 'orderTotalValues',
		currencyCode: 'EUR',
		trend: {
			__typename: 'orderTotalValuesTrend',
			percentage: 100.0,
			trendClassification: 'POSITIVE'
		},
		value: '20.0'
	},
	{
		__typename: 'orderTotalValues',
		currencyCode: 'USD',
		trend: {
			__typename: 'orderTotalValuesTrend',
			percentage: 20.0,
			trendClassification: 'POSITIVE'
		},
		value: '50.0'
	}
];
