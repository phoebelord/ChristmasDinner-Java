import {useHistory, useLocation} from "react-router";
import React, {useState} from "react";
import {Button, Divider, Collapse} from "antd";
import "./Config.css";

const {Panel} = Collapse;

export function Config() {
    const history = useHistory();
    const location = useLocation();
    const [config, setConfig] = useState(location.state.config);

    const handleEditClick = () => {
        history.push({
            pathname: '/config/edit',
            state: {config: config}
        })
    };

    const handleSolutionClick = () => {
        history.push({
            pathname: '/solution',
            state: {config: config}
        })
    };

    return (
        <div className="config-container">
            <div className="titleBar">
                <h1>{config.name}</h1>
                <div>
                    <Button type="dashed" onClick={handleEditClick}>Edit</Button>
                    <Button type="dashed" onClick={handleSolutionClick}>Get solution</Button>
                </div>
            </div>
            <div>
                <h2>Guests:</h2>
                {config.guests.map((guest, index) =>
                    <div key={index}>
                        <h3>{index + 1}: {guest.name}</h3>
                        <Collapse>
                            <Panel key={index} header="Relationships">
                                {guest.relationships.map((relationship, index) =>
                                    <div key={index}>
                                        <p>Guest Name: {relationship.guestName}</p>
                                        <p>Likability: {relationship.likability}</p>
                                        <Divider/>
                                    </div>
                                )}
                            </Panel>
                        </Collapse>
                        <Divider/>
                    </div>
                )}
            </div>
            <div>
                <h2>Tables:</h2>
                {config.tables.map((table, index) =>
                    <div key={index}>
                        <h3>{index + 1}: {table.shape}, {table.capacity} people</h3>
                        <Divider/>
                    </div>
                )}
            </div>
            <div className="lastModified">
                <p>Last Modified: {config.lastModified}</p>
            </div>
        </div>
    )
}