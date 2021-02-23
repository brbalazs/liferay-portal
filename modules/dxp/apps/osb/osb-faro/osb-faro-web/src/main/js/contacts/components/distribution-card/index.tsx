import AddDataSource from './AddDataSource';
import AddPropertyForm from './AddPropertyForm';
import autobind from 'autobind-decorator';
import Card from 'shared/components/Card';
import DistributionChart from './DistributionChart';
import ErrorDisplay from 'shared/components/ErrorDisplay';
import FaroConstants from 'shared/util/constants';
import React from 'react';
import Spinner from 'shared/components/Spinner';
import Tabs from './Tabs';
import {addAlert, alertTypes} from 'shared/actions/alerts';
import {
	addDistributionTab,
	fetchDistributionTabs,
	removeDistributionTab
} from 'shared/actions/preferences';
import {connect} from 'react-redux';
import {DistributionTab} from 'shared/util/records';
import {List, Map} from 'immutable';

const {
	preferencesScopes: {group}
} = FaroConstants;

interface IDistributionCardProps extends React.HTMLAttributes<HTMLElement> {
	addAlert: (object) => Promise<any>;
	addDistributionTab: (object) => Promise<any>;
	channelId: string;
	distributionKey: string;
	distributionTabsIList: List<DistributionTab>;
	error: boolean;
	fetchDistribution: (object) => Promise<any>;
	fetchDistributionTabs: (object) => Promise<any>;
	groupId: string;
	id: string;
	loading: boolean;
	removeDistributionTab: (object) => Promise<any>;
	showAddDataSource: boolean;
	showContext: boolean;
	viewAllLink: string;
}

interface IDistributionCardState {
	selectedTabIndex: number;
	showAddProperty: boolean;
}

class DistributionCard extends React.Component<
	IDistributionCardProps,
	IDistributionCardState
> {
	static defaultProps = {
		showAddDataSource: false
	};

	state = {
		selectedTabIndex: 0,
		showAddProperty: false
	};

	componentDidMount() {
		this.handleFetchDistributionTabs();
	}

	@autobind
	handleAddTab(tab: DistributionTab) {
		const {
			addAlert,
			addDistributionTab,
			distributionKey,
			groupId,
			id
		} = this.props;

		return addDistributionTab({
			distributionKey,
			distributionTab: tab,
			distributionTabId: tab.id,
			groupId,
			id
		})
			.then(({payload: {order}}) =>
				this.setState({
					selectedTabIndex: order.length - 1,
					showAddProperty: false
				})
			)
			.catch(({message}) =>
				addAlert({
					alertType: alertTypes.ERROR,
					message,
					timeout: false
				})
			);
	}

	@autobind
	handleDeleteTab(tabId: string) {
		const {
			distributionKey,
			groupId,
			id,
			removeDistributionTab
		} = this.props;

		this.setState({selectedTabIndex: 0}, () =>
			removeDistributionTab({
				distributionKey,
				distributionTabId: tabId,
				groupId,
				id
			})
		);
	}

	@autobind
	handleFetchDistributionTabs() {
		const {
			distributionKey,
			fetchDistributionTabs,
			groupId,
			id
		} = this.props;

		fetchDistributionTabs({distributionKey, groupId, id});
	}

	@autobind
	handleSelectTab(tabId: string) {
		const {distributionTabsIList} = this.props;

		this.setState({
			selectedTabIndex: distributionTabsIList.findIndex(
				tabIMap => tabIMap.get('id') === tabId
			)
		});
	}

	@autobind
	handleShowAddProperty(showAddProperty: boolean) {
		this.setState({showAddProperty});
	}

	render() {
		const {
			props: {
				channelId,
				distributionKey,
				distributionTabsIList,
				error,
				fetchDistribution,
				groupId,
				id,
				loading,
				showAddDataSource,
				showContext,
				viewAllLink
			},
			state: {selectedTabIndex, showAddProperty}
		} = this;

		const tabsCount = distributionTabsIList.size;

		return (
			<Card className='distribution-card-root' minHeight={536}>
				{error && !showAddProperty && (
					<ErrorDisplay
						onReload={this.handleFetchDistributionTabs}
						spacer
					/>
				)}

				{!loading && showAddDataSource && (
					<AddDataSource groupId={groupId} />
				)}

				{(!error || showAddProperty) && !showAddDataSource && (
					<>
						<Card.Header>
							<Tabs
								itemsIList={distributionTabsIList}
								onAdd={() => this.handleShowAddProperty(true)}
								onDelete={this.handleDeleteTab}
								onSelect={this.handleSelectTab}
								selectedTabIndex={selectedTabIndex}
								showAddProperty={showAddProperty || !tabsCount}
							/>
						</Card.Header>

						{((!tabsCount && !loading) || showAddProperty) && (
							<AddPropertyForm
								distributionKey={distributionKey}
								groupId={groupId}
								onCancel={() =>
									this.handleShowAddProperty(false)
								}
								onSubmit={this.handleAddTab}
								showContext={showContext}
								tabsIList={distributionTabsIList}
							/>
						)}

						{loading && <Spinner overlay />}

						{!!tabsCount && !showAddProperty && !loading && (
							<DistributionChart
								channelId={channelId}
								distributionKey={distributionKey}
								fetchDistribution={fetchDistribution}
								groupId={groupId}
								id={id}
								selectedTab={distributionTabsIList.get(
									selectedTabIndex
								)}
								viewAllLink={viewAllLink}
							/>
						)}
					</>
				)}
			</Card>
		);
	}
}

export default connect(
	(state, {distributionKey}) => {
		const distributionTabs = state.getIn(
			['preferences', group, 'distributionCardTabs', distributionKey],
			Map()
		);

		return {
			distributionTabsIList: distributionTabs.get('data', List()),
			error: distributionTabs.get('error', false),
			loading: distributionTabs.get('loading', true)
		};
	},
	{addAlert, addDistributionTab, fetchDistributionTabs, removeDistributionTab}
)(DistributionCard);
