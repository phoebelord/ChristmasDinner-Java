import {Divider, Icon, Input, Select} from "antd";
import React from "react";
import FormItem from "antd/lib/form/FormItem";

const {Option} = Select;

function Relationship(props) {
    return (
        <div className="indent">
            <p>Relationship:</p>
            <FormItem label="Guest Name" validateStatus={props.relationship.guestName.validateStatus}
                      help={props.relationship.guestName.errorMsg} className="config-form-row">
                <Select className="optional-relationship"
                        onChange={(event) => props.handleRelationshipGuestChange(event, props.relationshipNumber, props.guestNumber)}>
                    {props.guests.filter((guest, index) => {
                        return index !== props.guestNumber;
                    }).map((guest, index) =>
                        <Option key={index} value={guest.name.text}>{guest.name.text}</Option>
                    )}
                </Select>
            </FormItem>
            <FormItem label="value" validateStatus={props.relationship.likability.validateStatus}
                      help={props.relationship.likability.errorMsg} className="config-form-row">
                <Select className="optional-relationship"
                        onChange={(event) => props.handleRelationshipValueChange(event, props.relationshipNumber, props.guestNumber)}
                        value={props.relationship.likability.text}>
                    <Option value="10">Partner</Option>
                    <Option value="1">Likes</Option>
                    <Option value="-1">Dislikes</Option>
                </Select>
            </FormItem>
            <FormItem label="bribe" validateStatus={props.relationship.bribe.validateStatus}
                      help={props.relationship.bribe.errorMsg} className="config-form-row">
                <Input
                    placeholder="10"
                    className="optional-relationship"
                    onChange={(event) => props.handleRelationshipBribeChange(event, props.relationshipNumber, props.guestNumber)}
                    value={props.relationship.bribe.text}/>
            </FormItem>
            <Icon className="dynamic-delete-button"
                  type="close"
                  onClick={() => props.removeRelationship(props.guestNumber, props.relationshipNumber)}/>
            <Divider/>
        </div>
    )
}

export default Relationship;