import {COMPOSITION_FRAGMENT} from 'shared/queries/fragments';
import {gql} from 'apollo-boost';

export default gql`
	query SearchTerms(
		$channelId: String
		$rangeKey: Int!
		$size: Int!
		$start: Int!
	) {
		searchTerms(
			channelId: $channelId
			rangeKey: $rangeKey
			size: $size
			start: $start
		) {
			...compositionFragment
		}
	}

	${COMPOSITION_FRAGMENT}
`;
