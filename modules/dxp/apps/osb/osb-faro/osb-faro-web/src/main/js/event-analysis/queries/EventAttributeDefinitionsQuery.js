import {gql} from 'apollo-boost';

export default gql`
	query EventAttributeDefinitions(
		$keyword: String
		$page: Int!
		$size: Int!
	) {
		eventAttributeDefinitions(keyword: $keyword, page: $page, size: $size) {
			eventAttributeDefinitions {
				dataType
				description
				displayName
				id
				name
			}
			total
		}
	}
`;
