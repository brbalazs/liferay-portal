import {gql} from 'apollo-boost';

export default gql`
	query RecommendationsList(
		$keywords: String
		$size: Int!
		$sort: Sort!
		$start: Int!
	) {
		jobs(keywords: $keywords, size: $size, sort: $sort, start: $start) {
			jobs {
				active
				id
				name
				trainingFrequency
				trainingPeriod
				type
			}
			total
		}
	}
`;
