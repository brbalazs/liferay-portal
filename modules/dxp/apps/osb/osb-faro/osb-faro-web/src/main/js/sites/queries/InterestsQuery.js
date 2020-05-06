import {COMPOSITION_FRAGMENT} from 'shared/queries/fragments';
import {gql} from 'apollo-boost';

export default gql`
	query Interests($rangeKey: Int!, $size: Int!, $start: Int!) {
		siteInterests(rangeKey: $rangeKey, size: $size, start: $start) {
			...compositionFragment
		}
	}

	${COMPOSITION_FRAGMENT}
`;
