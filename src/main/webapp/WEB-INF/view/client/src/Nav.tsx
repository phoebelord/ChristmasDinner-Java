import * as React from 'react';
import {Container, Navbar} from "react-bootstrap";

function Nav() {
    return (
        <Navbar bg="dark" variant="dark" fixed="top" >
            <Container>
                <Navbar.Brand href="#home">
                    The Christmas Dinner Problem
                </Navbar.Brand>
            </Container>
        </Navbar>

    )
}


export default Nav