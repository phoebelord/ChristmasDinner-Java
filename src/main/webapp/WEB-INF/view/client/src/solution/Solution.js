import * as React from 'react';
import {useState} from 'react';
import {Tables} from "./Tables";
import {Button, Form, Input} from "antd";
import FormItem from "antd/es/form/FormItem";
import {getSolution} from "../utils/ApiUtils";

function Solution() {
    const [configId, setConfigId] = useState(null);
    const [solutions, setSolutions] = useState(null);

    const handleSubmit = (e) => {
        e.preventDefault();
        getSolution(configId)
            .then(response => {
                setSolutions(response)
            })
    };

    const handleIdChange = (e) => {
        setConfigId(e.target.value)
    };

    return (
        <div>
            <Form onSubmit={handleSubmit}>
                <FormItem label="configId">
                    <Input
                        placeholder="1"
                        size="large"
                    onChange={handleIdChange}/>
                </FormItem>
                <FormItem>
                    <Button type="primary"
                            htmlType="submit"
                            size="large">Submit</Button>
                </FormItem>
            </Form>
            <Tables solutions={solutions}/>
        </div>
    );
}

export default Solution
