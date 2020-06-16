import BaseSelect from 'shared/components/BaseSelect';
import Input from 'shared/components/Input';
import MetadataTag from './MetadataTag';
import Promise from 'metal-promise';
import React, {useEffect, useRef} from 'react';
import {BACKSPACE} from 'shared/util/key-constants';

interface IStringMatchInputProps {
	metadata: string;
	onMetadataChange: () => void;
	onStringMatchChange: () => void;
	stringMatch: string;
}

const METADATA_TAGS = ['canonicalurl', 'description', 'title', 'url'];

const getMetadataTagsFn = query => {
	return Promise.resolve(
		METADATA_TAGS.filter(val => val.includes(query.toLowerCase()))
	);
};

const StringMatchInput: React.FC<IStringMatchInputProps> = ({
	metadata,
	onBlur,
	onMetadataChange,
	onStringMatchChange,
	stringMatch
}) => {
	const _inputRef = useRef();

	const metadataResults = !!METADATA_TAGS.filter(val =>
		val.includes(stringMatch.toLowerCase())
	).length;

	useEffect(() => {
		if (!!metadata) {
			onStringMatchChange('');
		}
	}, [metadata]);

	useEffect(() => {
		if (!!metadata || !metadataResults) {
			_inputRef.current.focus();
		}
	}, [metadata, stringMatch]);

	return (
		<div>
			{(!!metadata || !metadataResults) && (
				<div className='form-control form-control-tag-group'>
					{!!metadata && <MetadataTag value={metadata} />}

					<input
						className='form-control-inset'
						onChange={event => {
							const {value} = event.target;

							onStringMatchChange(value);
						}}
						onKeyDown={event => {
							const {
								keyCode,
								target: {value}
							} = event;

							if (
								keyCode === BACKSPACE &&
								!stringMatch &&
								!value
							) {
								onMetadataChange('');
							}
						}}
						ref={_inputRef}
						value={stringMatch}
					/>
				</div>
			)}

			{!metadata && metadataResults && (
				<BaseSelect
					className='form-control-inset'
					dataSourceFn={getMetadataTagsFn}
					focusOnInit
					inputValue={stringMatch}
					itemRenderer={value => <MetadataTag value={value} />}
					menuTitle={Liferay.Language.get('available-metadata')}
					onBlur={onBlur}
					onInputValueChange={onStringMatchChange}
					onSelect={onMetadataChange}
				/>
			)}
		</div>
	);
};

export default StringMatchInput;
