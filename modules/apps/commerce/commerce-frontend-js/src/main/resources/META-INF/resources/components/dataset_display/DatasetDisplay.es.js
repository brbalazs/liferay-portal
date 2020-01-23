import {ClayIconSpriteContext} from '@clayui/icon';
import {ClayPaginationBarWithBasicItems} from '@clayui/pagination-bar';
import PropTypes from 'prop-types';
import React, {useState, useRef, useEffect} from 'react';

import {getRandomId, showNotification, getJsModule} from '../../utilities/index.es';
import {getViewById} from './views/index.es';
import {createOdataFilterStrings} from '../../utilities/odata.es';
import Modal from '../modal/Modal.es';
import DatasetDisplayContext from './DatasetDisplayContext.es';
import EmptyResultMessage from './EmptyResultMessage.es';
import ManagementBar from './management_bar/index.es';
import {formatFilters} from './utilities/filters.es';
import { OPEN_SIDE_PANEL, OPEN_MODAL } from '../../utilities/eventsDefinitions.es';

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
	const [views, updateViews] = useState(props.views);
	const [loading, setLoading] = useState(false);

	const [datasetDisplaySupportModalId] = useState('support-modal-' + getRandomId())

	const [selectedItemsValue, setSelectedItemsValue] = useState([]);
	const [filters, updateFilters] = useState(formatFilters(props.filters));
	const [sorting, updateSorting] = useState(props.sorting)
	const [items, updateItems] = useState(props.items)
	const [pageNumber, setPageNumber] = useState(props.pagination.initialPageNumber || 1);
	const [delta, setDelta] = useState(props.pagination.initialDelta || props.pagination.deltas[0].label);
	const [totalItems, setTotalItems] = useState(props.pagination.initialTotalItems);
	const [activeView, setActiveView] = useState(
		props.activeView || 0
	);
	const {
		component: CurrentViewComponent,
		contentRenderer,
		contentRendererModuleUrl: currentViewModuleUrl,
		...currentViewProps
	} = views[activeView];

	const selectable = props.bulkActions && !!props.bulkActions.length;

	useEffect(() => {
		if(!CurrentViewComponent && (currentViewModuleUrl || contentRenderer)) {
			setLoading(true);
			(
				contentRenderer 
				? getViewById(contentRenderer)
				: getJsModule(currentViewModuleUrl)
			)
				.then((component) => {
					updateViews((views) => views.map((view, i) => i === activeView ? {
						...view,
						component
					} : view))
				})
				.catch((err) => {
					showNotification(Liferay.Language.get('unexpected-error'), 'danger');
					throw new Error(`Requested module: ${currentViewModuleUrl} not available`, err);
				})
				.finally(() => setLoading(false))
		}
	}, [
		activeView,
		contentRenderer,
		views,
		currentViewModuleUrl,
		CurrentViewComponent,
		setLoading
	])

	const formRef = useRef(null);

	function updateDataset(dataSetData) {
		if (dataSetData instanceof Array) {
			return updateItems(dataSetData);
		}
		setTotalItems(dataSetData.totalItems);
		return updateItems(dataSetData.items);
	}

	function getData(apiUrl, filters, delta, pageNumber, sorting, showSuccessNotification = false) {
		return loadData(apiUrl, filters, delta, pageNumber, sorting)
			.then(updateDataset)
			.then(() => {
				if (showSuccessNotification) {
					showNotification(
						Liferay.Language.get('table-data-updated'),
						'success'
					);
				}
			})
			.catch((e) => {
				console.error(e);
				showNotification(
					Liferay.Language.get('unexpected-error'),
					'danger'
				);
			})
	};

	useEffect(() => {
		getData(props.apiUrl, filters.filter(e => !!e.value), delta, pageNumber, sorting);
	// eslint-disable-next-line react-hooks/exhaustive-deps
	}, [props.apiUrl, filters, delta, pageNumber, sorting]);

	const selectItems = (val) => {
		if (val instanceof Array) {
			return setSelectedItemsValue(val);
		}

		if(props.selectionType === 'single') {
			return setSelectedItemsValue([val]);
		} 

		const itemAdded = selectedItemsValue.find(item => item === val);

		if(itemAdded) {
			setSelectedItemsValue(selectedItemsValue.filter(el => el !== val));
		} else {
			setSelectedItemsValue(selectedItemsValue.concat(val))
		}
	};

	const managementBar = props.showManagementBar ? (
		<div className="dataset-display-management-bar-wrapper">
			<ManagementBar
				activeView={activeView}
				bulkActions={props.bulkActions}
				creationMenuItems={props.creationMenuItems}
				filters={filters}
				fluid={props.style === 'fluid'}
				onFiltersChange={updateFilters}
				selectAllItems={() => selectItems(items.map(item => item[props.selectedItemsKey]))}
				selectable={selectable}
				selectedItemsKey={props.selectedItemsKey}
				selectedItemsValue={selectedItemsValue}
				selectionType={props.selectionType}
				setActiveView={setActiveView}
				sidePanelId={props.sidePanelId}
				totalItemsCount={props.items.length}
				views={props.views}
			/>
		</div>
	) : null;

	const view = !loading && CurrentViewComponent ? (
		<div className="dataset-display-content-wrapper">
			<input hidden name={`${props.id}-selected-${props.selectedItemsKey}`} readOnly value={selectedItemsValue.join(',')}/>
			{
				items && items.length ? ( 
					<CurrentViewComponent
						datasetDisplayContext={DatasetDisplayContext}
						items={items}
						{...currentViewProps}
					/>
				) : (
					<EmptyResultMessage />
				)
			}
		</div>
	) : (
		<span aria-hidden="true" className="loading-animation my-7" />
	);

	const wrappedView = props.formId ? view : <form ref={formRef}>{view}</form>

	const pagination = (props.showPagination && props.pagination && items.length) ? (
		<div className="dataset-display-pagination-wrapper">
			<ClayPaginationBarWithBasicItems
				activeDelta={delta}
				activePage={pageNumber}
				className="mb-2"
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
	) : null;

	function openSidePanel(config) {
		return Liferay.fire(OPEN_SIDE_PANEL, {
			id: props.sidePanelId,
			...config
		})
	}

	function openModal(config) {
		return Liferay.fire(OPEN_MODAL, {
			id: datasetDisplaySupportModalId,
			...config
		})
	}

	return (
		<DatasetDisplayContext.Provider
			value={{
				formId: props.formId,
				formRef,
				loadData: () => getData(props.apiUrl, filters.filter(e => !!e.value), delta, pageNumber, sorting, true),
				modalId: datasetDisplaySupportModalId,
				openModal,
				openSidePanel,
				selectItems,
				selectable,
				selectedItemsKey: props.selectedItemsKey,
				selectedItemsValue,
				selectionType: props.selectionType,
				sidePanelId: props.sidePanelId,
				sorting,
				updateSorting,
			}}
		>
			<ClayIconSpriteContext.Provider value={props.spritemap}>
				<Modal id={datasetDisplaySupportModalId} />
				{props.style === 'default' && (
					<div className="dataset-display">
						{managementBar}
						{wrappedView}
						{pagination}
					</div>
				)}
				{props.style === 'stacked' && (
					<div className="dataset-display dataset-display-stacked">
						{managementBar}
						{wrappedView}
						{pagination}
					</div>
				)}
				{props.style === 'fluid' && (
					<div className="dataset-display dataset-display-fluid">
						{managementBar}
						<div className="container mt-3">
							{wrappedView}
							{pagination}
						</div>
					</div>
				)}
			</ClayIconSpriteContext.Provider>
		</DatasetDisplayContext.Provider>
	);
}

DatasetDisplay.propTypes = {
	activeViewId: PropTypes.string,
	apiUrl: PropTypes.string.isRequired,
	bulkActions: PropTypes.array,
	creationMenuItems: PropTypes.array,
	filters: PropTypes.array,
	formId: PropTypes.string,
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
	selectedItemsKey: PropTypes.string,
	selectionType: PropTypes.oneOf([
		'single',
		'multiple',
	]),
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
	views: PropTypes.arrayOf(
		PropTypes.shape({
			component: PropTypes.any,
			contentRenderer: PropTypes.string,
			contentRendererModuleUrl: PropTypes.string,
			icon: PropTypes.string,
			label: PropTypes.string,
			schema: PropTypes.object,
		})
	).isRequired,
};

DatasetDisplay.defaultProps = {
	filters: [],
	items: [],
	selectedItemsKey: 'id',
	selectionType: 'multiple',
	showManagementBar: true,
	showPagination: true,
	sorting: [],
	style: 'default'
};

export default DatasetDisplay;
