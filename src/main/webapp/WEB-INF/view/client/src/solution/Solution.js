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
    const [solutions, setSolutions] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [maxType, setMaxType] = useState("HAPPINESS");
    const [selection, setSelection] = useState("ROULETTE");
    const [crossover, setCrossover] = useState("TwoPoint");
    const [iterations, setIterations] = useState({value: 1000, validateStatus: 'success'});
    const [selectionSize, setSelectionSize] = useState({value: 50, validateStatus: 'success'});
    const [generationSize, setGenerationSize] = useState({value: 100, validateStatus: 'success'});
    const [mutationRate, setMutationRate] = useState({value: 0.01, validateStatus: 'success'});

    useEffect(() => {
        fetchSolutions();
    }, []);

    const fetchSolutions = () => {
        setIsLoading(true);
        getSolution(config.id, maxType, selection, crossover, iterations.value, selectionSize.value, generationSize.value, mutationRate.value)
            .then(response => {
                console.log(response);
                setSolutions(response);
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
        setSelection(e);
    };

    const handleCrossoverChange = (e) => {
        setCrossover(e);
    };

    const validateIterations = (val) => {
        if(val > 100000) {
            return {
                validateStatus: 'error',
                errorMsg: "Iterations too large"
            }
        } else if(val <= 0) {
            return {
                validateStatus: 'error',
                errorMsg: "Iterations must be greater than 0"
            }
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null
            }
        }
    };

    const handleIterationsChange = (e) => {
        setIterations({
            value: e.target.value,
            ...validateIterations(parseInt(e.target.value))
        })
    };

    const validateSelectionSize = (val) => {
        if(val >= generationSize.value) {
            return {
                validateStatus: 'error',
                errorMsg: "Selection size cannot be bigger than generation size"
            }
        } else if(val <= 0) {
            return {
                validateStatus: 'error',
                errorMsg: "Selection size must be greater than 0"
            }
        } else if(val % 2 === 1) {
            return {
                validateStatus: 'error',
                errorMsg: "Selection size must be even"
            }
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null
            }
        }
    };

    const handleSelectionSizeChange = (e) => {
        setSelectionSize({
            value: e.target.value,
            ...validateSelectionSize(parseInt(e.target.value))
        })
    };

    const validateGenerationsSize = (val) => {
        if(val <= 0) {
            return {
                validateStatus: 'error',
                errorMsg: "Generation size must be greater than 0"
            }
        } else if(val % 2 === 1) {
            return {
                validateStatus: 'error',
                errorMsg: "Generation size must be even"
            }
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null
            }
        }
    };

    const handleGenerationSizeChange = (e) => {
        setGenerationSize({
            value: e.target.value,
            ...validateGenerationsSize(parseInt(e.target.value))
        })
    };

    const validateMutationRate = (val) => {
        if(val > 1 || val < 0) {
            return {
                validateStatus: 'error',
                errorMsg: "Mutation rate must be between 0 - 1"
            }
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null
            }
        }
    };

    const handleMutationRateChange = (e) => {
        setMutationRate({
            value: e.target.value,
            ...validateMutationRate(parseFloat(e.target.value))
        })
    };


    return (
        <div className="solution-container">
            <Tables solutions={solutions}
                    fetchSolutions={fetchSolutions}
                    maxType={maxType}
                    handleMaxTypeChange={handleMaxTypeChange}
                    selection={selection}
                    handleSelectionChange={handleSelectionChange}
                    crossover={crossover}
                    handleCrossoverChange={handleCrossoverChange}
                    iterations={iterations}
                    handleIterationsChange={handleIterationsChange}
                    selectionSize={selectionSize}
                    handleSelectionSizeChange={handleSelectionSizeChange}
                    generationSize={generationSize}
                    handleGenerationSizeChange={handleGenerationSizeChange}
                    mutationRate={mutationRate}
                    handleMutationRateChange={handleMutationRateChange} />
        </div>
    );
}

export default Solution
