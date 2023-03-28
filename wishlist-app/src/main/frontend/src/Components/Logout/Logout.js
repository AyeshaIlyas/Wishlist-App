import { useContext } from "react";
import AuthContext from "../Contexts/AuthContext";
import './Logout.css';
import Cookies from "js-cookie";


export default function Logout() {
    const authState = useContext(AuthContext);

    const logoutHandler = () => {
        Cookies.remove("auth-session");
        authState.setIsLoggedIn(false);
    }
    return (
        <button style={{background: "black", color: "white", padding: "5px 10px"}} onClick={logoutHandler}>Logout</button>
    );
}