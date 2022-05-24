export default () => ({
	__typename: 'CommerceTotalOrderValue',
	currencies: {
		__typename: 'CommerceTotalOrderCurrencies',
		USD: {
			__typename: 'CommerceTotalOrderCurreny',
			trend: {
				__typename: 'CommerceTotalOrderTrend',
				percentage: 50,
				trendClassification: 'POSITIVE'
			},
			value: '$100,000.00'
		}
	}
});
