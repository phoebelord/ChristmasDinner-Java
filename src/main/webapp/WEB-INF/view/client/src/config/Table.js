import {Divider, Icon, Input, Select} from "antd";
import React from "react";
import FormItem from "antd/lib/form/FormItem";

const {Option} = Select;

function Table(props) {
    return (
        <div>
            {props.tableNumber > 0 ? <Divider/> : null}
            <h1>Table:</h1>
            <FormItem label="Table Shape" validateStatus={props.table.shape.validateStatus}
                      help={props.table.shape.errorMsg}
                      className="config-form-row">
                <Select onChange={(event) => props.handleTableShapeChange(event, props.tableNumber)}
                        className="optional-table"
                        value={props.table.shape.text}>
                    <Option value="Circle">Circle</Option>
                    <Option value="Rectangle">Rectangle</Option>
                </Select>
                <Icon className="dynamic-delete-button"
                      type="delete"
                      onClick={() => props.removeTable(props.tableNumber)}/>
            </FormItem>
            <FormItem label="Table Capacity" validateStatus={props.table.capacity.validateStatus}
                      help={props.table.capacity.errorMsg}
                      className="config-form-row">
                <Input
                    placeholder="10"
                    size="large"
                    className="optional-table"
                    onChange={(event) => props.handleTableCapacityChange(event, props.tableNumber)}
                    value={props.table.capacity.text}/>
            </FormItem>
        </div>
    )
}

export default Table;