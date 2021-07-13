import Button from 'shared/components/Button';
import React from 'react';
import URLConstants from 'shared/util/url-constants';
import {Align, ClayDropDownWithItems} from '@clayui/drop-down';
import {close, modalTypes, open} from 'shared/actions/modals';
import {connect} from 'react-redux';
import {Map} from 'immutable';
import {Modal} from 'shared/types';
import {PLANS} from 'shared/util/subscriptions';

const getDropdownItems = ({
	close,
	groupId,
	open,
	showModal
}: {
	close: Modal.close;
	groupId: string;
	open: Modal.open;
	showModal: boolean;
}): {href?: string; label: string; onClick?: () => void; target?: string}[] => [
	showModal
		? {
				label: Liferay.Language.get('report-an-issue'),
				onClick: () => {
					open(modalTypes.HELP_WIDGET_MODAL, {
						groupId,
						onClose: close
					});
				}
		  }
		: {
				href: URLConstants.TicketPageLink,
				label: Liferay.Language.get('report-an-issue'),
				target: '_blank'
		  },
	{
		href: URLConstants.DocumentationLink,
		label: Liferay.Language.get('help-center'),
		target: '_blank'
	}
];

interface IHelpWidgetProps {
	close: Modal.close;
	faroSubscriptionIMap: Map<string, any>;
	groupId: string;
	open: Modal.open;
}

const HelpWidget: React.FC<IHelpWidgetProps> = ({
	close,
	faroSubscriptionIMap,
	groupId,
	open
}) => {
	const basicTier = faroSubscriptionIMap.get('name') === PLANS.basic.name;

	return (
		<div className='help-widget-root'>
			<ClayDropDownWithItems
				alignmentPosition={Align.TopLeft}
				items={getDropdownItems({
					close,
					groupId,
					open,
					showModal: basicTier
				})}
				menuElementAttrs={{
					className: 'help-dropdown-root'
				}}
				trigger={
					<Button
						aria-label={Liferay.Language.get('help')}
						borderless
						className='help-button'
						display='defaut'
						icon='ac-question-mark'
						iconAlignment='right'
						size='sm'
					/>
				}
			/>
		</div>
	);
};

export default connect(
	(store, {groupId}) => ({
		faroSubscriptionIMap: store.getIn(
			['projects', groupId, 'data', 'faroSubscription'],
			Map()
		)
	}),
	{close, open}
)(HelpWidget);
