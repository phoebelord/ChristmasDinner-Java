import * as React from 'react';
import './App.css';
import {useEffect, useState} from "react";

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

function Result() {
    const [solutions, setSolutions] = useState([]);
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        setIsLoading(true);
        fetch('http://localhost:8080')
            .then(response => response.json())
            .then(data => {
                setSolutions(data);
                setIsLoading(false);
            });
    }, []);

    if (isLoading) {
        return <p>Loading ...</p>;
    }

    return (
        <div className="App">
            <div>
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
            </div>
        </div>
    )
}

export default Result;
