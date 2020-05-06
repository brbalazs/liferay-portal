import {gql} from 'apollo-boost';

export default gql`
	query SiteMetrics($channelId: String, $rangeKey: Int!, $timezone: String!) {
		siteVisitorHeatMap(
			channelId: $channelId
			rangeKey: $rangeKey
			timeZoneId: $timezone
		) {
			column: colDimension
			row: rowDimension
			value
		}
	}
`;
