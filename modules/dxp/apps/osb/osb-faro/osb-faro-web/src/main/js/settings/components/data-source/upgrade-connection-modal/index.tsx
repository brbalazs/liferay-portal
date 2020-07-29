import ConnectDXP from 'shared/components/onboarding-modal/ConnectDXP';
import Modal from 'shared/components/modal';
import React, {useState} from 'react';
import UpgradeConnectionType from './UpgradeConnectionType';

const MODAL_SCREENS = [UpgradeConnectionType, ConnectDXP];

interface IUpgradeConnectionModalProps {
	groupId: string;
	id: string;
	onClose: () => void;
}

const UpgradeConnectionModal: React.FC<IUpgradeConnectionModalProps> = ({
	groupId,
	id,
	onClose
}) => {
	const [dxpConnected, setDxpConnected] = useState(false);
	const [step, setStep] = useState(0);

	const ScreenComponent = MODAL_SCREENS[step];

	return (
		<Modal className='upgrade-connection-modal-root' size='xxl'>
			<ScreenComponent
				dataSourceId={id}
				dxpConnected={dxpConnected}
				groupId={groupId}
				isUpgrading
				onClose={onClose}
				onDxpConnected={setDxpConnected}
				onNext={(increment = 1) => setStep(step + increment)}
				onPrevious={() => setStep(step - 1)}
			/>
		</Modal>
	);
};

export default UpgradeConnectionModal;
