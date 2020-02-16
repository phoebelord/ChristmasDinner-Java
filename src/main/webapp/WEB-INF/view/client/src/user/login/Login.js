import {Form, notification, Icon, Input, Button} from "antd";
import {login} from "../../utils/ApiUtils";
import {ACCESS_TOKEN} from "../../constants";
import React from "react";
import {Link} from "react-router-dom";
import './Login.css'

function Login(props) {
    const AntWrappedLoginForm = Form.create()(LoginForm);
    return (
        <div className="login-container">
            <h1 className="page-title">Login</h1>
            <div className="login-content">
                <AntWrappedLoginForm onLogin={props.onLogin}/>
            </div>
        </div>
    )
}

function LoginForm(props) {
    const handleSubmit = (event) => {
        event.preventDefault();
        props.form.validateFields((err, values) => {
            if (!err) {
                const loginRequest = Object.assign({}, values);
                login(loginRequest)
                    .then(response => {
                        localStorage.setItem(ACCESS_TOKEN, response.accessToken);
                        props.onLogin();
                    }).catch(error => {
                    if (error.status === 401) {
                        notification.error({
                            message: "Christmas Dinner",
                            description: "Your email address or password is incorrect. Please try again!"
                        });
                    } else {
                        notification.error({
                            message: "Christmas Dinner",
                            description: error.message || "Oops, something went wrong. Please try again!"
                        });
                    }
                });
            }
        });
    };

    const {getFieldDecorator} = props.form;
    return (
        <Form onSubmit={handleSubmit} className="login-form">
            <Form.Item>
                {getFieldDecorator('email', {
                    rules: [{required: true, message: 'Please input your email'}],
                })(
                    <Input
                        prefix={<Icon type="user"/>}
                        size="large"
                        name="email"
                        placeholder="email"
                    />
                )}
            </Form.Item>
            <Form.Item>
                {getFieldDecorator("password", {
                    rules: [{required: true, message: "Please input your password"}],
                })(
                    <Input
                        prefix={<Icon type="lock"/>}
                        size="large"
                        name="password"
                        type="password"
                        placeholder="Password"
                    />
                )}
            </Form.Item>
            <Form.Item>
                <Button type="primary" htmlType="submit" size="large" className="login-form-butto">Login</Button>
                Or <Link to="/signup">register now!</Link>
            </Form.Item>

        </Form>
    )
}

export default Login;