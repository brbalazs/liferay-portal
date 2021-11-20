import React from 'react';
import {useQueryPagination} from 'shared/hooks';

// TODO: Maybe call this with Query pagination?
const withQueryPagination = initialParams => WrappedComponent => (
	props: any
) => {
	const paginationParams = useQueryPagination(initialParams);

	return <WrappedComponent {...props} {...paginationParams} />;
};

export default withQueryPagination;
