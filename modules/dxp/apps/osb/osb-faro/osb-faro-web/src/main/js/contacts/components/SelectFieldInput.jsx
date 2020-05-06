import * as fieldMappingsAPI from 'shared/api/field-mappings';
import autobind from 'autobind-decorator';
import FaroConstants from 'shared/util/constants';
import React from 'react';
import SelectInput from 'shared/components/SelectInput';
import Sticker from 'shared/components/Sticker';
import TextTruncate from 'shared/components/TextTruncate';
import {PropTypes} from 'prop-types';

const {fieldContexts, fieldTypes} = FaroConstants;

const ITEM_DISPLAY_LANG_MAP = {
	[fieldTypes.date]: Liferay.Language.get('field-avatar-date'),
	[fieldTypes.number]: Liferay.Language.get('field-avatar-number'),
	[fieldTypes.boolean]: Liferay.Language.get('field-avatar-boolean'),
	[fieldTypes.string]: Liferay.Language.get('field-avatar-string')
};

const STICKER_DISPLAY_MAP = {
	[fieldTypes.boolean]: 'boolean',
	[fieldTypes.date]: 'date',
	[fieldTypes.number]: 'number',
	[fieldTypes.string]: 'string'
};

const Item = ({name, rawType}) => (
	<div className='field-display-root'>
		<Sticker circle display={STICKER_DISPLAY_MAP[rawType]} size='sm'>
			{ITEM_DISPLAY_LANG_MAP[rawType]}
		</Sticker>

		<div className='field-details'>
			<TextTruncate className='name' title={name} />
		</div>
	</div>
);

class SelectFieldInput extends React.Component {
	static defaultProps = {
		context: fieldContexts.demographics
	};

	static propTypes = {
		context: PropTypes.string,
		groupId: PropTypes.string.isRequired
	};

	_selectInputRef = React.createRef();

	/**
	 * Public method for focusing the input and triggering the dropdown.
	 */
	focus() {
		this._selectInputRef.current.focus();
	}

	@autobind
	getItems(query) {
		const {context, groupId} = this.props;

		return fieldMappingsAPI
			.search({
				context,
				delta: 10,
				groupId,
				orderByType: '',
				query
			})
			.then(({items}) => items);
	}

	render() {
		return (
			<SelectInput
				{...this.props}
				dataSourceFn={this.getItems}
				itemRenderer={Item}
				placeholder={Liferay.Language.get('select-field')}
				ref={this._selectInputRef}
			/>
		);
	}
}

export default SelectFieldInput;
