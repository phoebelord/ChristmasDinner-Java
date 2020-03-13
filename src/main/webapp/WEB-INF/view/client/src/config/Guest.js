import Relationship from "./Relationship";
import {Button, Divider, Icon, Input} from "antd";
import React from "react";
import FormItem from "antd/lib/form/FormItem";

function Guest(props) {
    const relationshipViews = [];
    if(props.addRelationship) {
        props.guest.relationships.forEach((relationship, index) => {
            relationshipViews.push(<Relationship
                key={index}
                relationship={relationship}
                relationshipNumber={index}
                guestNumber={props.guestNumber}
                removeRelationship={props.removeRelationship}
                handleRelationshipGuestChange={props.handleRelationshipGuestChange}
                handleRelationshipValueChange={props.handleRelationshipValueChange}
                handleRelationshipBribeChange={props.handleRelationshipBribeChange}
                addRelationship={props.addRelationship}
                guests={props.guests}/>)
        });
    }

    const className = props.addRelationship ? "" : "optional-guest";
    return (
        <div>
            {props.guestNumber > 0 ? <Divider/> : null}
            <FormItem label="Guest Name" validateStatus={props.guest.name.validateStatus}
                      help={props.guest.name.errorMsg}
                      className="config-form-row">
                <Input
                    placeholder={'Guest ' + (props.guestNumber + 1)}
                    size="large"
                    className={className}
                    onChange={(event) => props.handleGuestChange(event, props.guestNumber)}
                    value={props.guest.name.text}/>
                {props.addRelationship ? null : (
                    <Icon type="delete" className="dynamic-delete-button"
                          onClick={() => {
                              props.removeGuest(props.guestNumber);
                          }}/>
                )}
            </FormItem>
            {props.addRelationship ? (
                <div>
                    {relationshipViews}
                    <FormItem className="config-form-row">
                        <Button type="dashed" className="create-config-form-button" onClick={() => props.addRelationship(props.guestNumber)}>Add Relationship</Button>
                    </FormItem>
                </div>
                ): null}
        </div>

    )



}

export default Guest;