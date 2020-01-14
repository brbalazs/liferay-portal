import {ClayIconSpriteContext} from '@clayui/icon';
import {ClayPaginationBarWithBasicItems} from '@clayui/pagination-bar';
import PropTypes from 'prop-types';
import React, {useState, useRef, useEffect} from 'react';

import {getRandomId, showNotification} from '../../utilities/index.es';
import {createOdataFilterStrings} from '../../utilities/odata.es';
import Modal from '../modal/Modal.es';
import DatasetDisplayContext from './DatasetDisplayContext.es';
import EmptyResultMessage from './EmptyResultMessage.es';
import ManagementBar from './management_bar/index.es';
import {getRenderers} from './utilities/contentRenderers.es';
import {formatFilters} from './utilities/filters.es';



function loadData(apiUrl, filters, delta, page = 1, sorting = []) {
	const authString = `&p_auth=${window.Liferay.authToken}`;
	const filterString = `&${createOdataFilterStrings(filters)}`;
	const pagination = `&pageSize=${delta}&page=${page}`;
	const sortingString = sorting.length ? `&orderBy=${JSON.stringify(sorting)}` : ``;

	const url = `${apiUrl}${authString}${pagination}${sortingString}${filterString}`;

	return fetch(url)
		.then(response => response.json())
}

function DatasetDisplay(props) {
	const contentRenderers = getRenderers(props.contentRenderers);

	const [datasetDisplaySupportModalId] = useState('support-modal-' + getRandomId())

	const [selectedItemsId, setselectedItemsId] = useState([]);
	const [filters, updateFilters] = useState(formatFilters(props.filters));
	const [sorting, updateSorting] = useState(props.sorting)
	const [items, updateItems] = useState(props.items)
	const [pageNumber, setPageNumber] = useState(props.pagination.initialPageNumber || 1);
	const [delta, setDelta] = useState(props.pagination.initialDelta || props.pagination.deltas[0].label);
	const [totalItems, setTotalItems] = useState(props.pagination.initialTotalItems);
	const [activeContentRenderer, setActiveContentRenderer] = useState(props.activeContentRenderer || contentRenderers.find((el) => el.default).id);

	const ContentRendererComponent = contentRenderers.find(renderer => renderer.id === activeContentRenderer).component;

	const formRef = useRef(null);

	function updateDataset(dataSetData) {
		if (dataSetData instanceof Array) {
			return updateItems(dataSetData);
		}
		updateItems(dataSetData.items);
		setTotalItems(dataSetData.totalItems);
	}

	function getData(apiUrl, filters, delta, pageNumber, sorting, showSuccessNotification = false) {
		return loadData(apiUrl, filters, delta, pageNumber, sorting)
			.then(updateDataset)
			.then(() => {
				if (showSuccessNotification) {
					showNotification(
						Liferay.Language.get('table-data-updated'),
						'success'
					)
				}
			})
			.catch((e) => {
				console.error(e)
				showNotification(
					Liferay.Language.get('unexpected-error'),
					'danger'
				)
			})
	};

	useEffect(() => {
		getData(props.apiUrl, filters.filter(e => !!e.value), delta, pageNumber, sorting)
	// eslint-disable-next-line react-hooks/exhaustive-deps
	}, [props.apiUrl, filters, delta, pageNumber, sorting])

	const selectItems = (val, checked) => {
		if (val === 'all-items') {
			if (checked) {
				setselectedItemsId(props.items.map(el => el.id));
			}
			else {
				setselectedItemsId([]);
			}
		}
		else {
			if (checked) {
				setselectedItemsId(selectedItemsId.concat(val));
			}
			else {
				setselectedItemsId(selectedItemsId.filter(el => el !== val));
			}
		}
	};

	const management_bar = props.showManagementBar ? (
		<div className="dataset-display-management-bar-wrapper">
			<ManagementBar
				activeContentRenderer={activeContentRenderer}
				bulkActions={props.bulkActions}
				contentRenderers={props.contentRenderers}
				creationMenuItems={props.creationMenuItems}
				filters={filters}
				fluid={props.style === 'fluid'}
				onFiltersChange={updateFilters}
				selectAllItems={() => selectItems('all-items', true)}
				selectedItemsId={selectedItemsId}
				setActiveContentRenderer={setActiveContentRenderer}
				sidePanelId={props.sidePanelId}
				totalItemsCount={props.items.length}
			/>
		</div>
	) : null;

	const content = (
		<div className="dataset-display-content-wrapper">
			{
				items && items.length ? ( 
					<ContentRendererComponent
						items={items}
						onSelect={selectItems}
						schema={props.schema}
						selectable={props.bulkActions && !!props.bulkActions.length}
						selectedItemsId={selectedItemsId}
						sidePanelId={props.sidePanelId}
						sorting={sorting}
					/>
				) : (
					<EmptyResultMessage />
				)
			}
		</div>
	)

	const pagination = (props.showPagination && props.pagination && items.length) ? (
		<div className="dataset-display-pagination-wrapper">
			<ClayPaginationBarWithBasicItems
				activeDelta={delta}
				activePage={pageNumber}
				deltas={props.deltas}
				ellipsisBuffer={3}
				onDeltaChange={(deltaVal) => {
					setPageNumber(1);
					setDelta(deltaVal);
				}}
				onPageChange={setPageNumber}
				totalItems={totalItems}
			/>
		</div>
	) : null

	return (
		<DatasetDisplayContext.Provider
			value={{
				formRef,
				loadData: () => getData(props.apiUrl, filters.filter(e => !!e.value), delta, pageNumber, sorting, true),
				modalId: datasetDisplaySupportModalId,
				sidePanelId: props.sidePanelId,
				sorting,
				updateSorting,
			}}
		>
			<ClayIconSpriteContext.Provider value={props.spritemap}>
				<Modal id={datasetDisplaySupportModalId} />
					{props.style === 'default' && (
						<div className="dataset-display">
							{management_bar}
							{content}
							{pagination}
						</div>
					)}
					{props.style === 'stacked' && (
						<div className="dataset-display dataset-display-stacked">
							{management_bar}
							{content}
							{pagination}
						</div>
					)}
					{props.style === 'fluid' && (
						<div className="dataset-display dataset-display-fluid">
							{management_bar}
							<div className="container mt-3">
								{content}
								{pagination}
							</div>
						</div>
					)}
			</ClayIconSpriteContext.Provider>
		</DatasetDisplayContext.Provider>
	);
}

DatasetDisplay.propTypes = {
	activeContentRenderer: PropTypes.string,
	apiUrl: PropTypes.string.isRequired,
	bulkActions: PropTypes.array,
	contentRenderers: PropTypes.arrayOf(
		PropTypes.shape({
			component: PropTypes.any.isRequired,
			default: PropTypes.bool,
			icon: PropTypes.string,
			id: PropTypes.string,
			label: PropTypes.string,
		})
	),
	creationMenuItems: PropTypes.array,
	filters: PropTypes.array,
	id: PropTypes.string.isRequired,
	items: PropTypes.array.isRequired,
	pagination: PropTypes.shape({
		deltas: PropTypes.arrayOf(
			PropTypes.shape({
				href: PropTypes.string,
				label: PropTypes.number.isRequired,
			}).isRequired,
		),
		initialDelta: PropTypes.number.isRequired,
		initialPageNumber: PropTypes.number,
		initialTotalItems: PropTypes.number.isRequired,
	}),
	schema: PropTypes.object,
	showManagementBar: PropTypes.bool,
	showPagination: PropTypes.bool,
	sidePanelId: PropTypes.string,
	sorting: PropTypes.array,
	spritemap: PropTypes.string.isRequired,
	style: PropTypes.oneOf([
		'default',
		'fluid',
		'stacked'
	]),
};

DatasetDisplay.defaultProps = {
	filters: [],
	items: [],
	showManagementBar: true,
	showPagination: true,
	sorting: [],
	style: 'default'
};

export default DatasetDisplay;
