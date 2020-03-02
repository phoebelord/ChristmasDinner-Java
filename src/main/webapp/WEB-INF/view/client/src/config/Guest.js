import Relationship from "./Relationship";
import {Button, Divider, Icon, Input} from "antd";
import React from "react";
import FormItem from "antd/lib/form/FormItem";

function Guest(props) {
    const relationshipViews = [];
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

    return (
        <div>
            <FormItem label="Guest Name" validateStatus={props.guest.name.validateStatus}
                      help={props.guest.name.errorMsg}
                      className="config-form-row">
                <Input
                    placeholder={'Guest ' + (props.guestNumber + 1)}
                    size="large"
                    className="optional-guest"
                    onChange={(event) => props.handleGuestChange(event, props.guestNumber)}
                    value={props.guest.name.text}/>
            </FormItem>
            {relationshipViews}
            <FormItem className="config-form-fow">
                <Button type="dashed" onClick={() => props.addRelationship(props.guestNumber)}>Add Relationship</Button>
            </FormItem>
            <Icon className="dynamic-delete-button"
                  type="close"
                  onClick={() => props.removeGuest(props.guestNumber)}/>
            <Divider/>
        </div>

    )
}

export default Guest;