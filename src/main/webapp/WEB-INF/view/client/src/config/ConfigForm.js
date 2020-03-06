import Guest from "./Guest";
import Table from "./Table";
import {Button, Divider, Form, Input} from "antd";
import FormItem from "antd/lib/form/FormItem";
import React, {useEffect, useState} from "react";

export function ConfigForm(props) {

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
        if (value == -1 || value == 1 || value == 10) {
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

    const isFormInvalid = () => {
        let invalid = (props.name.validateStatus !== 'success');
        console.log(invalid);
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

    const guestViews = [];
    props.guests.forEach((guest, index) => {
        guestViews.push(<Guest
            key={index} guest={guest}
            guestNumber={index}
            removeGuest={removeGuest}
            handleGuestChange={handleGuestNameChange}
            addRelationship={props.addRelationship}
            removeRelationship={removeRelationship}
            handleRelationshipGuestChange={handleRelationshipGuestChange}
            handleRelationshipValueChange={handleRelationshipValueChange}
            handleRelationshipBribeChange={handleRelationshipBribeChange}
            guests={props.guests}
        />)
    });

    const tableViews = [];
    props.tables.forEach((table, index) => {
        tableViews.push(<Table key={index} table={table} tableNumber={index} removeTable={removeTable}
                               handleTableShapeChange={handleTableShapeChange}
                               handleTableCapacityChange={handleTableCapacityChange}/>)
    });

    return (
        <div className="new-config-container">
            <h1 className="page-title">Create Config</h1>
            <div className="new-config-content">
                <Form onSubmit={props.handleSubmit} className="create-config-form">
                    <FormItem label="Config name" validateStatus={props.name.validateStatus} help={props.name.errorMsg}
                              className="config-form-row">
                        <Input
                            placeholder='Your config name'
                            size="large"
                            onChange={handleNameChange}
                            value={props.name.text}/>
                    </FormItem>
                    <Divider/>
                    {guestViews}
                    <FormItem className="config-form-fow">
                        <Button type="dashed" onClick={props.addGuest}>Add Guest</Button>
                    </FormItem>
                    {tableViews}
                    <FormItem className="config-form-fow">
                        <Button type="dashed" onClick={props.addTable}>Add Table</Button>
                    </FormItem>
                    <FormItem className="config-form-row">
                        <Button type="primary"
                                htmlType="submit"
                                size="large"
                                disabled={isFormInvalid()}
                                className="create-config-form-button">Save Config</Button>
                    </FormItem>
                </Form>
            </div>
        </div>
    )
}