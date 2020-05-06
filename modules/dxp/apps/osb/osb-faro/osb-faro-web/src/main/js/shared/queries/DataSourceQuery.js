import {gql} from 'apollo-boost';

export default gql`
	query DataSource(
		$credentialsType: String
		$size: Int
		$sort: Sort
		$type: String
	) {
		dataSources(
			credentialsType: $credentialsType
			size: $size
			sort: $sort
			type: $type
		) {
			id
			name
			url
		}
	}
`;
