import Button from 'shared/components/Button';
import Constants from 'shared/util/constants';
import getCN from 'classnames';
import Icon from 'shared/components/Icon';
import MetadataTag from '../MetadataTag';
import React, {useEffect} from 'react';
import RecommendationPageAssetsQuery from '../../queries/RecommendationPageAssetsQuery';
import Spinner from 'shared/components/Spinner';
import Table from 'shared/components/table';
import {close, modalTypes, open} from 'shared/actions/modals';
import {connect} from 'react-redux';
import {EXCLUDE, Filter, RULE_NAME_LABEL_MAP} from '../../utils/utils';
import {FieldArray} from 'formik';
import {get} from 'lodash';
import {Modal} from 'shared/types';
import {useLazyQuery, useQuery} from '@apollo/react-hooks';

const {
	pagination: {orderDescending}
} = Constants;

const CountCell: React.FC<{
	className: string;
	data: Filter;
}> = ({className, data: {name, value}}) => {
	const {data, loading} = useQuery(RecommendationPageAssetsQuery, {
		variables: {
			propertyFilters: [
				{
					filter: value,
					negate: name === EXCLUDE
				}
			],
			size: 0,
			sort: {
				column: 'title',
				type: orderDescending.toUpperCase()
			},
			start: 0
		}
	});

	if (loading) {
		return (
			<td className={className}>
				<Spinner size='sm' />
			</td>
		);
	}

	return (
		<td className={className}>
			{get(data, ['pageAssets', 'total'], 0).toLocaleString()}
		</td>
	);
};

const RuleCell: React.FC<{
	className: string;
	data: Filter;
}> = ({className, data: {name, value}}) => {
	const [rule, exactMatchSign, metadataTag] = value
		.split(/\s*([=~])\s*/, 3)
		.reverse();

	const exactMatch = exactMatchSign === '=';

	return (
		<td className={getCN('rule', className)}>
			<b>{`${RULE_NAME_LABEL_MAP[name]}:`}</b>

			{metadataTag && <MetadataTag value={metadataTag} />}

			<span className='rule-value secondary-info'>
				{exactMatch ? `"${rule}"` : rule}
			</span>
		</td>
	);
};

interface IItemsProps {
	close: Modal.close;
	groupId: string;
	itemFilters: Filter[];
	open: Modal.open;
}

const Items: React.FC<IItemsProps> = ({close, groupId, itemFilters, open}) => {
	const [
		getPageAssetsTotal,
		{data, loading: pagesTotalLoading}
	] = useLazyQuery(RecommendationPageAssetsQuery);

	useEffect(() => {
		getPageAssetsTotal({
			variables: {
				propertyFilters: itemFilters.map(({name, value}) => ({
					filter: value,
					negate: name === EXCLUDE
				})),
				size: 0,
				sort: {
					column: 'title',
					type: orderDescending.toUpperCase()
				},
				start: 0
			}
		});
	}, [itemFilters]);

	const renderTotalPages = (): React.ReactNode => {
		if (pagesTotalLoading) {
			return (
				<div>
					<Spinner size='sm' />
				</div>
			);
		}

		return (
			<div>{get(data, ['pageAssets', 'total'], 0).toLocaleString()}</div>
		);
	};

	return (
		<div className='items-root'>
			<div className='title'>{Liferay.Language.get('add-items')}</div>

			<div className='secondary-info'>
				{Liferay.Language.get(
					'create-rules-to-match-your-urls-and-page-metadata.-if-you-dont-define-rules,-the-recommendation-model-will-use-all-urls-from-this-workspace-to-train-the-recommendations-model'
				)}
			</div>

			<FieldArray name='itemFilters'>
				{arrayHelpers => (
					<>
						<Button
							className='new-rule-button'
							onClick={() => {
								// Maybe add a toast alert to inform the user that this already exists therefore it was not added

								open(modalTypes.NEW_RULE_MODAL, {
									groupId,
									onClose: close,
									onSubmit: filter => {
										if (
											!itemFilters.find(
												item => item.id === filter.id
											)
										) {
											arrayHelpers.push(filter);
										}

										close();
									}
								});
							}}
						>
							{Liferay.Language.get('new-rule')}
						</Button>

						{!!itemFilters.length && (
							<>
								<Table
									columns={[
										{
											accessor: 'name',
											cellRenderer: RuleCell,
											className: 'table-cell-expand',
											label: Liferay.Language.get('rule'),
											sortable: false
										},
										{
											accessor: 'value',
											cellRenderer: CountCell,
											className: 'table-column-text-end',
											label: Liferay.Language.get(
												'matching-items'
											),
											sortable: false
										}
									]}
									items={itemFilters}
									renderInlineRowActions={({data, items}) => (
										<span>
											<Button
												borderless
												display='secondary'
												onClick={() => {
													arrayHelpers.remove(
														items.findIndex(
															itemData =>
																itemData.id ===
																data.id
														)
													);
												}}
												outline
											>
												<Icon symbol='times' />
											</Button>
										</span>
									)}
									rowIdentifier={['name', 'value']}
								/>

								<div className='total-included-pages d-flex justify-content-between'>
									<div>
										{Liferay.Language.get(
											'total-included-pages'
										)}
									</div>

									{renderTotalPages()}
								</div>
							</>
						)}
					</>
				)}
			</FieldArray>
		</div>
	);
};

export default connect(
	null,
	{close, open}
)(Items);
