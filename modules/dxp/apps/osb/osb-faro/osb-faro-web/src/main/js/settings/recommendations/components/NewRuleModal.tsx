import Button from 'shared/components/Button';
import Checkbox from 'shared/components/Checkbox';
import Constants from 'shared/util/constants';
import InfoPopover from 'shared/components/InfoPopover';
import Modal from 'shared/components/modal';
import RadioGroup from 'shared/components/RadioGroup';
import React, {useEffect, useState} from 'react';
import RecommendationPageAssetsQuery from '../queries/RecommendationPageAssetsQuery';
import StringMatchInput from './StringMatchInput';
import Table from 'shared/components/table';
import {compose} from 'redux';
import {EXCLUDE, INCLUDE} from '../utils/utils';
import {useLazyQuery} from '@apollo/react-hooks';
import {withEmpty, withPaginationBar, withStatefulPagination} from 'shared/hoc';

const {
	pagination: {orderDescending}
} = Constants;

interface INewRuleModalProps {
	delta: number;
	groupId: string;
	onClose: () => void;
	onOrderByFieldChange: (orderParams: {
		orderBy: string;
		orderByField: string;
	}) => void;
	onSubmit: (value: {id: string; name: string; value: string}) => void;
	orderBy: string;
	orderByField: string;
	page: number;
	paginationProps: {
		onDeltaChange: (delta: number) => void;
		onPageChange: (page: number) => void;
	};
	query: string;
}

const NewRuleModal: React.FC<INewRuleModalProps> = ({
	delta,
	onClose,
	onOrderByFieldChange,
	onSubmit,
	orderBy,
	orderByField,
	page,
	paginationProps
}) => {
	const [focusOnInit, setFocusOnInit] = useState(false);
	const [metadata, setMetadata] = useState('');
	const [stringMatch, setStringMatch] = useState('');
	const [exactMatch, setExactMatch] = useState(false);
	const [includeExclude, setIncludeExclude] = useState(INCLUDE);

	useEffect(() => {
		setFocusOnInit(true);
	}, []);

	const [getPageAssets, {data, loading}] = useLazyQuery(
		RecommendationPageAssetsQuery,
		{
			fetchPolicy: 'network-only'
		}
	);

	const filter: string = `${metadata} ${
		exactMatch ? '=' : '~'
	} ${stringMatch}`;

	const fetchPageAssets = (): void => {
		getPageAssets({
			variables: {
				propertyFilters: [
					{
						filter,
						negate: false
					}
				],
				size: delta,
				sort: {
					column: orderByField,
					type: orderBy.toUpperCase()
				},
				start: (page - 1) * delta
			}
		});
	};

	useEffect(() => {
		fetchPageAssets();
	}, [delta, orderBy, orderByField, page]);

	const TableWithPagination = compose<any>(
		withEmpty({spacer: true}),
		withPaginationBar()
	)(Table);

	return (
		<Modal className='new-rule-modal-root' size='lg'>
			<Modal.Header
				onClose={onClose}
				title={Liferay.Language.get('new-rule')}
			/>

			<Modal.Body>
				<div>
					<RadioGroup
						checked={includeExclude}
						inline
						name='includeExclude'
						onChange={setIncludeExclude}
					>
						<RadioGroup.Option
							key={INCLUDE}
							label={Liferay.Language.get('include')}
							value={INCLUDE}
						/>

						<RadioGroup.Option
							key={EXCLUDE}
							label={Liferay.Language.get('exclude')}
							value={EXCLUDE}
						/>
					</RadioGroup>
				</div>

				<div>
					<div className='strings-matching-input-container'>
						<span className='strings-matching-title'>
							{Liferay.Language.get('string-match')}
						</span>

						<span>{`(${Liferay.Language.get('regex-only')})`}</span>

						<div className='d-flex'>
							<StringMatchInput
								className='flex-grow-1'
								focusOnInit={focusOnInit}
								metadata={metadata}
								onEnterClick={fetchPageAssets}
								onMetadataChange={setMetadata}
								onStringMatchChange={setStringMatch}
								stringMatch={stringMatch}
							/>

							<div className='find-matches-button-container d-flex flex-column justify-content-center'>
								<Button
									disabled={!stringMatch || !metadata}
									onClick={fetchPageAssets}
								>
									{Liferay.Language.get('find-matches')}
								</Button>
							</div>
						</div>
					</div>

					<div className='exact-match'>
						<Checkbox
							checked={exactMatch}
							displayInline
							label={Liferay.Language.get('exact-match')}
							name='exactMatch'
							onChange={event =>
								setExactMatch(event.target.checked)
							}
						/>

						<InfoPopover
							className='exact-match-help-icon'
							content={Liferay.Language.get(
								'use-exact-match-to-add-specific-urls'
							)}
						/>
					</div>
				</div>

				{(loading || data) && (
					<div className='results'>
						<div className='title'>
							{Liferay.Language.get('matched-items')}
						</div>

						<div className='secondary-info'>
							{Liferay.Language.get(
								'item-sets-can-vary-per-period-depending-on-interactions.-metadata-matches-pages-with-at-least-one-view-event'
							)}
						</div>

						<TableWithPagination
							columns={[
								{
									accessor: 'title',
									className:
										'table-cell-expand text-truncate',
									label: Liferay.Language.get('page-name')
								},
								{
									accessor: metadata ? metadata : 'url',
									className:
										'table-cell-expand text-truncate',
									label: metadata ? metadata : 'url',
									sortable: false
								}
							]}
							defaultSort={{
								field: 'title',
								sortOrder: orderDescending
							}}
							delta={delta}
							items={data ? data.pageAssets.pageAssets : []}
							loading={loading}
							onSortChange={onOrderByFieldChange}
							orderBy={orderBy}
							orderByField={orderByField}
							page={page}
							paginationProps={paginationProps}
							total={data ? data.pageAssets.total : 0}
						/>
					</div>
				)}
			</Modal.Body>

			<Modal.Footer>
				<Button onClick={onClose}>
					{Liferay.Language.get('cancel')}
				</Button>

				<Button
					disabled={!stringMatch || !metadata}
					display='primary'
					onClick={() =>
						onSubmit({
							id: `${includeExclude} - ${filter}`,
							name: includeExclude,
							value: filter
						})
					}
				>
					{Liferay.Language.get('add-rule')}
				</Button>
			</Modal.Footer>
		</Modal>
	);
};

export default withStatefulPagination(
	NewRuleModal,
	{
		defaultDelta: 5,
		defaultOrderBy: orderDescending,
		defaultOrderByField: 'title'
	},
	null,
	false
);
