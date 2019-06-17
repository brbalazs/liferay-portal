import React from 'react';
import ReactDOM from 'react-dom';
import App from './App.es';

import { StoreProvider } from './components/StoreContext.es';

import apiEndpointDefinitions from '../../../../../../dev/apiEndpointDefinitions';

import 'clay-css/src/scss/atlas.scss';

import '../css/main.scss';

const props = {
    spritemap: '/test-icons.svg',
    basename: '/test',
    modelSelectorMakerEndpoint: apiEndpointDefinitions.MAKER,
    modelSelectorYearEndpoint: apiEndpointDefinitions.YEAR,
    modelSelectorModelEndpoint: apiEndpointDefinitions.MODEL,
    areasEndpoint: apiEndpointDefinitions.AREAS,
    foldersEndpoint: apiEndpointDefinitions.FOLDERS
}

ReactDOM.render(
    <StoreProvider>
        <App {...props} />
    </StoreProvider>, 
    document.getElementById('root')
);
