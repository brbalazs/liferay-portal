import ConnectDXP from 'shared/components/onboarding-modal/ConnectDXP';
import DXPConnectionType from 'shared/components/onboarding-modal/DXPConnectionType';
import Modal from 'shared/components/modal';
import React, {useState} from 'react';

const MODAL_SCREENS = [DXPConnectionType, ConnectDXP];

interface IConnectDXPModalProps {
	groupId: string;
	id: string;
	onClose: () => void;
}

const ConnectDXPModal: React.FC<IConnectDXPModalProps> = ({
	groupId,
	id,
	onClose
}) => {
	const [dxpConnected, setDxpConnected] = useState(false);
	const [step, setStep] = useState(0);

	const ScreenComponent = MODAL_SCREENS[step];

	return (
		<Modal className='connect-dxp onboarding-modal-root' size='xxl'>
			<ScreenComponent
				dataSourceId={id}
				dxpConnected={dxpConnected}
				groupId={groupId}
				isUpgrading={false}
				onClose={onClose}
				onDxpConnected={setDxpConnected}
				onNext={(increment = 1) => setStep(step + increment)}
				onPrevious={() => setStep(step - 1)}
			/>
		</Modal>
	);
};

export default ConnectDXPModal;
