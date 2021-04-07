import IssueSubmitted from './IssueSubmitted';
import Modal from '../modal';
import React, {useState} from 'react';
import ReportIssue from './ReportIssue';

const MODAL_SCREENS = [ReportIssue, IssueSubmitted];

interface IHelpWidgetModalProps {
	onClose: () => void;
}

const HelpWidgetModal: React.FC<IHelpWidgetModalProps> = ({onClose}) => {
	const [step, setStep] = useState(0);

	const ScreenComponent = MODAL_SCREENS[step];

	return (
		<Modal className='help-widget-modal-root'>
			<ScreenComponent
				onClose={onClose}
				onNext={(increment = 1) => setStep(step + increment)}
			/>
		</Modal>
	);
};

export default HelpWidgetModal;
