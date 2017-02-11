import React from 'react';
import { render } from 'react-dom';

import { Provider } from 'react-redux';
import { createStore, applyMiddleware } from 'redux';
import { composeWithDevTools } from 'redux-devtools-extension';
import thunk from 'redux-thunk';

import App from './App';
import './index.css';

import combiner from './reducers/combiner';

const store = createStore(combiner, composeWithDevTools(applyMiddleware(thunk)));

render(
    <Provider store={store}>
        <App/>
    </Provider>,
    document.querySelector('#root')
);

