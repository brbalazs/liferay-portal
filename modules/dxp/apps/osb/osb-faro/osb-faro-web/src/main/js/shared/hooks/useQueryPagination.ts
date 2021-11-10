import useQueryParams from './useQueryParams';
import {createOrderIOMap, paginationDefaults} from 'shared/util/pagination';
import {OrderedMap} from 'immutable';
import {OrderParams} from 'shared/util/records';

const {
	delta: defaultDelta,
	page: defaultPage,
	query: defaultQuery
} = paginationDefaults;

type QueryPaginationParams = {
	initialDelta?: number;
	initialOrderIOMap?: OrderedMap<string, OrderParams>;
	initialPage?: number;
	initialQuery?: string;
};

// TODO: Need to set the defaults for this.
const useQueryPagination = ({
	initialDelta = defaultDelta,
	initialOrderIOMap, // TODO: should there be an initial here too with no values?
	initialPage = defaultPage,
	initialQuery = defaultQuery
}: QueryPaginationParams) => {
	const {
		delta = initialDelta,
		field,
		page = initialPage,
		query = initialQuery,
		sortOrder
	} = useQueryParams();

	// TODO: rangeKey should prob be in here too? or a separate hook? prob separate?

	let orderIOMap = initialOrderIOMap;

	if (field && sortOrder) {
		orderIOMap = createOrderIOMap(field, sortOrder);
	}

	return {
		delta: parseInt(delta as string),
		orderIOMap,
		page: parseInt(page as string),
		query
	};
};

export default useQueryPagination;
