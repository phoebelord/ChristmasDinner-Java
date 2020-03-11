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
    const [maxType, setMaxType] = useState("HAPPINESS");
    const [selection, setSelection] = useState("ROULETTE");
    const [crossover, setCrossover] = useState("TwoPoint");

    useEffect(() => {
        fetchSolution();
    }, []);

    const fetchSolution = () => {
        setIsLoading(true);
        getSolution(config.id, maxType, selection, crossover)
            .then(response => {
                setSolution(response);
                setIsLoading(false)
            })
    };

    if (isLoading) {
        return <LoadingIndicator/>
    }

    const handleMaxTypeChange = (e) => {
        setMaxType(e.target.value);
    };

    const handleSelectionChange = (e) => {
        setSelection(e.target.value);
    };

    const handleCrossoverChange = (e) => {
        setCrossover(e.target.value);
    };


    return (
        <div className="solution-container">
            <Tables solution={solution}
                    fetchSolution={fetchSolution}
                    maxType={maxType}
                    handleMaxTypeChange={handleMaxTypeChange}
                    selection={selection}
                    handleSelectionChange={handleSelectionChange}
                    crossover={crossover}
                    handleCrossoverChange={handleCrossoverChange}/>
        </div>
    );
}

export default Solution
