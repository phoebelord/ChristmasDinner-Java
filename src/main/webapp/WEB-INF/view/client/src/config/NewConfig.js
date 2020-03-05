import React, {useState} from "react";
import {useHistory} from 'react-router-dom';
import {notification} from "antd";
import {createConfig} from "../utils/ApiUtils";
import "./NewConfig.css"
import {ConfigForm} from "./ConfigForm";

function NewConfig(props) {
    const history = useHistory();
    const [name, setName] = useState({text: ''});
    const [guests, setGuests] = useState([]);
    const [tables, setTables] = useState([]);

    const addGuest = () => {
        const guestss = guests.slice();
        setGuests(guestss.concat([{
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
            id: '',
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

export default NewConfig;