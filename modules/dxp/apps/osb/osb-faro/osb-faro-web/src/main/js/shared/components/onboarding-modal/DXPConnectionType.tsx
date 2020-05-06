import Button from 'shared/components/Button';
import Icon from 'shared/components/Icon';
import Modal from '../modal';
import React from 'react';
import {ClayButtonWithIcon} from '@clayui/button';
import {Routes, toRoute} from 'shared/util/router';

const BENEFITS = [
	Liferay.Language.get('faster-setup'),
	Liferay.Language.get('more-secure'),
	Liferay.Language.get('finer-control-of-your-data')
];

const REQUIREMENTS = [
	`7.2 ${Liferay.Language.get('fix-pack')} 5/SP2`,
	`7.1 ${Liferay.Language.get('fix-pack')} 18/SP4`,
	`7.0 ${Liferay.Language.get('fix-pack')} 90/SP13`
];

interface ITokenConnectionProps {
	onNext: (increment?: number) => void;
}

const TokenConnection: React.FC<ITokenConnectionProps> = ({onNext}) => (
	<div className='token-connection col-8 d-flex flex-column flex-grow-1'>
		<div className='header'>
			<div className='title'>
				{Liferay.Language.get('a-new-way-to-connect-your-dxp-sites')}
			</div>
		</div>

		<Modal.Body className='d-flex flex-column justify-content-between'>
			<span className='description'>
				{Liferay.Language.get(
					'using-our-new-token-based-connection,-connecting-analytics-cloud-to-your-sites-is-easier-than-ever.-the-latest-dxp-fix-packs-support-this-method'
				)}
			</span>

			<div className='benefits-requirements-container row no-gutters'>
				<div className='benefits col-6'>
					<div className='heading'>{`${Liferay.Language.get(
						'benefits'
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

				<div className='requirements col-6'>
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
			</div>

			<div className='connect-button-container d-flex justify-content-center'>
				<Button display='primary' onClick={() => onNext()}>
					{Liferay.Language.get('connect-with-token')}
				</Button>
			</div>
		</Modal.Body>
	</div>
);

interface IOAuthConnectionProps {
	groupId: string;
	onboarding: boolean;
	onClose: () => void;
	onSkip: () => void;
}

const OAuthConnection: React.FC<IOAuthConnectionProps> = ({
	groupId,
	onboarding,
	onClose,
	onSkip
}) => (
	<div className='oauth-connection col-4 d-flex flex-column flex-grow-1'>
		<div className='header d-flex'>
			<div className='title flex-grow-1'>
				{Liferay.Language.get('cant-upgrade')}
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

		<Modal.Body className='d-flex flex-column justify-content-between'>
			<div className='description'>
				{Liferay.Language.get(
					'if-your-dxp-instance-does-not-meet-the-minimum-requirements,-you-must-connect-with-oauth'
				)}

				<div className='secondary-info'>
					{Liferay.Language.get(
						'this-connection-type-will-be-deprecated-in-a-future-analytics-cloud-release'
					)}
				</div>
			</div>

			<div className='connect-button-container d-flex justify-content-center'>
				<Button
					href={toRoute(Routes.SETTINGS_LIFERAY_ADD, {groupId})}
					onClick={onClose}
				>
					{Liferay.Language.get('connect-with-oauth')}
				</Button>
			</div>
		</Modal.Body>

		<Modal.Footer className='d-flex justify-content-end'>
			<Button
				borderless
				className='skip-button'
				display='light'
				onClick={onboarding ? onSkip : onClose}
			>
				{onboarding
					? Liferay.Language.get('skip')
					: Liferay.Language.get('cancel')}
			</Button>
		</Modal.Footer>
	</div>
);

interface IDXPConnectionTypeProps {
	groupId: string;
	onboarding?: boolean;
	onClose: () => void;
	onNext: (increment: number) => void;
}

const DXPConnectionType: React.FC<IDXPConnectionTypeProps> = ({
	groupId,
	onboarding = false,
	onClose,
	onNext
}) => (
	<div className='dxp-connection-type row d-flex flex-grow-1 no-gutters'>
		<TokenConnection onNext={onNext} />

		<OAuthConnection
			groupId={groupId}
			onboarding={onboarding}
			onClose={onClose}
			onSkip={() => onNext(2)}
		/>
	</div>
);

export default DXPConnectionType;
