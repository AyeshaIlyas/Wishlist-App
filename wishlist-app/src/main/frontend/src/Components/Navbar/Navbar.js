import React, { Component } from "react";
import { NavLink, Link } from "react-router-dom";
import "./Navbar.css";
import logo from "./logo.png";

class Navbar extends Component {

    createNav() {
        if (this.props.isLoggedIn) {
            return (
                <nav className="Navbar">
                    <Link to="/"><img id="Navbar-logo" src={logo} alt="genie-logo"/></Link>
                    <div className="Navbar-links">
                        <NavLink key="profile" to="/profile">Profile</NavLink>
                        <NavLink key="wishlists" to="/wishlists">Wishlists</NavLink>
                        <NavLink key="shared" to="/shared">Shared</NavLink>
                    </div>
                    <button className="Navbar-logout" onClick={this.props.logout}>Logout</button>
                </nav>
            );
        } 
        return (
            <nav className="Navbar">
                <Link to="/"><img id="Navbar-logo" src={logo} alt="genie-logo"/></Link>
                <h1 className="Navbar-title">Fourth Wish</h1>
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
