import Button from 'shared/components/Button';
import getCN from 'classnames';
import Icon from 'shared/components/Icon';
import MetadataTag from '../MetadataTag';
import React from 'react';
import Table from 'shared/components/table';
import {FieldArray} from 'formik';
import {RULE_NAME_LABEL_MAP} from '../../utils/utils';

const CountCell: React.FC<{
	className: string;
	data: {count: number; id: string; name: string; value: string};
}> = ({className, data: {count}}) => (
	// TODO: LRAC-6112 Fetch the count here only if count does not currently exist. Use replace to add the value to the fieldArray item. Will need to add loading indicator
	// TODO: LRAC-6113 Add button here to open model with matching metadata/pages

	<td className={className}>{count}</td>
);

const RuleCell: React.FC<{
	className: string;
	data: {name: string; value: string};
}> = ({className, data: {name, value}}) => {
	const [rule, metadataTag] = value.split(/\s*(?:[=~])\s*/, 2).reverse();

	return (
		<td className={getCN('rule', className)}>
			<b>{`${RULE_NAME_LABEL_MAP[name]}:`}</b>

			{metadataTag && <MetadataTag value={metadataTag} />}

			<span className='rule-value secondary-info'>{rule}</span>
		</td>
	);
};

interface IItemsProps {
	items: any[]; // TODO: rename items to filters
}

const Items: React.FC<IItemsProps> = ({items}) => {
	const totalPages = items.reduce((acc, {count}) => acc + count, 0);

	return (
		<div className='items-root'>
			<div className='title'>{Liferay.Language.get('add-items')}</div>

			<div className='secondary-info'>
				{Liferay.Language.get(
					'create-rules-to-match-your-urls-and-page-metadata.-if-you-dont-define-rules,-the-recommendation-model-will-use-all-urls-from-this-workspace-to-train-the-recommendations-model'
				)}
			</div>

			<FieldArray name='items'>
				{arrayHelpers => (
					<>
						<Button
							className='new-rule-button'
							onClick={() => {
								const newItem = {
									count: 16,
									id:
										'excludeFilter - og:url = .jp/blog/group=./.',
									name: 'excludeFilter',
									value: 'og:url = .jp/blog/group=./.'
								};

								if (
									!items.find(item => item.id === newItem.id)
								) {
									arrayHelpers.push(newItem);
								}

								// TODO: open modal
								// do not allow addition of duplicate values
								// Maybe add a toast alert to inform the user that this already exists therefore it was not added
							}}
						>
							{Liferay.Language.get('new-rule')}
						</Button>

						{!!items.length && (
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
									items={items}
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

									<div>{totalPages}</div>
								</div>
							</>
						)}
					</>
				)}
			</FieldArray>
		</div>
	);
};

export default Items;
