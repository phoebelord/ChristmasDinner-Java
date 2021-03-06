import React, {useEffect, useState} from 'react';
import './App.css';
import {notification, Layout} from "antd";
import {getCurrentUser} from "../utils/ApiUtils";
import {ACCESS_TOKEN} from "../constants";
import {Route, withRouter, Switch, useHistory} from 'react-router-dom';
import LoadingIndicator from "../common/LoadingIndicator";
import AppHeader from "../common/AppHeader";
import Login from "../user/login/Login";
import Signup from "../user/signup/Signup";
import NewConfig from "../config/NewConfig";
import {ViewConfig} from "../config/ViewConfig";
import PrivateRoute from "../common/PrivateRoute";
import Solution from "../solution/Solution";
import {UserHome} from "../user/home/UserHome";
import EditConfig from "../config/EditConfig";

const { Content } = Layout;

function App(props) {
  const history = useHistory();
  const [currentUser, setCurrentUser] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  notification.config({
    placement: "topRight",
    top: 70,
    duration: 3
  });

  const loadCurrentUser = () => {
    setIsLoading(true);
    getCurrentUser()
        .then(response => {
          setCurrentUser(response);
          setIsAuthenticated(true);
          setIsLoading(false);
        }).catch(error => {
          setIsLoading(false);
    });
  };

  useEffect(() => {
    loadCurrentUser();
  }, []);

  const handleLogout = () => {
    localStorage.removeItem(ACCESS_TOKEN);
    setCurrentUser(null);
    setIsAuthenticated(false);

    history.push("/");

    notification.success({
      message: "Christmas Dinner",
      description: "You're successfully logged out"
    });
  };

  const handleLogin = () => {
    loadCurrentUser();
    history.push("/");

    notification.success({
      message: "Christmas Dinner",
      description: "You're successfully logged in"
    });
  };

  if(isLoading) {
    return <LoadingIndicator />
  }

  return (
    <Layout className="app-container">
      <AppHeader isAuthenticated={isAuthenticated} currentUser={currentUser} onLogout={handleLogout}/>
      <Content className="app-content">
        <div className="container">
          <Switch>
            <Route exact path="/" render={() => <UserHome authenticated={isAuthenticated}/>} handleLogout={handleLogout}/>
            <Route path="/login" render={(props) => <Login onLogin={handleLogin} {...props} />}/>
            <Route path="/signup" component={Signup}/>
            <PrivateRoute authenticated={isAuthenticated} exact path="/config" component={ViewConfig} handleLogout={handleLogout}/>
            <PrivateRoute authenticated={isAuthenticated} path="/config/create" component={NewConfig} handleLogout={handleLogout}/>
            <PrivateRoute authenticated={isAuthenticated} path="/config/edit" component={EditConfig}  handleLogout={handleLogout}/>
            <PrivateRoute authenticated={isAuthenticated} path="/solution" component={Solution} handleLogout={handleLogout}/>
          </Switch>
        </div>
      </Content>
    </Layout>
  );
}

export default App;
