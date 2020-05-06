import BasePage from 'shared/components/base-page';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import CardWithRangeKey from 'shared/hoc/CardWithRangeKey';
import Icon from 'shared/components/Icon';
import React, {useCallback, useContext, useState} from 'react';
import {HOC_CARD_PROPTYPES} from 'shared/util/proptypes';
import {setUriQueryValues} from 'shared/util/router';
import {Tab, default as Tabs} from 'shared/components/TableTabs';

interface IBasePageContext {
	filters: object;
	rangeKey: {
		defaultValue: string;
		lastValue: string;
	};
	router: {
		params: object;
		query: object;
	};
}

interface ITableTabsCardProps extends React.HTMLAttributes<HTMLElement> {
	footerHref?: string;
	footerLabel?: string;
	label: string;
	metricLabel: string;
}

/**
 * HOC
 * @description Table Tabs Card
 * @param {function} withTableTabs
 */
const withTableTabs = (withData, tabConfig: Tab[], tableConfig: object) => {
	const ComponentWithData = withData()(Tabs);

	ComponentWithData.propTypes = HOC_CARD_PROPTYPES;

	const defaultProps = {
		className: 'table-tabs-root',
		metricLabel: Liferay.Language.get('items')
	};

	const TableTabsCard = ({
		className,
		footerHref,
		footerLabel,
		label,
		metricLabel
	}: ITableTabsCardProps) => {
		const {router} = useContext(BasePage.Context as React.Context<
			IBasePageContext
		>);

		const [activeTabId, setActiveTab] = useState(tabConfig[0].tabId);
		const handleActiveTabChanged = useCallback(
			newVal => setActiveTab(newVal),
			[]
		);

		return (
			<CardWithRangeKey className={className} label={label}>
				{({rangeKey}) => (
					<>
						<ComponentWithData
							activeTabId={activeTabId}
							filters={{}}
							metricLabel={metricLabel}
							onActiveTabChange={handleActiveTabChanged}
							rangeKey={rangeKey}
							router={router}
							tabConfig={tabConfig}
							tableConfig={tableConfig}
						/>

						{footerHref && (
							<Card.Footer>
								<Button
									display='link'
									href={setUriQueryValues(
										{rangeKey},
										footerHref
									)}
									size='sm'
								>
									{footerLabel}

									<Icon symbol='angle-right' />
								</Button>
							</Card.Footer>
						)}
					</>
				)}
			</CardWithRangeKey>
		);
	};

	TableTabsCard.defaultProps = defaultProps;

	return TableTabsCard;
};

export {withTableTabs};
