import {COMPOSITION_FRAGMENT} from 'shared/queries/fragments';
import {gql} from 'apollo-boost';

export default gql`
	query Interests(
		$channelId: String
		$keywords: String
		$size: Int!
		$sort: Sort!
		$start: Int!
	) {
		individualInterests(
			channelId: $channelId
			keywords: $keywords
			size: $size
			sort: $sort
			start: $start
		) {
			...compositionFragment
		}
	}

	${COMPOSITION_FRAGMENT}
`;
