import PropType from 'prop-types';
import React from 'react';

import DefaultSelector from '../../quantity_selector/QuantitySelector.es'

function QuantitySelector(props) {
	return (
        <div className="row">
            <div className="col-auto">
                <DefaultSelector
                    size="small"
                    style="simple"
                    {...props.value}
                />
            </div>
        </div>
	);
}

QuantitySelector.propTypes = {
	value: PropType.object
}

export default QuantitySelector;
