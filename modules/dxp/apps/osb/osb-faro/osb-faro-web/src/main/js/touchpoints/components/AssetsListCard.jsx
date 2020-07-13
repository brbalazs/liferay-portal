import BasePage from 'shared/components/base-page';
import React from 'react';
import Table from 'shared/components/table';
import TextTruncate from 'shared/components/TextTruncate';
import {Link} from 'react-router-dom';
import {pickBy} from 'lodash';
import {PropTypes} from 'prop-types';
import {toAssetDashboardRoute} from 'shared/util/router';

const CLASSNAME = 'analytics-assets-list';

const ITEMS_SHAPE = PropTypes.shape({
	assetId: PropTypes.string,
	assetType: PropTypes.string,
	interactions: PropTypes.number,
	title: PropTypes.string,
	type: PropTypes.string
});

/**
 * Assets List Card
 * @class
 */
class AssetsListCard extends React.Component {
	static contextType = BasePage.Context;

	static defaultProps = {
		items: []
	};

	static propTypes = {
		items: PropTypes.arrayOf(ITEMS_SHAPE),
		rangeSelectors: PropTypes.object
	};

	constructor(props) {
		super(props);

		this._elementRef = React.createRef();
	}

	/**
	 * Get Asset URL
	 * @description Get url to navigate in a dashboard
	 * @param {string} assetId
	 * @param {string} assetType
	 * @param {string} title
	 */
	getUrl(assetId, assetType, title) {
		const {
			context: {
				router: {params, query}
			},
			props: {rangeSelectors}
		} = this;

		return toAssetDashboardRoute(
			assetType,
			{
				...params,
				assetId,
				title,
				...(assetType === 'custom' ? {id: assetId} : false)
			},
			pickBy({
				...query,
				rangeEnd: rangeSelectors.rangeEnd,
				rangeKey: rangeSelectors.rangeKey,
				rangeStart: rangeSelectors.rangeStart
			})
		);
	}

	/**
	 * Render Title Column
	 * @param {object} param0
	 */
	renderTitleColumn({assetId, assetType, title}) {
		const url = this.getUrl(assetId, assetType, title);

		return (
			<td className='table-cell-expand'>
				<Link
					className='font-weight-semibold text-truncate-inline text-dark'
					to={url}
				>
					<TextTruncate title={title} />
				</Link>
			</td>
		);
	}

	/**
	 * Render Type Column
	 * @param {object} param0
	 */
	renderTypeColumn({type}) {
		return (
			<td className='table-cell-expand'>
				<div className='font-weight-semibold text-secondary text-truncate-inline'>
					<TextTruncate title={type} />
				</div>
			</td>
		);
	}

	/**
	 * Render Interaction Column
	 * @param {object} param0
	 */
	renderInteractionColumn({interactions = '-'}) {
		return (
			<td className='font-weight-semibold text-secondary table-cell-expand table-column-text-end'>
				<div className='w-100'>{interactions.toLocaleString()}</div>
			</td>
		);
	}

	/**
	 * Lifecycle Render - ReactJS
	 */
	render() {
		const {items} = this.props;

		const tableColumns = [
			{
				accessor: 'title',
				cellRenderer: ({data}) => this.renderTitleColumn(data),
				label: Liferay.Language.get('asset-name'),
				sortable: false,
				title: true
			},
			{
				accessor: 'type',
				cellRenderer: ({data}) => this.renderTypeColumn(data),
				label: Liferay.Language.get('asset-type'),
				sortable: false
			},
			{
				accessor: 'interactions',
				cellRenderer: ({data}) => this.renderInteractionColumn(data),
				className: 'table-column-text-end',
				label: Liferay.Language.get('interactions'),
				sortable: false
			}
		];

		return (
			<div className={CLASSNAME} ref={this._elementRef}>
				<Table
					className='table-hover'
					columns={tableColumns}
					items={items}
					rowIdentifier={['assetId', 'assetTitle']}
				/>
			</div>
		);
	}
}

export default AssetsListCard;
