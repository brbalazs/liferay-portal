import CopyButton from 'shared/components/CopyButton';
import Input from 'shared/components/Input';
import React from 'react';

interface ICodeSnippet {
	code?: string;
	codeLines?: Array<string>;
}

const CodeSnippet: React.FC<ICodeSnippet> = ({code, codeLines}) => {
	const getDisplayedCode = ([...codeLines]: Array<string>): string => {
		const lastLine = codeLines.pop();
		return codeLines.join('\n\t').concat(`\n${lastLine}`);
	};

	const displayedCode = code || getDisplayedCode(codeLines);

	return (
		<Input.Group className='code-snippet-root'>
			<Input
				className='code-snippet-textarea'
				disabled
				type='textarea'
				value={displayedCode}
			/>

			<CopyButton
				buttonText={Liferay.Language.get('copy')}
				className='copy-button'
				text={displayedCode}
			/>
		</Input.Group>
	);
};

export default CodeSnippet;
