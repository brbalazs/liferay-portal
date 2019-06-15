import React from 'react';
import ReactDOM from 'react-dom';
import App from './App.es';

import { StoreProvider } from './components/StoreContext.es';

import 'clay-css/src/scss/atlas.scss';

import '../css/main.scss';

const fakeData = {
    id: 'adminPartFinder',
    spritemap: '/test-icons.svg',
    areaId: 'asd',
    areaApiUrl: 'http://localhost:4000/api/car-parts/area',
    productApiUrl: 'http://localhost:4000/api/products',
}

ReactDOM.render(
    <StoreProvider>
        <App {...fakeData} />
    </StoreProvider>, 
    document.getElementById('root')
);
