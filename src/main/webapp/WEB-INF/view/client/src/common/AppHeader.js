import {Menu, Icon, Dropdown, Layout} from 'antd'
import React from "react";
import {Link, withRouter} from "react-router-dom";
import "./AppHeader.css";

function AppHeader(props) {
    const handleMenuCLick = (key) => {
        if(key === "logout"){
            props.onLogout();
        }
    };

    let menuItems;
    if(props.currentUser) {
        menuItems = [
            <Menu.Item key="/">
                <Link to="/">
                    <Icon type="home" className="nav-icon" />
                </Link>
            </Menu.Item>,
            <Menu.Item key="/profile" className="profile-menu">
                <ProfileDropdownMenu currentUser={props.currentUser} handleMenuClick={handleMenuCLick} />
            </Menu.Item>
        ];
    } else {
        menuItems = [
            <Menu.Item key="/login">
                <Link to="/login">Login</Link>
            </Menu.Item>,
            <Menu.Item key="/signup">
                <Link to="/signup">Sign up</Link>
            </Menu.Item>
        ];
    }

    return (
        <Layout.Header className="app-header">
            <div className="container">
                <div className="app-title">
                    <Link to="/">Christmas Dinner</Link>
                </div>
                <Menu
                    className="app-menu"
                    mode="horizontal"
                    selectedKeys={[props.location.pathname]}
                    style={{lineHeight: '64px'}} >
                    {menuItems}
                </Menu>
            </div>
        </Layout.Header>
    )
}

function ProfileDropdownMenu(props) {
    const dropdownMenu = (
        <Menu onClick={props.handleMenuClick} className="profile-dropdown-menu">
            <Menu.Item key="user-info" className="dropdown-item" disabled>
                <div className={"user-email-info"}>
                    {props.currentUser.email}
                </div>
            </Menu.Item>
            <Menu.Divider/>
            <Menu.Item key="logout" className="dropdown-item">
                Logout
            </Menu.Item>
        </Menu>
    );

    return (
        <Dropdown overlay={dropdownMenu} trigger={['click']} getPopupContainer={() => document.getElementsByClassName('profile-menu')[0]}>
            <a className="ant-dropdown-link">
                <Icon type="user" className="nav-icon" style={{marginRight: 0}} /> <Icon type="down" />
            </a>
        </Dropdown>
    )
}

export default withRouter(AppHeader);