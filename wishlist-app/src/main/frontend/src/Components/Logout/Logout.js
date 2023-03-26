import { useNavigate } from "react-router-dom"
import { logout } from "../../utils/auth"
import './Logout.css';


export default function Logout({setVerified}) {
    const navigate = useNavigate()

    const logoutHandler = () => {
        logout()
        setVerified(false)
        navigate("/login")
    }
    return (
        <button style={{background: "black", color: "white", padding: "5px 10px"}} onClick={logoutHandler}>Logout</button>
    );
}