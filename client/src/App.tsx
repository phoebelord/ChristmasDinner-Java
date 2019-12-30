import * as React from 'react';
import './App.css';
import Nav from "./Nav";
import {Container} from "react-bootstrap";
import DataSetForm from "./Solution";


function App() {
    return (
        <Container className="p-3" fluid={true}>
            <Nav/>
            <DataSetForm />
        </Container>
    )
}

export default App;
