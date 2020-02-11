import * as React from 'react';
import Form from "./Form";
import {InputProps} from "./Input";

function Login() {
    const inputs: Array<InputProps> = [{
        name: "email",
        id: "email",
        placeholder: "name@server.com",
        type: "text",
        className: "form-control"
    }, {
        name: "password",
        id: "password",
        type: "password",
        className: "form-control"
    }, {
        type: "submit",
        className: "btn btn-primary",
        value: "Submit"
    }];

    const params = new URLSearchParams(window.location.search);
    const error = params.get('error');

    return (
        <div>
            <Form error={error ? error : ''} name='loginForm' method='POST' action='/perform_login'
                  inputs={inputs}/>
        </div>
    )
}

export default Login