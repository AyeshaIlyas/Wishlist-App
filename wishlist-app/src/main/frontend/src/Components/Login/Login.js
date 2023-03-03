import {Link} from "react-router-dom";

export default function Login() {

    return (
        <div className="Login">
            <h1>LOGIN</h1>
            <form>
                <label for="username">Username:</label>
                <input type="text" id="username" name="username" value="John"/><br/>
                <label for="password">Password:</label>
                <input type="text" id="password" name="password" value="123CSC"/><br/><br/>
            </form>
            <Link to="/profile"><button type="button">LOGIN</button></Link>
        </div>
    ); 
}

