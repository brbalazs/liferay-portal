import autobind from 'autobind-decorator';
import Button from './Button';
import debounce from 'shared/util/debounce-decorator';
import FaroConstants from 'shared/util/constants';
import getCN from 'classnames';
import Icon from 'shared/components/Icon';
import Modal from './modal';
import ModalInfoBar from 'shared/components/ModalInfoBar';
import Nav from 'shared/components/Nav';
import NavBar from 'shared/components/NavBar';
import NoResultsDisplay, {
	getFormattedTitle
} from 'shared/components/NoResultsDisplay';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import SearchInput from './SearchInput';
import Spinner from 'shared/components/Spinner';
import {autoCancel, hasRequest} from 'shared/util/request-decorator';
import {noop} from 'lodash';
import {PropTypes} from 'prop-types';
import {sub} from 'shared/util/lang';

const {orderAscending, orderDescending} = FaroConstants.pagination;

@hasRequest
export default class SearchableModal extends React.Component {
	static defaultProps = {
		countLabel: Liferay.Language.get('x-items'),
		delta: 10,
		fitContent: false,
		footer: null,
		items: [],
		onChange: noop,
		onClose: noop,
		showSortButton: true,
		showToolbar: true,
		title: Liferay.Language.get('see-all')
	};

	static propTypes = {
		countLabel: PropTypes.string,
		dataSourceFn: PropTypes.func.isRequired,
		delta: PropTypes.number,
		fitContent: PropTypes.bool,
		footer: PropTypes.node,
		items: PropTypes.array,
		noResultsIcon: PropTypes.string,
		noResultsName: PropTypes.string,
		noResultsTitle: PropTypes.string,
		onChange: PropTypes.func,
		onClose: PropTypes.func,
		showSortButton: PropTypes.bool,
		showToolbar: PropTypes.bool,
		title: PropTypes.string
	};

	state = {
		loading: true,
		query: '',
		reverseSort: false,
		total: 0
	};
	constructor(props) {
		super(props);

		this._page = 1;
	}

	componentDidMount() {
		this.getItems();
	}

	componentWillUnmount() {
		this.getFilteredItems.cancel();
	}

	@debounce(500)
	getFilteredItems(query) {
		this._page = 1;

		this.setState({
			query
		});

		this.getItems(query);
	}

	@autoCancel
	getItems(query) {
		const {
			props: {dataSourceFn, delta, items, onChange},
			state: {reverseSort}
		} = this;

		this.setState({
			loading: true
		});

		return dataSourceFn({
			cur: this._page,
			delta,
			orderBy: reverseSort ? orderDescending : orderAscending,
			query
		})
			.then(({items: newItems, total}) => {
				onChange(this._page > 1 ? [...items, ...newItems] : newItems);

				this.setState({
					loading: false,
					total
				});

				this._page++;
			})
			.catch(noop);
	}

	@autobind
	handleFilter(value) {
		this.getFilteredItems(value);
	}

	@autobind
	handleLoadMoreClick() {
		this.getItems();
	}

	@autobind
	handleSortOrder() {
		this._page = 1;

		this.setState(
			{
				reverseSort: !this.state.reverseSort
			},
			() => this.getItems()
		);
	}

	renderChildren() {
		const {
			props: {
				children,
				items,
				noResultsIcon,
				noResultsName,
				noResultsTitle
			},
			state: {loading, total}
		} = this;

		if (items.length < total) {
			return (
				<div>
					{children}

					<div className='load-more-container'>
						<Button
							className={getCN({loading})}
							onClick={this.handleLoadMoreClick}
						>
							{loading ? (
								<Spinner />
							) : (
								Liferay.Language.get('load-more')
							)}
						</Button>
					</div>
				</div>
			);
		} else if (loading) {
			return <Spinner spacer />;
		} else if (!total) {
			return (
				<NoResultsDisplay
					icon={noResultsIcon ? {symbol: noResultsIcon} : undefined}
					spacer
					title={getFormattedTitle(noResultsName, noResultsTitle)}
				/>
			);
		} else {
			return <div>{children}</div>;
		}
	}

	render() {
		const {
			props: {
				className,
				countLabel,
				fitContent,
				footer,
				onClose,
				showSortButton,
				showToolbar,
				title,
				...otherProps
			},
			state: {query, reverseSort, total}
		} = this;

		const contentClasses = getCN('scroll-container', {
			'fit-content': fitContent
		});

		return (
			<Modal
				{...omitDefinedProps(otherProps, SearchableModal.propTypes)}
				className={getCN('searchable-modal-root', className)}
				size='lg'
			>
				<Modal.Header onClose={onClose} title={title} />

				<div>
					{showToolbar && (
						<NavBar display='light'>
							{showSortButton && (
								<Nav>
									<Nav.Item>
										<Button
											className='nav-link nav-link-monospaced'
											display='unstyled'
											onClick={this.handleSortOrder}
										>
											<Icon
												symbol={
													reverseSort
														? 'order-arrow-descending'
														: 'order-arrow-ascending'
												}
											/>
										</Button>
									</Nav.Item>
								</Nav>
							)}

							<div className='navbar-form navbar-form-autofit'>
								<SearchInput
									autoFocus
									onChange={query => this.setState({query})}
									onSubmit={this.handleFilter}
									placeholder={Liferay.Language.get('search')}
									value={query}
								/>
							</div>
						</NavBar>
					)}

					<div className={contentClasses}>
						{!!total && (
							<ModalInfoBar>
								{sub(countLabel, [total])}
							</ModalInfoBar>
						)}

						{this.renderChildren()}
					</div>
				</div>

				{footer}
			</Modal>
		);
	}
}
