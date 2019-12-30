import * as React from 'react';
import {Navbar} from "react-bootstrap";

function Nav() {
    return (
        <Navbar bg="dark" variant="dark" fixed="top">
            <Navbar.Brand href="#home">
                The Christmas Dinner Problem
            </Navbar.Brand>
        </Navbar>

    )
}


export default Nav