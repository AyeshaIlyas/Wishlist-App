import React, { useContext, useEffect, useState } from "react";
import { NavLink, Link, useNavigate } from "react-router-dom";
import "./Navbar.css";
import logo from "./logo.png";
import AuthContext from "../Contexts/AuthContext";
import Cookies from "js-cookie";
import {authWrapper} from "./../../services/utils";
import { getProfile } from '../../services/profileService';

export default function Navbar() {
    const authState = useContext(AuthContext);
    const navigate = useNavigate();
    const [profileInfo, setProfileInfo] = useState({});

    useEffect(
        () => {
            const makeRequest = async () => {
                const safeGetProfile = authWrapper(authState.setIsLoggedIn, getProfile);
                const profile = await safeGetProfile();
                if (profile) {
                    setProfileInfo(profile);
                }
            }
            makeRequest();
        }, [authState]
    );

    const displayProfile = () => {
        return(
            <div className="Navbar-profile-info">
                <p>{profileInfo.firstName} {profileInfo.lastName}</p>
                <p> {profileInfo.email}</p>               
            </div> 
        );      
    }

    const logout = () => {
        Cookies.remove("auth-session");
        sessionStorage.removeItem("token");
        authState.setIsLoggedIn(false);
        navigate("/login");
    }

    return authState.isLoggedIn ? 
        (
            <nav className="Navbar">
                 <div className="logo">
                    <Link to="/"><img id="Navbar-logo" src={logo} alt="genie-logo"/></Link>
                    <Link to="/" className="Navbar-product-name"><span className="Navbar-product-name">Fourth Wish</span></Link>
                </div>
                <div className="Navbar-links">
                    <NavLink key="profile" to="/profile">Profile</NavLink>
                    <NavLink key="wishlists" to="/wishlists">Wishlists</NavLink>
                    <NavLink key="shared" to="/shared">Shared</NavLink>
                </div>
                <div className="Navbar-userInfo">
                    {displayProfile()}
                    <button className="Navbar-logout" onClick={logout}>Logout</button>
                </div>

            </nav>
        ) : (
            <nav className="Navbar"> 
                <div className="logo">
                    <Link to="/"><img id="Navbar-logo" src={logo} alt="genie-logo"/></Link>
                    <Link to="/" className="Navbar-product-name"><span className="Navbar-product-name">Fourth Wish</span></Link>
                </div>
                <div className="Navbar-links">
                    <NavLink key="login" to="/login">Login</NavLink>
                    <NavLink key="register" to="/register">Register</NavLink>
                </div>
            </nav>
        );
           
}


