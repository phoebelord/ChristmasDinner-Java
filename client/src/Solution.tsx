import * as React from 'react';
import {ChangeEvent, useEffect, useState} from 'react';
import {Container, Form} from "react-bootstrap";
import {Tables} from "./Tables";

export interface Solution {
    arrangements: Array<Arrangement>
    happinessScore: number
}

export interface Arrangement {
    shape: string
    names: Array<string>
}

interface SolutionProps {
    data: string
    type: string
}

function Solution(props: SolutionProps) {
    const [solutions, setSolutions] = useState([]);
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        setIsLoading(true);
        fetch("http://localhost:8080?dataSet=" + props.data + "&algorithmType=" + props.type)
            .then(response => response.json())
            .then(data => {
                setSolutions(data);
                setIsLoading(false);
            });
    }, [props.data, props.type]);


    return (
        <Tables solutions={solutions} isLoading={isLoading}/>
    )
}


function DataSetForm() {
    const [dataSet, setData] = useState("data_a");
    const [algorithmType, setType] = useState("Genetic");

    function handleDataChange(e: ChangeEvent<HTMLInputElement>) {
        setData(e.target.value);
    }

    function handleAlgorithmChange(e: ChangeEvent<HTMLInputElement>) {
        setType(e.target.value);
    }

    return (
        <Container fluid={true} className="pt-5 text-center">
            <Form>
                <Form.Group controlId="dataSet">
                    <Form.Label>Dataset:</Form.Label>
                    <Form.Control as="select" onChange={handleDataChange}>
                        <option value="data_a">A</option>
                        <option value="data_b">B</option>
                        <option value="data_c">C</option>
                        <option value="data_d">D</option>
                        <option value="data_e">E</option>
                        <option value="data_f">F</option>
                    </Form.Control>
                </Form.Group>
                <Form.Group controlId="algorithmType">
                    <Form.Label>Algorithm Type:</Form.Label>
                    <Form.Control as="select" onChange={handleAlgorithmChange}>
                        <option value="Genetic">Genetic</option>
                        <option value="Naive">Naive</option>
                        <option value="BnB">BnB</option>
                    </Form.Control>
                </Form.Group>
            </Form>
            <Solution data={dataSet} type={algorithmType}/>
        </Container>
    );
}

export default DataSetForm
