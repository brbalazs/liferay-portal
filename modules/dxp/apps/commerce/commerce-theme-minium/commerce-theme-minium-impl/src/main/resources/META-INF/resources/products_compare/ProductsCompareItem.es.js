'use strict';

import template from './ProductsCompareItem.soy';
import Component from 'metal-component';
import Soy, {Config} from 'metal-soy';

class ProductsCompareItem extends Component {

    attached(){
        return this._showItem();
    }

    syncVisibility(visibility){
        console.log('visibility', this.id, visibility);
    }

    _showItem(){
        console.log('show', this.id)
        this.emit('updateProductVisibility', this.id, 'visible' );
    }
    
    _hideItem(){
        console.log('hide', this.id)
        this.emit('updateProductVisibility', this.id, 'hidden' );
        return setTimeout(() => {
            return this.emit('removeProduct', this.id);
        }, 300);
    }

	_handleRemoveProduct() {
        return this._hideItem();
	}
};

Soy.register(ProductsCompareItem, template);

ProductsCompareItem.STATE = {
    thumbnail: Config.string(),
    id: Config.oneOfType([
        Config.string(),
        Config.number()
    ]),
    visibility: Config.oneOf([
        'showing',
        'visible',
        'hiding',
        'hidden'
    ])
};

export {ProductsCompareItem};
export default ProductsCompareItem;