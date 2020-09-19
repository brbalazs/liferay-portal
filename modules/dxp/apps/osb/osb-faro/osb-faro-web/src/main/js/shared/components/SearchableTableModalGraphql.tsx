import Button from 'shared/components/Button';
import CrossPageSelect from 'shared/hoc/CrossPageSelect';
import ListComponent from 'shared/hoc/ListComponent';
import Modal from 'shared/components/modal';
import React, {useEffect} from 'react';
import {
	ACTION_TYPES,
	useSelectionContext,
	withSelectionProvider
} from 'shared/context/selection';
import {Columns, IPagination} from 'shared/types';
import {DocumentNode} from 'graphql';
import {noop, omit, pickBy} from 'lodash';
import {OrderedMap} from 'immutable';
import {QueryHookOptions, useQuery} from '@apollo/react-hooks';
import {safeResultToProps} from 'shared/util/mappers';
import {withStatefulPagination} from 'shared/hoc';

interface ISearchableTableModalGraphQLProps extends IPagination {
	className: string;
	columns: Columns;
	graphqlQuery: DocumentNode;
	instruction?: string;
	mapPropsToOptions: (props: {[key: string]: any}) => QueryHookOptions;
	mapResultToProps: (result: {
		[key: string]: any;
	}) => {items: any[]; total: number};
	onClose?: () => void;
	onSubmit: (selectedItems: OrderedMap<string, any>) => void;
	requireSelection?: boolean;
	selectedItems?: any[];
	submitMessage?: string;
	title?: string;
}

const SearchableTableModalGraphql: React.FC<
	ISearchableTableModalGraphQLProps
> = ({
	className,
	columns,
	delta = 10,
	graphqlQuery,
	instruction = '',
	mapPropsToOptions,
	mapResultToProps,
	onClose = noop,
	orderBy,
	orderByField,
	onSubmit,
	requireSelection = true,
	selectedItems = [],
	submitMessage = Liferay.Language.get('submit'),
	title = Liferay.Language.get('select-items'),
	...otherProps
}) => {
	const {data, error, loading} = useQuery(
		graphqlQuery,
		mapPropsToOptions({delta, orderBy, orderByField, ...otherProps})
	);

	const {
		selectedItems: contextSelectedItems,
		selectionDispatch
	} = useSelectionContext();

	useEffect(() => {
		if (selectedItems.length) {
			selectionDispatch({
				payload: {items: selectedItems},
				type: ACTION_TYPES.add
			});
		}
	}, []);

	const handleSubmit = () => onSubmit(contextSelectedItems);

	const {empty, items, total} = safeResultToProps(mapResultToProps)({
		data: {error, loading, ...data},
		ownProps: {}
	});

	return (
		<Modal className={className} size='lg'>
			<Modal.Header onClose={onClose} title={title} />

			<Modal.Body>
				<div className='text-secondary'>{instruction}</div>
			</Modal.Body>

			<CrossPageSelect
				autoFocus
				columns={columns}
				defaultDelta={delta}
				defaultOrderBy={orderBy}
				defaultOrderByField={orderByField}
				defaultSort={{field: orderByField, sortOrder: orderBy}}
				delta={delta}
				empty={empty}
				items={items}
				loading={loading}
				orderBy={orderBy}
				orderByField={orderByField}
				pageDisplay={false}
				total={total}
				{...otherProps}
			>
				{props => <ListComponent {...props} />}
			</CrossPageSelect>

			<Modal.Footer>
				<Button onClick={onClose}>
					{Liferay.Language.get('cancel')}
				</Button>

				<Button
					disabled={requireSelection && !contextSelectedItems.size}
					display='primary'
					onClick={handleSubmit}
				>
					{submitMessage}
				</Button>
			</Modal.Footer>
		</Modal>
	);
};

const WrappedComponent = withStatefulPagination(
	SearchableTableModalGraphql,
	({
		delta: defaultDelta,
		orderBy: defaultOrderBy,
		orderByField: defaultOrderByField
	}) =>
		pickBy({
			defaultDelta,
			defaultOrderBy,
			defaultOrderByField
		}),
	({onOrderByFieldsChange, ...otherStatefulProps}) => ({
		onSortChange: onOrderByFieldsChange,
		...omit(otherStatefulProps, 'onSearchValueChange')
	}),
	false
);

export default withSelectionProvider(WrappedComponent);
