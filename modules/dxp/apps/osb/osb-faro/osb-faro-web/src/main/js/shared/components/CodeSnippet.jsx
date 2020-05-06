import Clipboard from 'clipboard';
import CopyButton from 'shared/components/CopyButton';
import Input from 'shared/components/Input';
import React from 'react';
import {PropTypes} from 'prop-types';

class CodeSnippet extends React.Component {
	static propTypes = {
		code: PropTypes.string
	};

	componentDidMount() {
		this._clipboard = new Clipboard('[data-clipboard-text]');
	}

	componentWillUnmount() {
		this._clipboard.destroy();
	}

	render() {
		const {code} = this.props;

		return (
			<Input.Group
				className={
					this.props.className ? ` ${this.props.className}` : ''
				}
				className='code-snippet-root'
			>
				<Input disabled type='textarea' value={code} />

				<Input.Inset>
					<CopyButton text={code} />
				</Input.Inset>
			</Input.Group>
		);
	}
}

export default CodeSnippet;
