import * as React from 'react';
import {useEffect} from 'react';
import {Container} from "react-bootstrap";
import {Solution, Arrangement} from "./Solution"

interface TablesProps {
    solutions: Array<Solution>
    isLoading: boolean
}

export function Tables(props: TablesProps) {

    useEffect(() => {
        updateLayout();
        window.addEventListener('resize', updateLayout);

        return () => {
            window.removeEventListener('resize', updateLayout)
        }
    });

    function updateLayout() {
        const lists = document.getElementsByClassName("peopleList");
        Array.from(lists).forEach((currentList) => {
            const shape = currentList.classList.contains("Circle") ? "Circle" : "Rectangle";
            const people = currentList.getElementsByClassName("person");
            const height = (currentList as HTMLElement).offsetHeight;
            const width = (currentList as HTMLElement).offsetWidth;
            for (let j = 0; j < people.length; j++) {
                const person = people[j] as HTMLElement;
                person.style.transform = getStyleString(people.length, j, shape, width, height);
            }
        });
    }

    function getStyleString(length: number, index: number, shape: string, width: number, height: number) {
        if (shape === "Circle") {
            const offsetAngle = 360 / length;
            const rotateAngle = offsetAngle * index;
            return "rotate(" + rotateAngle + "deg) translate(0, -" + (width / 2) + "px) rotate(-" + rotateAngle + "deg) translate(-50%, -50%)"
        } else if (shape === "Rectangle") {
            const sideLength = (length - 2) / 2;
            const sideSpaceing = width / sideLength;

            let xTranslation;
            let yTranslation = 0;
            if (index < sideLength) {
                // top row
                yTranslation = -(height / 2);
                xTranslation = (sideSpaceing / 2) + (sideSpaceing * index);
            } else if (index > sideLength && index < length - 1) {
                // bottom row
                xTranslation = (sideSpaceing / 2) + (sideSpaceing * (-index + (2 * sideLength)));
                yTranslation = height / 2;
            } else {
                // ends
                if (index === sideLength) {
                    xTranslation = width / 2;
                } else {
                    xTranslation = -(width / 2);
                }
            }

            if (xTranslation < (width / 2) && xTranslation !== -(width / 2)) {
                xTranslation = -((width / 2) - xTranslation);
            } else if (xTranslation > (width / 2)) {
                xTranslation = xTranslation - (width / 2);
            }

            return "translate(" + xTranslation + "px, " + yTranslation + "px) translate(-50%, -50%)"
        }
        return "translate(-50%, -50%)"
    }

    if (props.isLoading) {
        return <p>Loading ...</p>
    }

    return (
        <Container className="solution text-center" fluid={true}>
            <h2>Solution:</h2>
            {props.solutions.map((solution: Solution, solIndex) =>
                <div key={solIndex} className="solution">
                    {solution.arrangements.map((arrangement: Arrangement, arrIndex) =>
                        <div key={arrIndex} className={"mt-5 mb-5 peopleList " + arrangement.shape}>
                            {arrangement.names.map((name: string, nameIndex) => {
                                return (
                                    <div key={nameIndex} className="person">
                                        Seat {nameIndex}: {name}
                                    </div>
                                )
                            })}
                            <div className="shape">
                                Shape: {arrangement.shape}
                            </div>
                        </div>
                    )}
                    <br/>
                    <div className="answer">
                        Score: {solution.happinessScore}
                    </div>
                </div>
            )}
        </Container>
    )
}