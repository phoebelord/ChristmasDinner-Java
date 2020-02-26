import * as React from 'react';
import {useEffect} from 'react';
import "./Tables.css"
import {Button} from "antd";

export function Tables(props) {

    useEffect(() => {
        updateLayout();
        window.addEventListener('resize', updateLayout);

        return () => {
            window.removeEventListener('resize', updateLayout)
        }
    });

    function updateLayout() {
        const lists = document.getElementsByClassName("guestList");
        Array.from(lists).forEach((currentList) => {
            const shape = currentList.classList.contains("Circle") ? "Circle" : "Rectangle";
            const guests = currentList.getElementsByClassName("guest");
            const height = (currentList).offsetHeight;
            const width = (currentList).offsetWidth;
            for (let j = 0; j < guests.length; j++) {
                const guest = guests[j];
                guest.style.transform = getStyleString(guests.length, j, shape, width, height);
            }
        });
    }

    function getStyleString(length, index, shape, width, height) {
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

    if (props.solution) {
        return (
            <div className="tables-container">
                <div className="titleBar">
                    <h2>Solution:</h2>
                    <p>Score: {props.solution.happinessScore}</p>
                    <Button type="dashed" onClick={props.fetchSolution}>Try again</Button>
                </div>
                <div className="table">
                    {props.solution.arrangements.map((arrangement, arrIndex) =>
                        <div key={arrIndex} className={"mt-5 mb-5 guestList " + arrangement.shape}>
                            {arrangement.names.map((name, nameIndex) => {
                                return (
                                    <div key={nameIndex} className="guest">
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
                </div>

            </div>
        )
    } else {
        return null;
    }


}