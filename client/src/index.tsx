import * as React from 'react';
import * as ReactDOM from 'react-dom';
import './index.css';
import registerServiceWorker from './registerServiceWorker';
import Result from "./App";

ReactDOM.render(
  <Result />,
  document.getElementById('root') as HTMLElement
);
registerServiceWorker();
