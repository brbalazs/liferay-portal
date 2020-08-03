import Button from 'shared/components/Button';
import Icon from 'shared/components/Icon';
import Modal from 'shared/components/modal';
import React from 'react';

import {ClayButtonWithIcon} from '@clayui/button';

const BENEFITS = [
	Liferay.Language.get(
		'data-syncing-resumes-immediately-after-configuration'
	),
	Liferay.Language.get('existing-field-mappings-are-maintained')
];

const REQUIREMENTS = [
	`7.2 ${Liferay.Language.get('fix-pack')} 5/SP2`,
	`7.1 ${Liferay.Language.get('fix-pack')} 18/SP4`,
	`7.0 ${Liferay.Language.get('fix-pack')} 93/SP13`
];

const WARNINGS = [
	Liferay.Language.get('sites-and-contacts-must-be-reconfigured-in-dxp'),
	Liferay.Language.get(
		'field-mappings-will-not-be-editable-until-a-future-update'
	)
];

interface ITokenConnectionProps {
	onNext: (increment?: number) => void;
}

const TokenConnection: React.FC<ITokenConnectionProps> = ({onNext}) => (
	<div className='token-connection col-8 d-flex flex-column flex-grow-1'>
		<div className='header'>
			<div className='title'>
				{Liferay.Language.get('upgrading-your-connection-type')}
			</div>
		</div>

		<Modal.Body className='d-flex flex-column'>
			<span className='description'>
				{Liferay.Language.get(
					'using-our-new-token-based-connection,-connecting-analytics-cloud-to-your-sites-is-easier-than-ever.-the-latest-dxp-fix-packs-support-this-method'
				)}
			</span>

			<div className='benefits-requirements-container row no-gutters'>
				<div className='col-12'>
					<div className='heading'>{`${Liferay.Language.get(
						'what-to-expect-upon-upgrade'
					)}:`}</div>

					<ul>
						{BENEFITS.map(benefit => (
							<li className='item' key={benefit}>
								<Icon
									className='benefits-check'
									symbol='check-circle'
								/>

								<span>{benefit}</span>
							</li>
						))}
					</ul>
				</div>

				<div className='col-12'>
					<ul>
						{WARNINGS.map(warning => (
							<li className='item' key={warning}>
								<Icon
									className='warning-exclamation'
									symbol='exclamation-circle'
								/>

								<span>{warning}</span>
							</li>
						))}
					</ul>
				</div>
			</div>

			<div className='connect-button-container d-flex justify-content-center'>
				<Button display='primary' onClick={() => onNext()}>
					{Liferay.Language.get('continue-with-upgrade')}
				</Button>
			</div>
		</Modal.Body>
	</div>
);

interface IRequirementsProps {
	onClose: () => void;
}

const Requirements: React.FC<IRequirementsProps> = ({onClose}) => (
	<div className='requirements col-4 d-flex flex-column flex-grow-1'>
		<div className='header d-flex'>
			<div className='title flex-grow-1'>
				{Liferay.Language.get('not-ready-yet')}
			</div>

			<div className='d-flex justify-content-end'>
				<ClayButtonWithIcon
					borderless
					className='close-button'
					displayType='secondary'
					onClick={onClose}
					small
					symbol='times'
				/>
			</div>
		</div>

		<Modal.Body className='d-flex flex-column'>
			<div className='description'>
				{Liferay.Language.get(
					'if-your-dxp-instance-does-not-meet-the-minimum-requirements,-you-must-upgrade-to-the-latest-fix-pack-before-continuing-with-the-connection-upgrade'
				)}
			</div>

			<div className='benefits-requirements-container'>
				<div className='heading'>{`${Liferay.Language.get(
					'dxp-minimum-requirements'
				)}:`}</div>

				<ul>
					{REQUIREMENTS.map(requirement => (
						<li className='item' key={requirement}>
							{requirement}
						</li>
					))}
				</ul>
			</div>

			<div className='requirements-button-container d-flex justify-content-center'>
				<Button onClick={onClose}>
					{Liferay.Language.get('upgrade-later')}
				</Button>
			</div>
		</Modal.Body>
	</div>
);

interface IUpgradeConnectionTypeProps {
	onClose: () => void;
	onNext: (increment: number) => void;
}

const UpgradeConnectionType: React.FC<IUpgradeConnectionTypeProps> = ({
	onClose,
	onNext
}) => (
	<div className='upgrade-connection-type row d-flex flex-grow-1 no-gutters'>
		<TokenConnection onNext={onNext} />

		<Requirements onClose={onClose} />
	</div>
);

export default UpgradeConnectionType;
