import React, { useContext } from "react";
import { NavLink, Link } from "react-router-dom";
import "./Navbar.css";
import logo from "./logo.png";
import AuthContext from "../Contexts/AuthContext";

export default function Navbar() {
    const authState = useContext(AuthContext);

    return authState.isLoggedIn ? 
        (
            <nav className="Navbar">
                <Link to="/"><img id="Navbar-logo" src={logo} alt="genie-logo"/></Link>
                <div className="Navbar-links">
                    <NavLink key="profile" to="/profile">Profile</NavLink>
                    <NavLink key="wishlists" to="/wishlists">Wishlists</NavLink>
                    <NavLink key="shared" to="/shared">Shared</NavLink>
                </div>
                <button className="Navbar-logout" onClick={this.props.logout}>Logout</button>
            </nav>
        ) : (
            <nav className="Navbar">
                <Link to="/"><img id="Navbar-logo" src={logo} alt="genie-logo"/></Link>
                <h1 className="Navbar-title">Fourth Wish</h1>
            </nav>
        );
           
}


