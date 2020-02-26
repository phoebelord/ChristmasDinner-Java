import * as React from 'react';
import {useState} from 'react';
import {Tables} from "./Tables";
import {Button, Form, Input} from "antd";
import {getSolution} from "../utils/ApiUtils";
import {useLocation} from "react-router";
import {useEffect} from "react";
import LoadingIndicator from "../common/LoadingIndicator";

function Solution() {
    const location = useLocation();
    const [config, setConfig] = useState(location.state.config);
    const [solutions, setSolutions] = useState(null);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        fetchSolution();
    }, []);

    const fetchSolution = () => {
        setIsLoading(true);
        getSolution(config.id)
            .then(response => {
                setSolutions(response);
                setIsLoading(false)
            })
    };

    if(isLoading) {
        return <LoadingIndicator/>
    }


    return (
        <div>
            <Tables solutions={solutions}/>
            <Button type="dashed" onClick={fetchSolution}>Try again</Button>
        </div>
    );
}

export default Solution
