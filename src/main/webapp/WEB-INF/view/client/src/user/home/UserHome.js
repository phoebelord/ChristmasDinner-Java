import React, {useEffect, useState} from "react";
import {getConfig, getMyConfigs} from "../../utils/ApiUtils";
import LoadingIndicator from "../../common/LoadingIndicator";
import {Table} from "antd";
import {useHistory} from "react-router";

export function UserHome() {
    const [configs, setConfigs] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const history = useHistory();

    useEffect(() => {
        setIsLoading(true);
        getMyConfigs()
            .then(response => {
                setConfigs(response);
                setIsLoading(false);
            })
            .catch(error => {
                setIsLoading(false);
            })
    }, []);

    if(isLoading) {
        return <LoadingIndicator/>
    }

    console.log(configs);

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
        <Table dataSource={configs} columns={columns} rowKey='id' onRow={(record, index) => {
            return {
                onClick: () => {handleRowClick(record)}
            }
        }}/>
    )
}