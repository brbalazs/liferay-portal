import {gql} from 'apollo-boost';

export default gql`
	query UsersList(
		$keywords: String
		$size: Int!
		$sort: Sort!
		$start: Int!
	) {
		users(keywords: $keywords, size: $size, sort: $sort, start: $start) {
			dxpEntities {
				id
				name
				... on User {
					dataSourceName
					screenName
				}
			}
			total
		}
	}
`;
