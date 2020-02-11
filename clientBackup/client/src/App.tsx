import * as React from 'react';
import './App.css';
import {BrowserRouter, Route, useHistory} from 'react-router-dom';
import Login from "./Login";
import Home from "./Home";
import Nav from "./Nav";
import {Container} from "react-bootstrap";
import {useEffect, useState} from "react";
import {getCurrentUser} from "./Utils";


function App() {
    const history = useHistory();
    const [currentUser, setCurrentUser] = useState(null);
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [isLoading, setIsLoading] = useState(false);

    const loadCurrentUser = () => {
        setIsLoading(true);
        getCurrentUser().then(response => {
            setCurrentUser(response);
            setIsAuthenticated(true);
            setIsLoading(false);
        }).catch(error => {
            setIsLoading(false);
        })
    };

    useEffect(() => {
        loadCurrentUser();
    }, []);

    const handleLogout = (redirectTo="/") => {
        localStorage.removeItem('accessToken');
        setCurrentUser(null);
        setIsAuthenticated(false);
        history.push(redirectTo);
    };

    const handleLogin = () => {
        loadCurrentUser();
        history.push("/");
    };

    if(isLoading) {
        return <p>Loading .....</p>
    }

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

