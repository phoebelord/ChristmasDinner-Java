import React, {useState} from "react";
import {useHistory} from 'react-router-dom';
import {notification} from "antd";
import {editConfig} from "../utils/ApiUtils";
import "./NewConfig.css"
import {useLocation} from "react-router";
import {ConfigForm} from "./ConfigForm";

function EditConfig(props) {
    const history = useHistory();
    const location = useLocation();
    const [name, setName] = useState( {text: location.state.config.name});
    const [guests, setGuests] = useState(location.state.config.guests.map(guest => {
        return {
            id: guest.id,
            name: {
                text: guest.name
            },
            relationships: guest.relationships.map(relationship => {
                return {
                    id: relationship.id,
                    guestName: {
                        text: relationship.guestName
                    },
                    likability: {
                        text: relationship.likability.toString()
                    },
                    bribe: {
                        text: relationship.bribe
                    }
                }
            })
        }
    }));

    const [tables, setTables] = useState(location.state.config.tables.map(table => {
        return {
            id: table.id,
            shape: {
                text: table.shape
            },
            capacity: {
                text: table.capacity
            }
        }
    }));

    const addGuest = () => {
        const guestss = guests.slice();
        setGuests(guestss.concat([{
            id: -1,
            name: {
                text: ''
            },
            relationships: []
        }]));
    };

    const addRelationship = (guestNumber) => {
        const guestss = guests.slice();
        const guestRelationships = guests[guestNumber].relationships.slice();
        guestRelationships.push({
            id: -1,
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

    const addTable = () => {
        const tabless = tables.slice();
        setTables(tabless.concat([{
            id: -1,
            shape: {
                text: ''
            },
            capacity: {
                text: null
            }
        }]));
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        const configData = {
            id: location.state.config.id ,
            name: name.text,
            guests: guests.map(guest => {
                return {
                    id: guest.id,
                    name: guest.name.text,
                    relationships: guest.relationships.map(relationship => {
                        return {
                            id: relationship.id,
                            guestName: relationship.guestName.text,
                            likability: relationship.likability.text,
                            bribe: relationship.bribe.text
                        }
                    })
                }
            }),
            tables: tables.map(table => {
                return {
                    id: table.id,
                    shape: table.shape.text,
                    capacity: table.capacity.text
                }
            })
        };

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


    };

    return (
        <ConfigForm
            name={name}
            setName={setName}
            guests={guests}
            setGuests={setGuests}
            tables={tables}
            setTables={setTables}
            addGuest={addGuest}
            addRelationship={addRelationship}
            addTable={addTable}
            handleSubmit={handleSubmit}/>
    )

}

export default EditConfig;