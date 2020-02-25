import {useLocation} from "react-router";
import React, {useState} from "react";
import {Divider} from "antd";

export function Config() {
    const location = useLocation();
    const [config, setConfig] = useState(location.state.config);

    return (
        <div>
            <h1>{config.name}</h1>
            <div>
                <h2>Guests:</h2>
                {config.guests.map((guest, index) =>
                    <div key={index}>
                        <h3>Guest {index + 1}</h3>
                        <p>Name: {guest.name}</p>
                        <p>Relationships:</p>
                        {guest.relationships.map((relationship, index) =>
                            <div key={index} className="indent">
                                <p>Guest Name: {relationship.guestName}</p>
                                <p>Likability: {relationship.likability}</p>
                                <Divider/>
                            </div>
                        )}
                        <Divider/>
                    </div>
                )}
            </div>
            <div>
                <h2>Tables:</h2>
                {config.tables.map((table, index) =>
                    <div key={index} className="indent">
                        <h3>Table {index + 1}</h3>
                        <p>Shape: {table.shape}</p>
                        <p>Capacity: {table.capacity}</p>
                        <Divider/>
                    </div>
                )}
            </div>
            <p>Last Modified: {config.lastModified}</p>
        </div>
    )
}