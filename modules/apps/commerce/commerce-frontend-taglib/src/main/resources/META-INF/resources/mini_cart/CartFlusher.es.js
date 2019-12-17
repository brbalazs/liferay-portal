import Component from 'metal-component';
import Soy, {Config} from 'metal-soy';

import template from './CartFlusher.soy';

class CartFlusher extends Component {
    _handleAskConfirmation(e) {
        e.preventDefault();

        this.isAsking = true;
    }

    _handleCancel() {
        this.isAsking = false;
    }

    _handleConfirm() {
        const products = null,
            summary = {
                discount: null,
                itemsQuantity: 0,
                subtotal: '0',
                total: '0'
            };

        this.emit('deleteAllItems', { products, summary });

        /* TODO Restore once endpoint is correctly set
        fetch(this.apiEndpoint, { method: 'DELETE'})
            .then(response => response.json())
            .then(({success, products, summary}) => {
                this.isAsking = false;

                if (success) {
                    this.emit('deleteAllItems', { products, summary });
                }

                throw new Error('Unable to empty the cart');
            })
            .catch(e => {
                this.isAsking = false;

                console.log(e);
            });
        */
    }
}

Soy.register(CartFlusher, template);

CartFlusher.STATE = {
    apiEndpoint: Config.string().required(),
    isAsking: Config.bool().value(false)
};

export {CartFlusher};
export default CartFlusher;