import Guest from "./Guest";
import Table from "./Table";
import {Button, Divider, Form, Input} from "antd";
import FormItem from "antd/lib/form/FormItem";
import React, {useEffect, useState} from "react";

export function ConfigForm(props) {
    const [currentStep, setCurrentStep] = useState(1);

    const removeGuest = (guestNumber) => {
        const guestss = props.guests.slice();
        props.setGuests([...guestss.slice(0, guestNumber), ...guestss.slice(guestNumber + 1)]);
    };

    useEffect(() => {
        // Remove any relationships other's have with them
        const names = props.guests.map(guest => guest.name.text);
        props.guests.forEach((guest, i) => {
            guest.relationships.forEach((relationship, j) => {
                if((!names.includes(relationship.guestName.text) && relationship.guestName.text !== "")){
                    removeRelationship(i, j);
                }
            })
        });
    }, [props.guests]);

    const removeRelationship = (guestNumber, relationshipNumber) => {
        const guestss = props.guests.slice();
        let guestRelationships = guestss[guestNumber].relationships.slice();
        guestRelationships = [...guestRelationships.slice(0, relationshipNumber), ...guestRelationships.slice(relationshipNumber + 1)];
        guestss[guestNumber].relationships = guestRelationships;
        props.setGuests(guestss);
    };

    const removeTable = (tableNumber) => {
        const tabless = props.tables.slice();
        props.setTables([...tabless.slice(0, tableNumber), ...tabless.slice(tableNumber + 1)])
    };

    const validateName = (nameText) => {
        if (nameText.length === 0) {
            return {
                validateStatus: 'error',
                errorMsg: "Please enter a name for the config"
            }
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null
            }
        }
    };

    const handleNameChange = (event) => {
        const value = event.target.value;
        props.setName({
            text: value,
            ...validateName(value)
        })
    };

    const validateTableShape = (shape) => {
        if (shape === 'Circle' || shape === 'Rectangle') {
            return {
                validateStatus: 'success',
                errorMsg: null
            }
        } else {
            return {
                validateStatus: 'error',
                errorMsg: "A table can only be a circle or Rectangle"
            }
        }
    };

    const validateTableCapacity = (capacity) => {
        if (capacity > 1 && capacity < 20) {
            return {
                validateStatus: 'success',
                errorMsg: null
            }
        } else {
            return {
                validateStatus: 'error',
                errorMsg: "A table can seat 2-20 people"
            }
        }
    };

    const handleGuestNameChange = (event, index) => {
        const guestss = props.guests.slice();
        const value = event.target.value;
        let originalName = guestss[index].name.text;

        guestss.forEach(guest => {
            if(guest.name.text === originalName) {
                guest.name.text = value;
            }
            guest.relationships.forEach(relationship => {
                if(relationship.guestName.text === originalName) {
                    relationship.guestName.text = value;
                }
            })
        });

        props.setGuests(guestss);
    };

    const handleTableShapeChange = (value, index) => {
        const tabless = props.tables.slice();
        let table = tabless[index];
        table.shape = {
            text: value,
            ...validateTableShape(value)
        };
        tabless[index] = {...table};
        props.setTables(tabless);
    };

    const handleTableCapacityChange = (event, index) => {
        const tabless = props.tables.slice();
        const value = event.target.value;
        let table = tabless[index];
        table.capacity = {
            text: value,
            ...validateTableCapacity(value)
        };
        tabless[index] = {...table};
        props.setTables(tabless);
    };

    const handleRelationshipGuestChange = (value, relIndex, guestIndex) => {
        const guestss = props.guests.slice();
        const relationship = guestss[guestIndex].relationships[relIndex];
        relationship.guestName = {
            text: value,
            ...validateRelationshipGuest(value)
        };
        guestss[guestIndex].relationships[relIndex] = {...relationship};
        props.setGuests(guestss);
    };

    const validateRelationshipGuest = (name) => {
        if (name.length === 0) {
            return {
                validateStatus: 'error',
                errorMsg: "Please enter a guest name"
            }
        }

        let containsName = false;
        props.guests.forEach(guest => {
            if (guest.name.text === name) {
                containsName = true;
            }
        });

        if (!containsName) {
            return {
                validateStatus: 'error',
                errorMsg: "Not a valid guest name"
            }
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null
            }
        }
    };

    const handleRelationshipValueChange = (value, relIndex, guestIndex) => {
        const guestss = props.guests.slice();
        const relationship = guestss[guestIndex].relationships[relIndex];
        relationship.likability = {
            text: value,
            ...validateRelationshipValue(value)
        };
        guestss[guestIndex].relationships[relIndex] = {...relationship};
        props.setGuests(guestss);
    };

    const validateRelationshipValue = (value) => {
        if (value == -1 || value == 0 || value == 1 || value == 10) {
            return {
                validateStatus: 'success',
                errorMsg: null
            }
        } else {
            return {
                validateStatus: 'error',
                errorMsg: "Not a valid value"
            }
        }
    };

    const handleRelationshipBribeChange = (event, relIndex, guestIndex) => {
        const guestss = props.guests.slice();
        const value = event.target.value;
        const relationship = guestss[guestIndex].relationships[relIndex];
        relationship.bribe = {
            text: value,
            ...validateBribe(value)
        };
        guestss[guestIndex].relationships[relIndex] = {...relationship};
        props.setGuests(guestss);
    };

    const validateBribe = (value) => {
        if (value >= 0) {
            return {
                validateStatus: 'success',
                errorMsg: null
            }
        } else {
            return {
                validateStatus: 'error',
                errorMsg: "Not a valid bribe"
            }
        }
    };

    const handleNext = () => {
        setCurrentStep(currentStep + 1);
    };

    const handlePrevious = () => {
        setCurrentStep(currentStep - 1);
    };

    return (
        <div className="new-config-container">
            <div className="new-config-content">
                <Form onSubmit={props.handleSubmit} className="create-config-form">
                    <Step1 name={props.name} handleNameChange={handleNameChange} handleNext={handleNext} currentStep={currentStep}/>
                    <Step2 guests={props.guests}
                           addGuest={props.addGuest}
                           removeGuest={removeGuest}
                           handleGuestChange={handleGuestNameChange}
                           handleNext={handleNext}
                           handlePrevious={handlePrevious}
                           currentStep={currentStep}/>
                    <Step3 guests={props.guests}
                           addGuest={props.addGuest}
                           removeGuest={removeGuest}
                           handleGuestChange={handleGuestNameChange}
                           addRelationship={props.addRelationship}
                           removeRelationship={removeRelationship}
                           handleRelationshipGuestChange={handleRelationshipGuestChange}
                           handleRelationshipValueChange={handleRelationshipValueChange}
                           handleRelationshipBribeChange={handleRelationshipBribeChange}
                           handleNext={handleNext}
                           handlePrevious={handlePrevious}
                           currentStep={currentStep}/>
                    <Step4 tables={props.tables}
                           addTable={props.addTable}
                           removeTable={removeTable}
                           handleTableShapeChange={handleTableShapeChange}
                           handleTableCapacityChange={handleTableCapacityChange}
                           handlePrevious={handlePrevious}
                           currentStep={currentStep}/>
                </Form>
            </div>
        </div>
    )
}

function Step1(props) {

    const isFormInvalid = () => {
        return (props.name.validateStatus !== 'success');
    };

    if(props.currentStep !== 1) {
        return null;
    }
    return (
        <div className="step1-container">
            <h1 className="page-title">Create Config</h1>
            <FormItem label="Config name" validateStatus={props.name.validateStatus} help={props.name.errorMsg}
                      className="config-form-row">
                <Input
                    placeholder='Your config name'
                    size="large"
                    onChange={props.handleNameChange}
                    value={props.name.text}/>
            </FormItem>
            <FormItem className="config-form-row next">
                <Button type="primary" onClick={props.handleNext} disabled={isFormInvalid()}>Next</Button>
            </FormItem>
        </div>
    )
}

function Step2(props) {
    const isFormInvalid = () => {
        let invalid = false;
        props.guests.forEach(guest => {
            if(guest.name.text === "") {
                invalid = true
            }
        });

        return invalid;
    };
    if(props.currentStep !== 2) {
        return null;
    }

    const guestViews = [];
    props.guests.forEach((guest, index) => {
        guestViews.push(<Guest
            key={index} guest={guest}
            guestNumber={index}
            removeGuest={props.removeGuest}
            handleGuestChange={props.handleGuestChange}
            guests={props.guests}
        />)
    });
    return (
        <div className="step2-container">
            <h1 className="page-title">Add Guests</h1>
            {guestViews}
            <FormItem className="config-form-row">
                <Button type="dashed" className="create-config-form-button" onClick={props.addGuest}>Add Guest</Button>
            </FormItem>
            <div className="titleBar">
                <FormItem className="config-form-row">
                    <Button type="primary" onClick={props.handlePrevious}>Back</Button>
                </FormItem>
                <FormItem className="config-form-row">
                    <Button type="primary" onClick={props.handleNext} disabled={isFormInvalid()}>Next</Button>
                </FormItem>
            </div>
        </div>
    )
}

function Step3(props) {
    const isFormInvalid = () => {
        let invalid = false;
        props.guests.forEach(guest => {
            guest.relationships.forEach(relationship => {
                if (relationship.guestName.validateStatus && (relationship.guestName.validateStatus !== 'success')) {
                    console.log("rel guest name");
                    invalid = true;
                }

                if (relationship.likability.validateStatus && (relationship.likability.validateStatus !== 'success')) {
                    console.log("rel value");
                    invalid = true;
                }

                if (relationship.bribe.validateStatus && (relationship.bribe.validateStatus !== 'success')) {
                    console.log("rel bribe");
                    invalid = true;
                }
            })
        });

        return invalid;
    };

    if(props.currentStep !== 3) {
        return null;
    }

    const guestViews = [];
    props.guests.forEach((guest, index) => {
        guestViews.push(<Guest
            key={index} guest={guest}
            guestNumber={index}
            removeGuest={props.removeGuest}
            handleGuestChange={props.handleGuestChange}
            addRelationship={props.addRelationship}
            removeRelationship={props.removeRelationship}
            handleRelationshipGuestChange={props.handleRelationshipGuestChange}
            handleRelationshipValueChange={props.handleRelationshipValueChange}
            handleRelationshipBribeChange={props.handleRelationshipBribeChange}
            guests={props.guests}
        />)
    });
    return (
        <div className="step3-container">
            <h1 className="page-title">Add Relationships</h1>
            {guestViews}
            <div className="titleBar">
                <FormItem className="config-form-row">
                    <Button type="primary" onClick={props.handlePrevious}>Back</Button>
                </FormItem>
                <FormItem className="config-form-row">
                    <Button type="primary" onClick={props.handleNext} disabled={isFormInvalid()}>Next</Button>
                </FormItem>
            </div>
        </div>
    )
}

function Step4(props) {
    const isFormInvalid = () => {
        let invalid = false;
        props.tables.forEach(table => {
            if (table.shape.validateStatus && (table.shape.validateStatus !== 'success')) {
                console.log("shape");
                invalid = true;
            }

            if (table.capacity.validateStatus && (table.capacity.validateStatus !== 'success')) {
                console.log("capacity");
                invalid = true;
            }
        });
        return invalid;
    };

    if(props.currentStep !== 4) {
        return null;
    }

    const tableViews = [];
    props.tables.forEach((table, index) => {
        tableViews.push(<Table key={index} table={table} tableNumber={index} removeTable={props.removeTable}
                               handleTableShapeChange={props.handleTableShapeChange}
                               handleTableCapacityChange={props.handleTableCapacityChange}/>)
    });
    return (
        <div className="step4-container">
            <h1 className="page-title">Add Tables</h1>
            {tableViews}
            <FormItem className="config-form-row">
                <Button type="dashed" className="create-config-form-button" onClick={props.addTable}>Add Table</Button>
            </FormItem>
            <div className="titleBar">
                <FormItem className="config-form-row">
                    <Button type="dashed" onClick={props.handlePrevious}>Back</Button>
                </FormItem>
                <FormItem className="config-form-row">
                    <Button type="primary"
                            htmlType="submit"
                            disabled={isFormInvalid()}
                            className="create-config-form-button">Save</Button>
                </FormItem>
            </div>
        </div>
    )
}