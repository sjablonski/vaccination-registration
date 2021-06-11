import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter, Link, Route, Switch } from 'react-router-dom';
import { Button, Result as ResultComponent } from 'antd';
import RegistrationForm from 'pages/RegistrationForm';
import Result from 'pages/Result';
import './index.css';
import reportWebVitals from './reportWebVitals';

ReactDOM.render(
  <React.StrictMode>
    <div className="container">
      <BrowserRouter>
        <Switch>
          <Route exact path="/" component={RegistrationForm} />
          <Route path="/result" component={Result} />
          <Route
            path="*"
            render={() => (
              <ResultComponent
                status="404"
                title="404"
                subTitle="Przepraszamy, odwiedzona strona nie istnieje."
                extra={
                  <Link to={{ pathname: '/' }}>
                    <Button type="primary">Powrót do strony głównej</Button>
                  </Link>
                }
              />
            )}
          />
        </Switch>
      </BrowserRouter>
    </div>
  </React.StrictMode>,
  document.getElementById('root'),
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
