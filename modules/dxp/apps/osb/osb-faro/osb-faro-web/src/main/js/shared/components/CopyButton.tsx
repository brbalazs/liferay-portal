import Button, {Displays} from 'shared/components/Button';
import Clipboard from 'clipboard';
import Icon from 'shared/components/Icon';
import React, {useEffect} from 'react';

const CopyButton: React.FC<{
	display?: Displays;
	onClick?: (any) => void;
	position?: string;
	text: string;
}> = ({display, onClick, text, ...otherProps}) => {
	useEffect(() => {
		const _clipboard = new Clipboard('[data-clipboard-text]');

		return () => {
			_clipboard.destroy();
		};
	}, []);

	return (
		<Button
			aria-label={Liferay.Language.get('click-to-copy')}
			data-clipboard-text={text}
			data-tooltip-response={Liferay.Language.get('copied')}
			display={display}
			onClick={onClick}
			title={Liferay.Language.get('click-to-copy')}
			{...otherProps}
		>
			<Icon symbol='paste' />
		</Button>
	);
};

export default CopyButton;
