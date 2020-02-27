import React, {useState} from "react";
import {useHistory} from 'react-router-dom';
import {Button, Divider, Form, Icon, Input, notification} from "antd";
import {createConfig, editConfig} from "../utils/ApiUtils";
import FormItem from "antd/es/form/FormItem";
import "./NewConfig.css"
import {useLocation} from "react-router";

function NewConfig(props) {
    const history = useHistory();
    const location = useLocation();
    const [name, setName] = useState(location.state ? {text: location.state.config.name} : {text: ''});
    const [guests, setGuests] = useState(location.state ? location.state.config.guests : []);
    const [tables, setTables] = useState(location.state ? location.state.config.tables : []);

    const addGuest = () => {
        const guestss = guests.slice();
        setGuests(guestss.concat([{
            name: '',
            relationships: []
        }]));
    };

    const removeGuest = (guestNumber) => {
        const guestss = guests.slice();
        setGuests([...guestss.slice(0, guestNumber), ...guestss.slice(guestNumber + 1)])
    };

    const addRelationship = (guestNumber) => {
        const guestss = guests.slice();
        const guestRelationships = guests[guestNumber].relationships.slice();
        guestRelationships.push({
            guestName: "",
            likability: 0,
            bribe: 0
        });
        guestss[guestNumber].relationships = guestRelationships;
        setGuests(guestss);
    };

    const removeRelationship = (guestNumber, relationshipNumber) => {
        const guestss = guests.slice();
        let guestRelationships = guestss[guestNumber].relationships.slice();
        guestRelationships = [...guestRelationships.slice(0, relationshipNumber), ...guestRelationships.slice(relationshipNumber + 1)];
        guestss[guestNumber].relationships = guestRelationships;
        setGuests(guestss);
    };

    const addTable = () => {
        const tabless = tables.slice();
        setTables(tabless.concat([{
            shape: '',
            capacity: null
        }]));
    };

    const removeTable = (tableNumber) => {
        const tabless = tables.slice();
        setTables([...tabless.slice(0, tableNumber), ...tabless.slice(tableNumber + 1)])
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        const configData = {
            id: location.state ? location.state.config.id : '',
            name: name.text,
            guests: guests.map(guest => {
                return {
                    name: guest.name,
                    relationships: guest.relationships.map(relationship => {
                        return {
                            guestName: relationship.guestName,
                            likability: relationship.likability,
                            bribe: relationship.bribe
                        }
                    })
                }
            }),
            tables: tables.map(table => {
                return {
                    shape: table.shape,
                    capacity: table.capacity
                }
            })
        };

        console.log(configData);
        if(location.state) {
            editConfig(configData)
                .then(response => {
                    history.push("/");
                    notification.success({
                        message: "Christmas Dinner",
                        description: "Config successfully edited."
                    });
                }).catch(error => {
                if (error.status === 401) {
                    props.handleLogout('/signin', 'error', 'You have been logged out. Sign in to create a config');
                } else {
                    notification.error({
                        message: "Christmas Dinner",
                        description: error.message || "Oops, something went wrong. Please try again!"
                    })
                }
            })
        } else {
            createConfig(configData)
                .then(response => {
                    history.push("/");
                    notification.success({
                        message: "Christmas Dinner",
                        description: "Config successfully created."
                    });
                }).catch(error => {
                if (error.status === 401) {
                    props.handleLogout('/signin', 'error', 'You have been logged out. Sign in to create a config');
                } else {
                    notification.error({
                        message: "Christmas Dinner",
                        description: error.message || "Oops, something went wrong. Please try again!"
                    })
                }
            })
        }

    };

    const validateName = (nameText) => {
        if (nameText.length === 0) {
            return {
                validateStatus: 'error',
                errorMsg: "Please enter a name for the config"
            }
        }
    };

    const handleNameChange = (event) => {
        const value = event.target.value;
        setName({
            text: value,
            ...validateName(value)
        })
    };

    const validateGuest = (guestName) => {
        if (guestName.length === 0) {
            return {
                validateStatus: 'error',
                errorMsg: "Please enter a name for the guest"
            }
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null
            }
        }
    };

    const validateTableShape = (shape) => {
        if(shape === 'Circle' || shape === 'Rectangle') {
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
        if(capacity > 1 && capacity < 20) {
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

    const handleGuestChange = (event, index) => {
        const guestss = guests.slice();
        const value = event.target.value;
        let guest = guestss[index];
        guest.name = value;
        guestss[index] = {...guest, ...validateGuest(value)};
        setGuests(guestss);
    };

    const handleTableShapeChange = (event, index) => {
        const tabless = tables.slice();
        const value = event.target.value;
        let table = tabless[index];
        table.shape = value;
        tabless[index] = {...table, ...validateTableShape(value)};
        setTables(tabless);
    };

    const handleTableCapacityChange = (event, index) => {
        const tabless = tables.slice();
        const value = event.target.value;
        let table = tabless[index];
        table.capacity = value;
        tabless[index] = {...table, ...validateTableCapacity(value)};
        setTables(tabless);
    };

    const handleRelationshipGuestChange = (event, relIndex, guestIndex) => {
        const guestss = guests.slice();
        const value = event.target.value;
        const relationship = guestss[guestIndex].relationships[relIndex];
        relationship.guestName = value;
        guestss[guestIndex].relationships[relIndex] = {...relationship, ...validateRelationshipGuest(value)};
        setGuests(guestss);
    };

    const validateRelationshipGuest = (name) => {
        if(name.length === 0) {
            return {
                validateStatus: 'error',
                errorMsg: "Please enter a guest name"
            }
        }

        let containsName = false;
        guests.forEach(guest => {
            if(guest.name === name) {
                containsName = true;
            }
        });

        if(!containsName) {
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

    const handleRelationshipValueChange = (event, relIndex, guestIndex) => {
        const guestss = guests.slice();
        const value = event.target.value;
        const relationship = guestss[guestIndex].relationships[relIndex];
        relationship.likability = value;
        guestss[guestIndex].relationships[relIndex] = {...relationship, ...validateRelationshipValue(value)};
        setGuests(guestss);
    };

    const validateRelationshipValue = (value) => {
        if(value == -1 || value == 1 || value == 10) {
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
        const guestss = guests.slice();
        const value = event.target.value;
        const relationship = guestss[guestIndex].relationships[relIndex];
        relationship.bribe = value;
        guestss[guestIndex].relationships[relIndex] = {...relationship, ...validateBribe(value)};
        setGuests(guestss);
        console.log(guests);
    };

    const validateBribe = (value) => {
        if(value >= 0) {
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
        return name.validateStatus !== 'success';
    };

    const guestViews = [];
    guests.forEach((guest, index) => {
        guestViews.push(<Guest
            key={index} guest={guest}
            guestNumber={index}
            removeGuest={removeGuest}
            handleGuestChange={handleGuestChange}
            addRelationship={addRelationship}
            removeRelationship={removeRelationship}
            handleRelationshipGuestChange={handleRelationshipGuestChange}
            handleRelationshipValueChange={handleRelationshipValueChange}
            handleRelationshipBribeChange={handleRelationshipBribeChange}
        />)
    });

    const tableViews = [];
    tables.forEach((table, index) => {
        tableViews.push(<Table key={index} table={table} tableNumber={index} removeTable={removeTable} handleTableShapeChange={handleTableShapeChange} handleTableCapacityChange={handleTableCapacityChange}/>)
    });

    return (
        <div className="new-config-container">
            <h1 className="page-title">Create Config</h1>
            <div className="new-config-content">
                <Form onSubmit={handleSubmit} className="create-config-form">
                    <FormItem label="Config name" validateStatus={name.validateStatus} help={name.errorMsg} className="config-form-row">
                        <Input
                            placeholder='Your config name'
                            size="large"
                            onChange={handleNameChange}
                            value={name.text}/>
                    </FormItem>
                    <Divider />
                    {guestViews}
                    <FormItem className="config-form-fow">
                        <Button type="dashed" onClick={addGuest}>Add Guest</Button>
                    </FormItem>
                    {tableViews}
                    <FormItem className="config-form-fow">
                        <Button type="dashed" onClick={addTable}>Add Table</Button>
                    </FormItem>
                    <FormItem className="config-form-row">
                        <Button type="primary"
                                htmlType="submit"
                                size="large"
                                disabled={isFormInvalid}
                                className="create-config-form-button">Save Config</Button>
                    </FormItem>
                </Form>
            </div>
        </div>
    )
}

function Guest(props) {
    const relationshipViews = [];
    props.guest.relationships.forEach((relationship, index) => {
        relationshipViews.push(<Relationship
            key={index}
            relationship={relationship}
            relationshipNumber={index}
            guestNumber={props.guestNumber}
            removeRelationship={props.removeRelationship}
            handleRelationshipGuestChange={props.handleRelationshipGuestChange}
            handleRelationshipValueChange={props.handleRelationshipValueChange}
            handleRelationshipBribeChange={props.handleRelationshipBribeChange}
            addRelationship={props.addRelationship}/>)
    });

    return (
        <FormItem label="Guest Name" validateStatus={props.guest.validateStatus} help={props.guest.errorMsg} className="config-form-row">
            <Input
                placeholder={'Guest ' + (props.guestNumber + 1)}
                size="large"
                className="optional-guest"
                onChange={(event) => props.handleGuestChange(event, props.guestNumber)}
                value={props.guest.name}/>
             {relationshipViews}
            <FormItem className="config-form-fow">
                <Button type="dashed" onClick={() => props.addRelationship(props.guestNumber)}>Add Relationship</Button>
            </FormItem>
            <Icon className="dynamic-delete-button"
                  type="close"
                  onClick={() => props.removeGuest(props.guestNumber)} />
            <Divider />
        </FormItem>
    )
}

function Table(props) {
    return (
        <div>
            <h1>Table:</h1>
            <FormItem label="Table Shape" validateStatus={props.table.validateStatus} help={props.table.errorMsg} className="config-form-row indent">
                <Input
                    placeholder="Circle"
                    size="large"
                    className="optional-table"
                    onChange={(event) => props.handleTableShapeChange(event, props.tableNumber)}
                    value={props.table.shape}/>
            </FormItem>
            <FormItem label="Table Capacity" validateStatus={props.table.validateStatus} help={props.table.errorMsg} className="config-form-row indent">
                <Input
                    placeholder="10"
                    size="large"
                    className="optional-table"
                    onChange={(event) => props.handleTableCapacityChange(event, props.tableNumber)}
                    value={props.table.capacity}/>
            </FormItem>
            <Icon className="dynamic-delete-button"
                  type="close"
                  onClick={() => props.removeTable(props.tableNumber)} />
            <Divider />
        </div>
    )
}

function Relationship(props) {
    return (
        <div className="indent">
            <p>Relationship:</p>
            <FormItem label="Guest Name" validateStatus={props.relationship.validateStatus} help={props.relationship.errorMsg} className="config-form-row">
                <Input
                    placeholder="Guest Name"
                    size="large"
                    className="optional-relationship"
                    onChange={(event) => props.handleRelationshipGuestChange(event, props.relationshipNumber, props.guestNumber)}
                    value={props.relationship.guestName}/>
            </FormItem>
            <FormItem label="value" validateStatus={props.relationship.validateStatus} help={props.relationship.errorMsg} className="config-form-row">
                <Input
                    placeholder="10"
                    size="large"
                    className="optional-relationship"
                    onChange={(event) => props.handleRelationshipValueChange(event, props.relationshipNumber, props.guestNumber)}
                    value={props.relationship.likability}/>
            </FormItem>
            <FormItem label="bribe" validateStatus={props.relationship.validateStatus} help={props.relationship.errorMsg} className="config-form-row">
                <Input
                    placeholder="10"
                    size="large"
                    className="optional-relationship"
                    onChange={(event) => props.handleRelationshipBribeChange(event, props.relationshipNumber, props.guestNumber)}
                    value={props.relationship.bribe}/>
            </FormItem>
            <Icon className="dynamic-delete-button"
                  type="close"
                  onClick={() => props.removeRelationship(props.guestNumber, props.relationshipNumber)} />
            <Divider />
        </div>
    )
}

export default NewConfig;