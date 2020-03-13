import {useHistory, useLocation} from "react-router";
import React, {useState} from "react";
import {Button, Collapse, Divider, notification, Table} from "antd";
import "./Config.css";
import {deleteConfig} from "../utils/ApiUtils";

const {Panel} = Collapse;

export function ViewConfig(props) {
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

    const handleDeleteCLick = () => {
        deleteConfig(config.id)
            .then(response => {
                history.push("/");
                notification.success({
                    message: "Christmas Dinner",
                    description: "Successfully deleted config"
                });
            }).catch(error => {
                console.log(error);
            if (error.status === 401) {
                props.handleLogout('/signin', 'error', 'You have been logged out. Sign in to create a config');
            } else {
                notification.error({
                    message: "Christmas Dinner",
                    description: error.message || "Oops something went wrong"
                });
            }
            })
    };

    const getColumns = () => {
        let columns =  [
            {
                title: "",
                dataIndex: "name",
                width: "100px",
                render: (text) => (
                    <div style={{ wordWrap: 'break-word', wordBreak: 'break-word' }}>
                        {text}
                    </div>
                )
            }
        ];
        for(let i = 0; i < config.guests.length; i++) {
            const rel = "relationship" + i;
            columns.push({
                title: config.guests[i].name,
                dataIndex: rel,
                width: "100px",
                render: (text) => (
                    <div style={{ wordWrap: 'break-word', wordBreak: 'break-word' }}>
                        {text}
                    </div>
                )
            });
        }
        return columns;
    };

    const convertLikability = (value) => {
        if(value == -1) {
            return "Dislikes"
        } else if(value == 1) {
            return "Likes"
        } else if(value == 0) {
            return "Neutral"
        } else {
            return "Partner"
        }
    };

    const getRelationshipAndBribe = (i,j) => {
        const guest1 = config.guests[i];
        const guest2 = config.guests[j];

        const rel = "relationship" + j;
        let answer;
        guest1.relationships.forEach(relationship => {
            if(relationship.guestName === guest2.name) {
                answer = {
                    [rel]: convertLikability(relationship.likability) + ", " + relationship.bribe
                };
            }
        });

        if(guest1.name === guest2.name) {
            answer = {
                [rel]: "x"
            };
        }

        return (answer === undefined) ? {[rel]: ""} : answer;
    };

    const createRelationshipMatrix = () => {
        let matrix = [];
        for(let i = 0; i < config.guests.length; i++) {
            matrix[i] = new Array(config.guests.length);
        }

        for(let i = 0; i < config.guests.length; i++) {
            matrix[i] = {
                key: i,
                name: config.guests[i].name
            };

            for(let j = 0; j < config.guests.length; j++) {
                matrix[i] = {...matrix[i], ...getRelationshipAndBribe(i, j)}
            }
        }

        return matrix;
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
                <Table dataSource={createRelationshipMatrix()} columns={getColumns()} scroll={{  x: 'max content' }} bordered={true} />
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
            <div className="lastModified titleBar">
                <p>Last Modified: {config.lastModified}</p>
                <Button type="dashed" onClick={handleDeleteCLick}>Delete</Button>
            </div>
        </div>
    )
}