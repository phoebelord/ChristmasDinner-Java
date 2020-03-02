import FormItem, {Divider, Icon, Input, Select} from "antd";
import React from "react";

const {Option} = Select;

function Table(props) {
    return (
        <div>
            <h1>Table:</h1>
            <FormItem label="Table Shape" validateStatus={props.table.shape.validateStatus}
                      help={props.table.shape.errorMsg}
                      className="config-form-row indent">
                <Select onChange={(event) => props.handleTableShapeChange(event, props.tableNumber)}
                        value={props.table.shape.text}>
                    <Option value="Circle">Circle</Option>
                    <Option value="Rectangle">Rectangle</Option>
                </Select>
            </FormItem>
            <FormItem label="Table Capacity" validateStatus={props.table.capacity.validateStatus}
                      help={props.table.capacity.errorMsg}
                      className="config-form-row indent">
                <Input
                    placeholder="10"
                    size="large"
                    className="optional-table"
                    onChange={(event) => props.handleTableCapacityChange(event, props.tableNumber)}
                    value={props.table.capacity.text}/>
            </FormItem>
            <Icon className="dynamic-delete-button"
                  type="close"
                  onClick={() => props.removeTable(props.tableNumber)}/>
            <Divider/>
        </div>
    )
}

export default Table;