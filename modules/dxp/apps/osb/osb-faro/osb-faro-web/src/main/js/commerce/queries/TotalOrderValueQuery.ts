import {Currencies} from 'commerce/utils/types';
import {gql} from 'apollo-boost';

export interface CommerceTotalOrderValueData {
	commerceTotalOrderValue: {
		currencies: Currencies;
	};
}

export default gql`
	query CommerceTotalOrderValue(
		$channelId: String!
		$rangeEnd: String
		$rangeKey: Int
		$rangeStart: String
	) {
		commerceTotalOrderValue(
			channelId: $channelId
			rangeEnd: $rangeEnd
			rangeKey: $rangeKey
			rangeStart: $rangeStart
		) @client {
			currencies
		}
	}
`;
