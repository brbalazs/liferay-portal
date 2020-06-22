import AddChannelModal from 'settings/components/AddChannelModal';
import autobind from 'autobind-decorator';
import BatchActionModal from 'settings/components/user-list/BatchActionModal';
import ConfirmationModal from './ConfirmationModal';
import ConnectDXPModal from 'settings/components/ConnectDXPModal';
import ContactSalesModal from './ContactSalesModal';
import CreateMappingModal from './CreateMappingModal';
import CSVPreviewModal from 'settings/components/csv/CSVPreviewModal';
import DeleteChannelModal from 'settings/components/DeleteChannelModal';
import DeleteConfirmationModal from 'shared/components/DeleteConfirmationModal';
import dom from 'metal-dom';
import ExportLogModal from 'settings/components/ExportLogModal';
import FieldPreviewModal from 'settings/components/data-transformation-list/FieldPreviewModal';
import IndividualAttributesModal from 'settings/components/IndividualAttributesModal';
import InputModal from './InputModal';
import InterestTopicModal from 'settings/components/InterestTopicsModal';
import InviteUsersModal from 'settings/components/InviteUsersModal';
import LoadingModal from './LoadingModal';
import MatchingPagesModal from 'settings/recommendations/components/MatchingPagesModal';
import NewRequestModal from './NewRequestModal';
import NewRuleModal from 'settings/recommendations/components/NewRuleModal';
import OnboardingModal from './onboarding-modal';
import React from 'react';
import SearchableEntitiesTableModal from './SearchableEntitiesTableModal';
import SearchableTableModal from './SearchableTableModal';
import SearchableTableModalGraphql from './SearchableTableModalGraphql';
import SelectItemsModal from './SelectItemsModal';
import TestModal from './TestModal';
import UnassignedSegmentsModal from 'shared/components/unassigned-segments-modal';
import UpgradeConnectionModal from 'settings/components/data-source/upgrade-connection-modal';
import {close, modalTypes} from '../actions/modals';
import {connect} from 'react-redux';
import {List} from 'immutable';
import {onEnter} from 'shared/util/key-constants';
import {PropTypes} from 'prop-types';

const BODY_CLASSNAME = 'modal-open';

const COMPONENT_MAP = {
	[modalTypes.ADD_CHANNEL_MODAL]: AddChannelModal,
	[modalTypes.BATCH_ACTION_MODAL]: BatchActionModal,
	[modalTypes.CONFIRMATION_MODAL]: ConfirmationModal,
	[modalTypes.CONNECT_DXP_MODAL]: ConnectDXPModal,
	[modalTypes.CONTACT_SALES_MODAL]: ContactSalesModal,
	[modalTypes.CREATE_MAPPING_MODAL]: CreateMappingModal,
	[modalTypes.CSV_PREVIEW_MODAL]: CSVPreviewModal,
	[modalTypes.DELETE_CHANNEL_MODAL]: DeleteChannelModal,
	[modalTypes.DELETE_CONFIRMATION_MODAL]: DeleteConfirmationModal,
	[modalTypes.EXPORT_LOG_MODAL]: ExportLogModal,
	[modalTypes.FIELD_PREVIEW_MODAL]: FieldPreviewModal,
	[modalTypes.INDIVIDUAL_ATTRIBUTES_MODAL]: IndividualAttributesModal,
	[modalTypes.INPUT_MODAL]: InputModal,
	[modalTypes.INSERT_BLOCKED_KEYWORDS]: InterestTopicModal,
	[modalTypes.INVITE_USERS_MODAL]: InviteUsersModal,
	[modalTypes.LOADING_MODAL]: LoadingModal,
	[modalTypes.MATCHING_PAGES_MODAL]: MatchingPagesModal,
	[modalTypes.NEW_REQUEST_MODAL]: NewRequestModal,
	[modalTypes.NEW_RULE_MODAL]: NewRuleModal,
	[modalTypes.ONBOARDING_MODAL]: OnboardingModal,
	[modalTypes.UNASSIGNED_SEGMENTS_MODAL]: UnassignedSegmentsModal,
	[modalTypes.UPGRADE_CONNECTION_MODAL]: UpgradeConnectionModal,
	[modalTypes.SEARCHABLE_ENTITIES_TABLE_MODAL]: SearchableEntitiesTableModal,
	[modalTypes.SEARCHABLE_TABLE_MODAL]: SearchableTableModal,
	[modalTypes.SEARCHABLE_TABLE_MODAL_GRAPHQL]: SearchableTableModalGraphql,
	[modalTypes.SELECT_ITEMS_MODAL]: SelectItemsModal,
	[modalTypes.TEST]: TestModal
};

function toggleBodyModalOpen(open = true) {
	if (open) {
		document.body.classList.add(BODY_CLASSNAME);
	} else {
		document.body.classList.remove(BODY_CLASSNAME);
	}
}

export class ModalRenderer extends React.Component {
	static propTypes = {
		close: PropTypes.func.isRequired,
		modalsIList: PropTypes.instanceOf(List).isRequired
	};

	componentDidUpdate() {
		toggleBodyModalOpen(!!this.getCurrentModal());
	}

	componentWillUnmount() {
		toggleBodyModalOpen(false);
	}

	getCurrentModal() {
		return this.props.modalsIList.last();
	}

	@autobind
	@onEnter
	handleKeyPress(event) {
		this.handleClickOutside(event);
	}

	@autobind
	handleClickOutside(event) {
		const currentModalIMap = this.getCurrentModal();

		if (
			currentModalIMap.get('closeOnBlur', true) &&
			dom.match(event.target, '.modal-container')
		) {
			this.props.close();
		}
	}

	render() {
		return (
			<div
				className={`modal-renderer-root${
					this.props.className ? ` ${this.props.className}` : ''
				}`}
			>
				{this.props.modalsIList
					.map((modalIMap, i) => {
						const ModalComponent =
							COMPONENT_MAP[modalIMap.get('type')];

						return (
							<div
								className={`modal-container d-block fade modal show${
									this.props.className
										? ` ${this.props.className}`
										: ''
								}`}
								key={i}
								onClick={this.handleClickOutside}
								onKeyPress={this.handleKeyPress}
								role='button'
								tabIndex='0'
							>
								<ModalComponent
									{...modalIMap.get('props').toObject()}
								/>
							</div>
						);
					})
					.toJS()}

				{!!this.getCurrentModal() && (
					<div className='modal-backdrop fade show' />
				)}
			</div>
		);
	}
}

export default connect(
	state => ({
		modalsIList: state.get('modals', new List())
	}),
	{close}
)(ModalRenderer);
