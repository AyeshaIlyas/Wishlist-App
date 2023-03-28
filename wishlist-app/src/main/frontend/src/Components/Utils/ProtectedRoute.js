 import Login from "./../Login/Login";
 import AuthContext from "../Contexts/AuthContext";
import { useContext } from "react";

 const ProtectedRoute =  ({component}) => {
    const authState = useContext(AuthContext);
    return authState.isLoggedIn
        ? component
        : <Login />
 }

export default ProtectedRoute;