import Button from 'shared/components/Button';
import Constants from 'shared/util/constants';
import MetadataTag from './MetadataTag';
import Modal from 'shared/components/modal';
import React from 'react';
import RecommendationPageAssetsQuery from '../queries/RecommendationPageAssetsQuery';
import {EXCLUDE, Filter} from '../utils/utils';
import {getMapResultToProps} from 'shared/hoc/mappers/metrics';
import {graphql} from '@apollo/react-hoc';
import {withBaseResults, withStatefulPagination} from 'shared/hoc';

const {
	pagination: {orderDescending}
} = Constants;

const withData = () =>
	graphql(RecommendationPageAssetsQuery, {
		options: ({
			delta,
			itemFilters,
			orderBy,
			orderByField,
			page,
			query
		}: {
			delta: number;
			itemFilters: Filter[];
			orderBy: string;
			orderByField: string;
			page: number;
			query: string;
		}) => ({
			fetchPolicy: 'no-cache',
			variables: {
				keywords: query,
				propertyFilters: itemFilters.map(({name, value}) => ({
					filter: value,
					negate: name === EXCLUDE
				})),
				size: delta,
				sort: {
					column: orderByField,
					type: orderBy.toUpperCase()
				},
				start: (page - 1) * delta
			}
		}),
		props: getMapResultToProps(({pageAssets: {pageAssets, total}}) => ({
			items: pageAssets,
			total
		}))
	});

const TableWithData = withStatefulPagination(
	withBaseResults(withData, {
		getColumns: ({secondColumnHeader}) => [
			{
				accessor: 'title',
				className: 'table-cell-expand text-truncate',
				label: Liferay.Language.get('page-name')
			},
			{
				accessor: secondColumnHeader || 'url',
				className: 'secondary-info table-cell-expand text-truncate',
				label: secondColumnHeader || 'url',
				sortable: false
			}
		],
		showDropdownRangeKey: false
	}),
	{
		defaultDelta: 10,
		defaultOrderBy: orderDescending,
		defaultOrderByField: 'title'
	},
	({onOrderByFieldChange, onSearchValueChange, ...otherProps}) => ({
		onSearchSubmit: onSearchValueChange,
		onSortChange: onOrderByFieldChange,
		...otherProps
	}),
	false
);

interface IMatchingPagesModalProps {
	itemFilters: Filter[];
	onClose: () => void;
}

const MatchingPagesModal: React.FC<IMatchingPagesModalProps> = ({
	itemFilters,
	onClose
}) => {
	const {name, value} = itemFilters[0];

	const [rule, exactMatchSign, metadataTag] = value
		.split(/\s*([=~])\s*/, 3)
		.reverse();

	const customFilter = itemFilters.length === 1 && metadataTag;

	return (
		<Modal className='matching-pages-modal-root' size='xl'>
			<Modal.Header
				onClose={onClose}
				title={Liferay.Language.get('matching-pages')}
			/>

			<Modal.Body>
				{!!customFilter && (
					<div>
						<span className='include-exclude'>
							{`${
								name === EXCLUDE
									? Liferay.Language.get('exclude')
									: Liferay.Language.get('include')
							}:`}
						</span>

						<MetadataTag value={metadataTag} />

						<span className='rule'>
							{exactMatchSign ? `"${rule}"` : rule}
						</span>
					</div>
				)}
			</Modal.Body>

			<TableWithData
				itemFilters={itemFilters}
				noResultsProps={{spacer: true}}
				secondColumnHeader={customFilter}
			/>

			<Modal.Footer>
				<Button display='primary' onClick={onClose}>
					{Liferay.Language.get('done')}
				</Button>
			</Modal.Footer>
		</Modal>
	);
};

export default MatchingPagesModal;
