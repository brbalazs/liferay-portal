import React, { useState } from 'react';
import PropTypes from 'prop-types';
import classNames from 'classnames';
import ClayList from '@clayui/list';
import {ClayRadio} from '@clayui/form';

function SelectableItemsList(props) {
    const [currentValue, setCurrentValue] = useState(props.selectedItemValue);

    return (
        <ClayList>
            <input hidden name="selectedIds" readOnly value={currentValue}/>
            {props.items.map((item, i) => (
                <ClayList.Item  className={classNames(i ? 'border-left-0 border-bottom-0 border-right-0' : 'border-0')} flex key={item.id}>
                    {props.schema.radioValue && (
                        <ClayList.ItemField>
                                <ClayRadio
                                    checked={item[props.schema.radioValue] == currentValue}
                                    onChange={(e) => setCurrentValue(e.target.value)}
                                    value={item[props.schema.radioValue]}
                                />
                        </ClayList.ItemField>
                    )}
                    <ClayList.ItemField expand>
                        {props.schema.title && (
                            <ClayList.ItemTitle>{item[props.schema.title]}</ClayList.ItemTitle>
                        )}
                        {props.schema.description && (
                            <ClayList.ItemText>{item[props.schema.description]}</ClayList.ItemText>
                        )}
                    </ClayList.ItemField>
                </ClayList.Item>
            ))}
        </ClayList>
    )
}

SelectableItemsList.propTypes = {
    items: PropTypes.arrayOf(
        PropTypes.shape({
            id: PropTypes.oneOfType([
                PropTypes.string,
                PropTypes.number
            ]).isRequired
        })
    ),
    schema: PropTypes.shape({
        description: PropTypes.string,
        radioValue: PropTypes.string,
        title: PropTypes.string,
    }),
    selectedItemValue: PropTypes.oneOfType([
        PropTypes.number,
        PropTypes.string,
    ])
}

SelectableItemsList.defaultTypes = {
    selectedItemValue: ''
}

export default SelectableItemsList;