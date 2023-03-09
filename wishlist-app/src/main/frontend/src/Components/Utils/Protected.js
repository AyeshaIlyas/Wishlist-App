 import {Navigate } from "react-router-dom";
 import { loggedIn } from "../../utils/auth";

 const Protected =  ({children}) => {
    if (!loggedIn()) {
        return <Navigate to="/login" replace/>
    }
    return children;
 }

export default Protected