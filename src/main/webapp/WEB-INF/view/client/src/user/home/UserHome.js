import React, {useEffect, useState} from "react";
import {getConfig, getMyConfigs} from "../../utils/ApiUtils";
import {Table} from "antd";
import {useHistory} from "react-router";
import "./UserHome.css";

export function UserHome(props) {
    const [configs, setConfigs] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const history = useHistory();

    useEffect(() => {
        if(props.authenticated) {
            setIsLoading(true);
            getMyConfigs()
                .then(response => {
                    setConfigs(response);
                    setIsLoading(false);
                })
                .catch(error => {
                    setIsLoading(false);
                })
        } else {
            setConfigs([])
        }
    }, [props.authenticated]);

    const columns = [
        {
            title: 'Name',
            dataIndex: 'name',
            key: 'name',
            render: text => <a>{text}</a>,
        },
        {
            title: 'No. Guests',
            dataIndex: 'numGuests',
            key: 'numGuests',
        },
        {
            title: 'No. Tables',
            dataIndex: 'numTables',
            key: 'numTables',
        },
        {
            title: 'Last Modified',
            key: 'lastModified',
            dataIndex: 'lastModified'
        }
    ];

    const handleRowClick = (record) => {
        getConfig(record.id)
            .then(response => {
                history.push({
                    pathname: '/config',
                    state: {config: response}
                });
            })
    };

    return (
        <div className="home-container">
            <h1>Your Configs:</h1>
            <Table
                dataSource={configs}
                columns={columns}
                rowKey='id'
                onRow={(record, index) => {
                    return {
                        onClick: () => {handleRowClick(record)}
                    }
                }}
                loading={isLoading}/>
        </div>

    )
}