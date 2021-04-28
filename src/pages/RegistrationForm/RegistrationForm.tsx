import { useState } from 'react';
import { useHistory } from 'react-router-dom';
import { Alert, Button, Col, Form, Input, Row, Typography } from 'antd';
import axios from 'axios';

/* eslint-disable no-template-curly-in-string */
const validateMessages = {
  required: 'Pole ${label} jest wymagane',
  whitespace: 'Pole ${label} nie może być puste',
  types: {
    email: 'Podany email jest nieprawidłowy',
  },
  string: {
    len: '${label} musi mieć dokładnie ${len} znaków',
  },
  pattern: {
    mismatch: '${label} nie pasuje do wzorca',
  },
};

const phoneNumberPattern = /^(?:(?:(?:(?:\+|00)\d{2})?[ -]?(?:(?:\(0?\d{2}\))|(?:0?\d{2})))?[ -]?(?:\d{3}[- ]?\d{2}[- ]?\d{2}|\d{2}[- ]?\d{2}[- ]?\d{3}|\d{7})|(?:(?:(?:\+|00)\d{2})?[ -]?\d{3}[ -]?\d{3}[ -]?\d{3}))$/;

const errorInitState = { isError: false, message: '', description: '' };

const RegistrationForm = () => {
  const [form] = Form.useForm();
  const [isSubmitting, setSubmitting] = useState(false);
  const [error, setError] = useState(errorInitState);
  const history = useHistory();

  const onFinish = (values: any) => {
    setSubmitting(true);
    setError(errorInitState);
    axios
      .post('http://localhost:8080/api/registration/add', values)
      .then(() => {
        setSubmitting(false);
        history.replace({
          pathname: '/result',
          state: {
            status: 'success',
            title: 'Zgłoszenie zostało pomyślnie przesłane',
            subTitle: 'Twoja chęć udziału w szczepieniu przeciw COVID-19 została zgłoszona!',
          },
        });
      })
      .catch((err) => {
        setSubmitting(false);
        if (err.response) {
          if (err.response.data?.errors?.constructor === Object) {
            const { errors } = err.response.data;
            Object.keys(errors).forEach((key: any) => {
              form.setFields([
                {
                  name: key,
                  errors: [errors[key]],
                },
              ]);
            });
          } else if (err.response?.status >= 500 && err.response?.status <= 599) {
            setError({
              isError: true,
              message: `Błąd ${err.response.status}`,
              description: 'Przepraszam, wystąpił wewnętrzny błąd systemu.',
            });
          } else {
            throw err;
          }
        } else {
          setError({
            isError: true,
            message: 'Przesyłanie nie powiodło się!',
            description: 'Przepraszamy, wystąpił nieoczekiwany błąd.',
          });
        }
      });
  };

  return (
    <Row>
      {error.isError && (
        <Col span={24} xs={{ order: 2 }} sm={{ order: 1 }}>
          <Alert
            className="alert"
            message={error.message}
            description={error.description}
            type="error"
            showIcon
          />
        </Col>
      )}
      <Col span={24} xs={{ order: 1 }} sm={{ order: 2 }}>
        <Typography.Title level={2}>Zgłoszenie na szczepienia przeciw COVID-19</Typography.Title>
        <Typography.Text>
          Skorzystaj z naszego formularza i zgłoś chęć zaszczepienia przeciw COVID-19. Jak tylko
          uruchomimy szczepienia dla Twojej grupy wiekowej, priorytetowo zadzwonimy do Ciebie lub
          wskazanej przez Ciebie osoby, aby umówić konkretny termin szczepienia.
        </Typography.Text>

        <Form
          className="mt-3"
          form={form}
          layout="vertical"
          validateMessages={validateMessages}
          onFinish={onFinish}
        >
          <Row gutter={16}>
            <Col xs={24} sm={12}>
              <Form.Item
                label="Imię"
                name="firstName"
                rules={[{ required: true, whitespace: true }]}
              >
                <Input></Input>
              </Form.Item>
            </Col>
            <Col xs={24} sm={12}>
              <Form.Item
                label="Nazwisko"
                name="lastName"
                rules={[{ required: true, whitespace: true }]}
              >
                <Input></Input>
              </Form.Item>
            </Col>
          </Row>
          <Row gutter={16}>
            <Col xs={24} sm={12}>
              <Form.Item
                label="Numer PESEL"
                name="pesel"
                rules={[{ required: true, len: 11, whitespace: true }]}
              >
                <Input></Input>
              </Form.Item>
            </Col>
            <Col xs={24} sm={12}>
              <Form.Item
                label="Adres e-mail"
                name="email"
                rules={[{ required: true, type: 'email' }]}
              >
                <Input type="email"></Input>
              </Form.Item>
            </Col>
          </Row>
          <Row gutter={16}>
            <Col xs={24} sm={12}>
              <Form.Item
                label="Numer telefonu"
                name="phoneNumber"
                rules={[{ required: true, pattern: phoneNumberPattern }]}
              >
                <Input type="tel" placeholder="+48 123 456 789"></Input>
              </Form.Item>
            </Col>
          </Row>

          <Form.Item>
            <Row>
              <Col span={24} style={{ textAlign: 'right' }}>
                <Button loading={isSubmitting} type="primary" htmlType="submit">
                  Wyślij formularz
                </Button>
              </Col>
            </Row>
          </Form.Item>
        </Form>
      </Col>
    </Row>
  );
};

export default RegistrationForm;
