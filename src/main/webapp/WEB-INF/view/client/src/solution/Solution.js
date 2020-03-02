import * as React from 'react';
import {useEffect, useState} from 'react';
import {Tables} from "./Tables";
import {getSolution} from "../utils/ApiUtils";
import {useLocation} from "react-router";
import LoadingIndicator from "../common/LoadingIndicator";
import "./Solution.css";

function Solution() {
    const location = useLocation();
    const [config, setConfig] = useState(location.state.config);
    const [solution, setSolution] = useState(null);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        fetchSolution();
    }, []);

    const fetchSolution = () => {
        setIsLoading(true);
        getSolution(config.id)
            .then(response => {
                setSolution(response);
                setIsLoading(false)
            })
    };

    if (isLoading) {
        return <LoadingIndicator/>
    }


    return (
        <div className="solution-container">
            <Tables solution={solution} fetchSolution={fetchSolution}/>
        </div>
    );
}

export default Solution
