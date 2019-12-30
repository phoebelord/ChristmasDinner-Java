import * as React from 'react';
import {ChangeEvent, useEffect, useState} from 'react';
import {Container, Form} from "react-bootstrap";

interface Solution {
    arrangement: Array<Person>
    happinessScore: number
}

interface Person {
    id: number
    name: string
    relationships: Array<Relationship>
}

interface Relationship {
    personId: number
    likability: number
}

interface Props {
    data: string
}

function Solution(props: Props) {
    const [solutions, setSolutions] = useState([]);
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        setIsLoading(true);
        fetch("http://localhost:8080?dataSet=" + props.data)
            .then(response => response.json())
            .then(data => {
                setSolutions(data);
                setIsLoading(false);
            });
    }, [props.data]);

    if (isLoading) {
        return <p>Loading ...</p>;
    }

    return (
        <Container className="solution" fluid={true}>
            <h2>Solution:</h2>
            {solutions.map((solution: Solution, index) =>
                <div key={index}>
                    {solution.arrangement.map((person: Person, index2) =>
                        <div key={person.id}>
                            Seat {index2}: {person.name}
                        </div>)}
                    <br/>
                    Score: {solution.happinessScore}
                </div>)}
        </Container>
    )
}


function DataSetForm() {
    const [dataSet, setData] = useState("data_a");

    function handleChange(e: ChangeEvent<HTMLInputElement>) {
        setData(e.target.value);
    }

    return (
        <Container fluid={true} className="pt-5 text-center">
            <Form>
                <Form.Group controlId="dataSet">
                    <Form.Label>Dataset:</Form.Label>
                    <Form.Control as="select" onChange={handleChange}>
                        <option value="data_a">A</option>
                        <option value="data_b">B</option>
                        <option value="data_c">C</option>
                        <option value="data_d">D</option>
                        <option value="data_e">E</option>
                        <option value="data_f">F</option>
                    </Form.Control>
                </Form.Group>
            </Form>
            <Solution data={dataSet} />
        </Container>
    );
}

export default DataSetForm
