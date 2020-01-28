import * as React from 'react';
import {ChangeEvent, useEffect, useState} from 'react';
import {Container, Form} from "react-bootstrap";

interface Solution {
    arrangements: Array<Arrangement>
    happinessScore: number
}

interface Arrangement {
    shape: string
    names: Array<string>
}

interface Props {
    data: string
    type: string
}

function Solution(props: Props) {
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

    function getStyleString(length: number, index: number, shape: String) {
        if(shape == "Circle"){
            let offsetAngle = 360 /length;
            let rotateAngle = offsetAngle * index;
            return "rotate(" + rotateAngle + "deg) translate(0, -200px) rotate(-" + rotateAngle + "deg) translate(-50%, -50%)"
        } else if(shape == "Rectangle") {

            let sideLength = (length - 2) / 2;
            let sideSpaceing = 600 / sideLength;

            let xTranslation;
            let yTranslation = 0;
            if(index < sideLength) {
                //top row
                yTranslation = -100;
                xTranslation = (sideSpaceing / 2) + (sideSpaceing * index);
            } else if(index > sideLength && index < length - 1) {
                //bottom row
                xTranslation = (sideSpaceing / 2) + (sideSpaceing * (-index + (2 * sideLength)));
                yTranslation = 100;
            } else {
                //ends
                if(index == sideLength) {
                    xTranslation = 300;
                } else {
                    xTranslation = -300;
                }
            }

            if(xTranslation < 300 && xTranslation != -300) {
                xTranslation = -(300 - xTranslation);
            } else if(xTranslation > 300){
                xTranslation = xTranslation - 300;
            }

            return "translate(" + xTranslation + "px, " + yTranslation + "px) translate(-50%, -50%)"
        }
        return "translate(-50%, -50%)"
    }

    if (isLoading) {
        return <p>Loading ...</p>;
    }

    return (
        <Container className="solution text-center" fluid={true}>
            <h2>Solution:</h2>
            {console.log(solutions)}
            {solutions.map((solution: Solution, solIndex) =>
                <div key={solIndex} className="solution">
                    {solution.arrangements.map((arrangement: Arrangement, arrIndex) =>
                        <div key={arrIndex} className={"mt-5 mb-5 peopleList " + arrangement.shape}>
                            {arrangement.names.map((name: String, nameIndex) => {
                                return (
                                    <div key={nameIndex} className="person"
                                         style={{transform: getStyleString(arrangement.names.length, nameIndex, arrangement.shape)}}>
                                        Seat {nameIndex}: {name}
                                    </div>
                                )
                            })}
                            <div className="answer">
                                Shape: {arrangement.shape}
                            </div>
                        </div>
                    )}
                    <div className="answer">
                        Score: {solution.happinessScore}
                    </div>
                </div>
            )}
        </Container>
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
