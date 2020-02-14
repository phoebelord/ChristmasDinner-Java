import React, {useState} from "react";
import {useHistory} from 'react-router-dom';
import {Button, Form, Icon, Input, notification} from "antd";
import {createConfig} from "../utils/ApiUtils";
import FormItem from "antd/es/form/FormItem";

function NewConfig(props) {
    const history = useHistory();
    const [name, setName] = useState({
        text: ''
    });
    const [guests, setGuests] = useState([{
        name: '',
        relationships: []
    }]);
    const [tables, setTables] = useState([{
        shape: "Circle",
        capacity: 2
    }]);

    const addGuest = () => {
        const guestss = guests.slice();
        setGuests(guestss.concat([{
            name: '',
            relationships: []
        }]));
        console.log(guests);
    };

    const removeGuest = (guestNumber) => {
        const guestss = guests.slice();
        setGuests([...guestss.slice(9, guestNumber), ...guestss.slice(guestNumber + 1)])
        console.log(guests)
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        const configData = {
            name: name.text,
            guests: guests.map(guest => {
                return {
                    name: guest.name,
                    relationships: guest.relationships.map(relationship => {
                        return {
                            guestName: relationship.guestName,
                            likability: relationship.likability
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

    const handleGuestChange = (event, index) => {
        const guestss = guests.slice();
        const value = event.target.value;
        guestss[index] = {
            name: value,
            relationships: [],
            ...validateGuest(value)
        };
        setGuests(guestss);
    };

    const isFormInvalid = () => {
        return name.validateStatus !== 'success';
    };

    const guestViews = [];
    guests.forEach((guest, index) => {
        guestViews.push(<Guest key={index} guest={guest} guestNumber={index} removeGuest={removeGuest} handleGuestChange={handleGuestChange}/>)
    });

    return (
        <div className="new-config-container">
            <h1 className="page-title">Create Config</h1>
            <div className="new-config-content">
                <Form onSubmit={handleSubmit} className="create-config-form">
                    <FormItem validateStatus={name.validateStatus} help={name.errorMsg} className="config-form-row">
                        <Input
                            placeholder='Name'
                            size="large"
                            onChange={handleNameChange} />
                    </FormItem>
                    {guestViews}
                    <FormItem className="config-form-fow">
                        <Button type="dashed" onClick={addGuest}>Add Guest</Button>
                    </FormItem>
                    <FormItem className="config-form-row">
                        <Button type="primary"
                                htmlType="submit"
                                size="large"
                                disabled={isFormInvalid()}
                                className="create-config-form-button">Create Config</Button>
                    </FormItem>
                </Form>
            </div>
        </div>
    )
}

function Guest(props) {
    return (
        <FormItem validateStatus={props.guest.validateStatus} help={props.guest.errorMsg} className="config-form-row">
            <Input
                placeholder={'Guest ' + (props.guestNumber + 1)}
                size="large"
                className="optional-guest"
                onChange={(event) => props.handleGuestChange(event, props.guestNumber)} />

            <Icon className="dynamic-delete-button"
                  type="close"
                  onClick={() => props.removeGuest(props.guestNumber)} />
        </FormItem>
    )
}

export default NewConfig;