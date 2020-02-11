import * as React from 'react';
import './App.css';
import {BrowserRouter, Route} from 'react-router-dom';
import Login from "./Login";
import Home from "./Home";
import Nav from "./Nav";
import {Container} from "react-bootstrap";


function App() {
    return (
        <div>
            <Nav/>
            <Container className={"text-center mt-5 pt-3"}>
                <BrowserRouter>
                    <Route path="/index.html" exact={true} component={Login}/>
                    <Route path="/homepage" component={Home}/>
                </BrowserRouter>
            </Container>
        </div>
    )
}

export default App;

