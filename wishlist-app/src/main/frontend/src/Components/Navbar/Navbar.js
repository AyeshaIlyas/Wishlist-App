import React, { Component } from "react";
import { NavLink, Link } from "react-router-dom";
import "./Navbar.css";
import logo from "./logo.png";
import logo_With_Name from "./logo_With_Name.png";

class Navbar extends Component {

    createNav() {
        if (this.props.isLoggedIn) {
            return (
                <nav className="Navbar">
                    <Link to="/"><img className="Navbar-logo" src={logo} alt="genie-logo"/></Link>
                    <div className="Navbar-links">
                        <NavLink key="profile" to="/profile" >Profile</NavLink>
                        <NavLink key="wishlists" to="/wishlists">Wishlists</NavLink>
                        <NavLink key="shared" to="/shared">Shared</NavLink>
                    </div>
                    <NavLink key="logout" to="/"><button className="Navbar-logout" onClick={this.props.logout}>Logout</button></NavLink>
                </nav>
            );
       } 
        return (
            <nav className="Navbar">
                <Link to="/"><img className="Navbar-logo" src={logo_With_Name} alt="genie-logo"/></Link>
                <div className="Navbar-links">
                    <NavLink key="login" to="/login">Login</NavLink>
                    <NavLink key="register" to="/register">Sign Up</NavLink>
                </div>
            </nav>
        );
            
    }

    render() {
        return (
            <>
             {this.createNav()}
            </>
        );
    }
}

export default Navbar;
