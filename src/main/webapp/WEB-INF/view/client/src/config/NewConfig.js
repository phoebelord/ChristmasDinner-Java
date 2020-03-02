import React, {useState} from "react";
import {useHistory} from 'react-router-dom';
import {Button, Divider, Form, Input, notification} from "antd";
import {createConfig, editConfig} from "../utils/ApiUtils";
import "./NewConfig.css"
import {useLocation} from "react-router";
import Table from "./Table";
import Guest from "./Guest";
import FormItem from "antd/lib/form/FormItem";

function NewConfig(props) {
    const history = useHistory();
    const location = useLocation();

    const [name, setName] = useState(location.state ? {text: location.state.config.name} : {text: ''});

    const [guests, setGuests] = useState(location.state ? location.state.config.guests.map(guest => {
        return {
            name: {
                text: guest.name
            },
            relationships: guest.relationships.map(relationship => {
                return {
                    guestName: {
                        text: relationship.guestName
                    },
                    likability: {
                        text: relationship.likability
                    },
                    bribe: {
                        text: relationship.bribe
                    }
                }
            })
        }
    }) : []);

    const [tables, setTables] = useState(location.state ? location.state.config.tables.map(table => {
        return {
            shape: {
                text: table.shape
            },
            capacity: {
                text: table.capacity
            }
        }
    }) : []);

    const addGuest = () => {
        const guestss = guests.slice();
        setGuests(guestss.concat([{
            name: {
                text: ''
            },
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
            guestName: {
                text: ""
            },
            likability: {
                text: null
            },
            bribe: {
                text: null
            }
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
            shape: {
                text: ''
            },
            capacity: {
                text: null
            }
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
                    name: guest.name.text,
                    relationships: guest.relationships.map(relationship => {
                        return {
                            guestName: relationship.guestName.text,
                            likability: relationship.likability.text,
                            bribe: relationship.bribe.text
                        }
                    })
                }
            }),
            tables: tables.map(table => {
                return {
                    shape: table.shape.text,
                    capacity: table.capacity.text
                }
            })
        };

        if (location.state) {
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
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null
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

    const validateGuestName = (guestName) => {
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
        const guestss = guests.slice();
        const value = event.target.value;
        let guest = guestss[index];
        guest.name = {
            text: value,
            ...validateGuestName(value)
        };
        guestss[index] = {...guest};
        setGuests(guestss);
    };

    const handleTableShapeChange = (value, index) => {
        const tabless = tables.slice();
        let table = tabless[index];
        table.shape = {
            text: value,
            ...validateTableShape(value)
        };
        tabless[index] = {...table};
        setTables(tabless);
    };

    const handleTableCapacityChange = (event, index) => {
        const tabless = tables.slice();
        const value = event.target.value;
        let table = tabless[index];
        table.capacity = {
            text: value,
            ...validateTableCapacity(value)
        };
        tabless[index] = {...table};
        setTables(tabless);
    };

    const handleRelationshipGuestChange = (value, relIndex, guestIndex) => {
        const guestss = guests.slice();
        const relationship = guestss[guestIndex].relationships[relIndex];
        relationship.guestName = {
            text: value,
            ...validateRelationshipGuest(value)
        };
        guestss[guestIndex].relationships[relIndex] = {...relationship};
        setGuests(guestss);
    };

    const validateRelationshipGuest = (name) => {
        if (name.length === 0) {
            return {
                validateStatus: 'error',
                errorMsg: "Please enter a guest name"
            }
        }

        let containsName = false;
        guests.forEach(guest => {
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
        const guestss = guests.slice();
        const relationship = guestss[guestIndex].relationships[relIndex];
        relationship.likability = {
            text: value,
            ...validateRelationshipValue(value)
        };
        guestss[guestIndex].relationships[relIndex] = {...relationship};
        setGuests(guestss);
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
        const guestss = guests.slice();
        const value = event.target.value;
        const relationship = guestss[guestIndex].relationships[relIndex];
        relationship.bribe = {
            text: value,
            ...validateBribe(value)
        };
        guestss[guestIndex].relationships[relIndex] = {...relationship};
        setGuests(guestss);
        console.log(guests);
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
        let invalid = name.validateStatus !== 'success';
        guests.forEach(guest => {
            if (guest.name.validateStatus !== 'success') {
                invalid = true;
            }
            guest.relationships.forEach(relationship => {
                if (relationship.guestName.validateStatus !== 'success') {
                    invalid = true;
                }

                if (relationship.likability.validateStatus !== 'success') {
                    invalid = true;
                }

                if (relationship.bribe.validateStatus !== 'success') {
                    invalid = true;
                }
            })
        });

        tables.forEach(table => {
            if (table.shape.validateStatus !== 'success') {
                invalid = true;
            }

            if (table.capacity.validateStatus !== 'success') {
                invalid = true;
            }
        });

        return invalid;
    };

    const guestViews = [];
    guests.forEach((guest, index) => {
        guestViews.push(<Guest
            key={index} guest={guest}
            guestNumber={index}
            removeGuest={removeGuest}
            handleGuestChange={handleGuestNameChange}
            addRelationship={addRelationship}
            removeRelationship={removeRelationship}
            handleRelationshipGuestChange={handleRelationshipGuestChange}
            handleRelationshipValueChange={handleRelationshipValueChange}
            handleRelationshipBribeChange={handleRelationshipBribeChange}
            guests={guests}
        />)
    });

    const tableViews = [];
    tables.forEach((table, index) => {
        tableViews.push(<Table key={index} table={table} tableNumber={index} removeTable={removeTable}
                               handleTableShapeChange={handleTableShapeChange}
                               handleTableCapacityChange={handleTableCapacityChange}/>)
    });

    return (
        <div className="new-config-container">
            <h1 className="page-title">Create Config</h1>
            <div className="new-config-content">
                <Form onSubmit={handleSubmit} className="create-config-form">
                    <FormItem label="Config name" validateStatus={name.validateStatus} help={name.errorMsg}
                              className="config-form-row">
                        <Input
                            placeholder='Your config name'
                            size="large"
                            onChange={handleNameChange}
                            value={name.text}/>
                    </FormItem>
                    <Divider/>
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
                                disabled={isFormInvalid()}
                                className="create-config-form-button">Save Config</Button>
                    </FormItem>
                </Form>
            </div>
        </div>
    )
}

export default NewConfig;