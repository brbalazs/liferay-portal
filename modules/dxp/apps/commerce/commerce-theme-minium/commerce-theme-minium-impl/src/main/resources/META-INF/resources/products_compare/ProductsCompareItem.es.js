'use strict';

import template from './ProductsCompareItem.soy';
import Component from 'metal-component';
import Soy, {Config} from 'metal-soy';

class ProductsCompareItem extends Component {

    _hideItem(){
        this.emit('updateProductVisibility', this.id, 'hidden' );
        return setTimeout(
            () => {
                return this.emit(
                    'removeProduct', 
                    {
                        id: this.id
                    }
                );
            },
            300
        );
    }

	_handleRemoveProduct() {
        return this._hideItem();
    }
    
};

Soy.register(ProductsCompareItem, template);

ProductsCompareItem.STATE = {
    thumbnail: Config.string(),
    id: Config.oneOfType(
        [
            Config.string(),
            Config.number()
        ]
    ),
    visibility: Config.oneOf(
        [
            'showing',
            'visible',
            'hiding',
            'hidden'
        ]
    ),
    spritemap: Config.string().required()
};

export {ProductsCompareItem};
export default ProductsCompareItem;