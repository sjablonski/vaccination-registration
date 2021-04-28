import { Button, Result as ResultComponent } from 'antd';
import { Redirect, useHistory } from 'react-router';

const Result = ({ location }: any) => {
  const history = useHistory();

  const { status, title, subTitle } = (location && location.state) || {};
  if (status || title || subTitle) {
    return (
      <ResultComponent
        status={status}
        title={title}
        subTitle={subTitle}
        extra={[
          <Button type="primary" key="console" onClick={() => history.replace('/', {})}>
            Powrót
          </Button>,
        ]}
      />
    );
  } else {
    return <Redirect to={{ pathname: '/' }} />;
  }
};

export default Result;
