import * as React from 'react';
import {useEffect} from 'react';
import "./Tables.css"
import {Button, Col, Collapse, Divider, Form, Input, InputNumber, Pagination, Popover, Radio, Row, Select} from "antd";
import {useState} from "react";
import FormItem from "antd/lib/form/FormItem";
const { Panel } = Collapse;
const {Option} = Select;

export function Tables(props) {

    const [maxType, setMaxType] = useState(props.maxType);
    const [currentGeneration, setCurrentGeneration] = useState(props.solutions.length - 1);

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

    const handleChange = (page, size) => {
        setCurrentGeneration(page - 1);
    };

    const validateForm = () => {
        return props.iterations.validateStatus === 'success' &&
            props.selectionSize.validateStatus === 'success' &&
            props.generationSize.validateStatus === 'success' &&
            props.mutationRate.validateStatus === 'success'
    };

    console.log(props.solutions);
    let maxString = maxType.toString().charAt(0) + maxType.toString().slice(1).toLowerCase();
    if (props.solutions) {
        return (
            <div className="tables-container">
                <div className="titleBar">
                    <h2>Gen: {props.solutions[currentGeneration].generationNumber}</h2>
                    <p>{maxString}: {props.solutions[currentGeneration].happinessScore}</p>
                    <div>
                        <Radio.Group onChange={props.handleMaxTypeChange} value={props.maxType} className={"maximisation-buttons"}>
                            <Radio.Button value="HAPPINESS">Happiness</Radio.Button>
                            <Radio.Button value="PROFIT">Profit</Radio.Button>
                        </Radio.Group>
                        <Button type="dashed" onClick={props.fetchSolutions} disabled={!validateForm()}>Try again</Button>
                    </div>
                </div>
                <div className="advanced-settings-container">
                    <Collapse>
                        <Panel header="Advanced Settings">
                            <p>Warning: changing these fields may impact performance!</p>
                            <Form>
                                <Row gutter={[24, 24]}>
                                    <Col span={12}>
                                        <FormItem label="Selection Method">
                                            <Select onChange={props.handleSelectionChange} value={props.selection}>
                                                <Option value={"TOURNAMENT"}>Tournament</Option>
                                                <Option value={"ROULETTE"}>Roulette</Option>
                                            </Select>
                                        </FormItem>
                                    </Col>
                                    <Col span={12}>
                                        <FormItem label="Crossover Method">
                                            <Select onChange={props.handleCrossoverChange} value={props.crossover}>
                                                <Option value={"OnePoint"}>One Point</Option>
                                                <Option value={"TwoPoint"}>Two Point</Option>
                                            </Select>
                                        </FormItem>
                                    </Col>
                                </Row>
                                <Row gutter={[24, 24]}>
                                    <Col span={12}>
                                        <FormItem label="Iterations" validateStatus={props.iterations.validateStatus} help={props.iterations.errorMsg}>
                                            <Input onChange={props.handleIterationsChange} value={props.iterations.value}/>
                                        </FormItem>
                                    </Col>
                                    <Col span={12}>
                                        <FormItem label="Generation Size" validateStatus={props.generationSize.validateStatus} help={props.generationSize.errorMsg}>
                                            <Input onChange={props.handleGenerationSizeChange} value={props.generationSize.value}/>
                                        </FormItem>
                                    </Col>
                                </Row>
                                <Row gutter={[24, 24]}>
                                    <Col span={12}>
                                        <FormItem label="Selection Size" validateStatus={props.selectionSize.validateStatus} help={props.selectionSize.errorMsg}>
                                            <Input onChange={props.handleSelectionSizeChange} value={props.selectionSize.value}/>
                                        </FormItem>
                                    </Col>
                                    <Col span={12}>
                                        <FormItem label="Mutation Rate" validateStatus={props.mutationRate.validateStatus} help={props.mutationRate.errorMsg}>
                                            <Input onChange={props.handleMutationRateChange} value={props.mutationRate.value}/>
                                        </FormItem>
                                    </Col>
                                </Row>
                            </Form>
                        </Panel>
                    </Collapse>
                </div>
                <div className="table">
                    {props.solutions[currentGeneration].arrangements.map((arrangement, arrIndex) =>
                        <div key={arrIndex} className={"mt-5 mb-5 guestList " + arrangement.shape}>
                            {arrangement.guests.map((guest, nameIndex) => {
                                let content = (
                                    <div>
                                        {guest.relationships.map((relationship, relIndex) => {
                                            return (
                                                <p key={relIndex}>{relationship}</p>
                                            )
                                        })}
                                    </div>
                                );
                                return (
                                    <Popover content={content} title={guest.name}>
                                        <div key={nameIndex} className={"guest " + maxString + "-happiness-" + guest.happiness}>
                                            Seat {nameIndex}: {guest.name}
                                        </div>
                                    </Popover>
                                )
                            })}
                            <div className="shape">
                                {maxString}: {arrangement.happiness}
                            </div>
                        </div>
                    )}
                    <br/>
                </div>
                <div className="solutionPicker">
                    <Pagination defaultCurrent={props.solutions.length} total={props.solutions.length} defaultPageSize={1} onChange={handleChange}/>
                </div>
            </div>
        )
    } else {
        return null;
    }
}