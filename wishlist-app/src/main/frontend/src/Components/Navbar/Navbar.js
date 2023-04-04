import React, { useContext } from "react";
import { NavLink, Link } from "react-router-dom";
import "./Navbar.css";
import logo from "./logo.png";
import AuthContext from "../Contexts/AuthContext";
import Cookies from "js-cookie";
import logo_With_Name from "./logo_With_Name.png";

export default function Navbar() {
    const authState = useContext(AuthContext);
    
    const logout = () => {
        Cookies.remove("auth-session");
        sessionStorage.removeItem("token");
        authState.setIsLoggedIn(false);
    }
    return authState.isLoggedIn ? 
        (
            <nav className="Navbar">
                <Link to="/"><img id="Navbar-logo" src={logo} alt="genie-logo"/></Link>
                <div className="Navbar-links">
                    <NavLink key="profile" to="/profile">Profile</NavLink>
                    <NavLink key="wishlists" to="/wishlists">Wishlists</NavLink>
                    <NavLink key="shared" to="/shared">Shared</NavLink>
                </div>
                <button className="Navbar-logout" onClick={logout}>Logout</button>
            </nav>
        ) : (
            <nav className="Navbar">
                <Link to="/"><img id="Navbar-logo" src={logo_With_Name} alt="genie-logo"/></Link>
                <div className="Navbar-links">
                    <NavLink key="login" to="/login">Login</NavLink>
                    <NavLink key="register" to="/register">Sign Up</NavLink>
                </div>
            </nav>
        );
           
}


