import { BrowserRouter, Link, Route, Switch } from 'react-router-dom';
import { Button, Result as ResultComponent } from 'antd';
import RegistrationForm from 'pages/RegistrationForm';
import Result from 'pages/Result';
import './App.css';

function App() {
  return (
    <div className="container">
      <BrowserRouter>
        <Switch>
          <Route exact path="/" component={RegistrationForm} />
          <Route path="/result" component={Result} />
          <Route
            path="*"
            render={(props) => {
              return (
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
              );
            }}
          />
        </Switch>
      </BrowserRouter>
    </div>
  );
}

export default App;
