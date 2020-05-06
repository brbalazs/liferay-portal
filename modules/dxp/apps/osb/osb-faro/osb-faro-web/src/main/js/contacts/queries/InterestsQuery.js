import FaroConstants from 'shared/util/constants';
import {COMPOSITION_FRAGMENT} from 'shared/queries/fragments';
import {gql} from 'apollo-boost';

const {
	compositionTypes: {accountInterests, segmentInterests}
} = FaroConstants;

const INTERESTS_ID_MAP = {
	[accountInterests]: 'accountId',
	[segmentInterests]: 'individualSegmentId'
};

export default queryName => gql`
	query Interests(
		$active: Boolean!
		$id: String!
		$keywords: String
		$size: Int!
		$sort: Sort!
		$start: Int!
	) {
		${queryName}(
			active: $active
			${INTERESTS_ID_MAP[queryName]}: $id
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
