import CopyButton from 'shared/components/CopyButton';
import React from 'react';

interface ICodeSnippet {
	code?: string;
	codeLines?: Array<string>;
}

const CodeSnippet: React.FC<ICodeSnippet> = ({code = '', codeLines = ['']}) => {
	const getDisplayedCode = ([...codeLines]: Array<string>): string => {
		const lastLine = codeLines.pop();
		return codeLines.join('\n\t').concat(`\n${lastLine}`);
	};

	const displayedCode = code || getDisplayedCode(codeLines);

	return (
		<div className='code-snippet-root'>
			<CopyButton
				buttonText={Liferay.Language.get('copy')}
				className='copy-button'
				text={displayedCode}
			/>

			<code className='code-container'>{displayedCode}</code>
		</div>
	);
};

export default CodeSnippet;
