import BasePage from 'shared/components/base-page';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import CardWithRangeKey from 'shared/hoc/CardWithRangeKey';
import React, {useCallback, useContext, useState} from 'react';
import {HOC_CARD_PROPTYPES} from 'shared/util/proptypes';
import {pickBy} from 'lodash';
import {setUriQueryValues} from 'shared/util/router';
import {Tab, default as Tabs} from 'shared/components/TableTabs';
import {useParams} from 'react-router-dom';

interface ITableTabsCardProps extends React.HTMLAttributes<HTMLElement> {
	footerHref?: string;
	footerLabel?: string;
	label: string;
	legacyDropdownRangeKey: boolean;
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
		legacyDropdownRangeKey,
		metricLabel
	}: ITableTabsCardProps) => {
		const {router} = useContext(BasePage.Context);
		const {channelId} = useParams();

		const [activeTabId, setActiveTab] = useState(tabConfig[0].tabId);
		const handleActiveTabChanged = useCallback(
			newVal => setActiveTab(newVal),
			[]
		);

		return (
			<CardWithRangeKey
				className={className}
				label={label}
				legacyDropdownRangeKey={legacyDropdownRangeKey}
			>
				{({rangeSelectors}) => (
					<>
						<ComponentWithData
							activeTabId={activeTabId}
							channelId={channelId}
							filters={{}}
							metricLabel={metricLabel}
							onActiveTabChange={handleActiveTabChanged}
							rangeSelectors={rangeSelectors}
							router={router}
							tabConfig={tabConfig}
							tableConfig={tableConfig}
						/>

						{footerHref && (
							<Card.Footer>
								<Button
									display='link'
									href={setUriQueryValues(
										pickBy({...rangeSelectors}),
										footerHref
									)}
									icon='angle-right'
									iconAlignment='right'
									size='sm'
								>
									{footerLabel}
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
