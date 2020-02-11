import * as React from 'react';
import {createRef, FormEvent, useState} from "react";
import Input, {InputProps} from "./Input";
import { useHistory } from 'react-router-dom'

interface FormProps {
    error?: string
    inputs: Array<InputProps>
    name: string
    method: string
    action: string
}

function Form(props: FormProps) {
    const history = useHistory();
    const [failure] = useState(props.error ? "Wrong email or password" : "");
    const [errCount] = useState(0);
    const [errmsgs] = useState([]);
    const formRef = createRef<HTMLFormElement>();

    const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const form = formRef.current;
        if(!errCount && form) {
            const data = new FormData(form);
            fetch(form.action, {
                method: form.method,
                body: new URLSearchParams(data as any)
            }).then(v => {
                if(v.redirected) {
                    if(!v.url.includes("error")) {
                        console.log("we're logged in!");
                        history.push('/homepage')
                    } else {
                        window.location.href = v.url;
                    }
                }
            }).catch(exception => console.warn(exception))
        }
    };

    // const handleError = (field: string, errmsg: string) => {
    //     if(!field) {
    //         return
    //     }
    //
    //     if(errmsg) {
    //         setFailure("");
    //         setErrCount(errCount + 1);
    //         setErrmsgs({...errmsgs, [field]: errmsg})
    //     } else {
    //         setFailure("");
    //         setErrCount(errCount === 1 ? 0 : errCount - 1);
    //         setErrmsgs({...errmsgs, [field]: ''})
    //     }
    // };

    const renderError = () => {
        if(errCount || failure) {
            const errmsg = failure || Object.values(errmsgs).find(v=>v);
            console.log(`error: ${errmsg}`);
            return <div className="error">{errmsg}</div>
        } else {
            return ""
        }
    };

    const inputs = props.inputs.map(
        ({name, placeholder, type, value, className}, index) => (
            <div className="form-group row" key={index}>
                <label className="col-form-label" htmlFor={name}>{name}</label>
                <Input name={name} placeholder={placeholder} type={type} value={value} className={className} />
            </div>

        )
    );

    const errors = renderError();

    return (
        <form ref={formRef} onSubmit={handleSubmit} {...props}>
            {inputs}
            {errors}
        </form>
    )
}

export default Form;