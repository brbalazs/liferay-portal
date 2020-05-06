import FaroConstants from 'shared/util/constants';
import {get} from 'lodash';

export {getMapResultToProps} from 'sites/hocs/mappers/composition-query';

const {
	pagination: {cur: defaultPage, delta: defaultDelta, orderDescending}
} = FaroConstants;

const mapPropsToOptions: object = ({
	defaultSort: {field, sortOrder},
	router: {params, query}
}) => {
	const delta = parseInt(get(query, 'delta', defaultDelta));
	const page = parseInt(get(query, 'page', defaultPage));

	return {
		variables: {
			active: true,
			channelId: get(params, 'channelId'),
			id: get(params, 'id'),
			keywords: get(query, 'query', ''),
			size: delta,
			sort: {
				column: get(query, 'orderByField', field),
				type: get(query, 'orderBy', sortOrder).toUpperCase()
			},
			start: (page - 1) * delta
		}
	};
};

const mapCardPropsToOptions: object = ({channelId, id}) => ({
	variables: {
		active: true,
		channelId,
		id,
		size: 5,
		sort: {
			column: 'count',
			type: orderDescending.toUpperCase()
		},
		start: 0
	}
});

export {mapCardPropsToOptions, mapPropsToOptions};
