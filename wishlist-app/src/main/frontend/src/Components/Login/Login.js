import { useRef, useState, useEffect, useContext } from 'react';
import { Link, Navigate} from 'react-router-dom';
import './Login.css';
import axios from 'axios';
import AuthContext from '../Contexts/AuthContext';
import Cookies from 'js-cookie';

const login = async (creds) => {
    try {
        const response = await axios.post("http://127.0.0.1:9082/api/login",
            creds,
            {headers: {'Content-Type': 'application/json'}}
        );
        // add jwt to session storage
        sessionStorage.setItem("token", response.data.token);
        // since cookie isnt automatically being set....
        Cookies.set("auth-session", response.data.cookie);
        return {success: true};
    } catch (err) {
        return {success: false, 
                status: !err.response 
                ? 0 
                : err.response.status};
    }

}

const Login = () => {
    const authState = useContext(AuthContext);

    const errRef = useRef();

    const [email, setEmail] = useState('');
    const [pwd, setPwd] = useState('');
    const [errMsg, setErrMsg] = useState('');

    useEffect(() => {
        setErrMsg('');
    }, [email, pwd])

    const handleSubmit = async (e) => {
        e.preventDefault();
        const result = await login({email, password: pwd});
        if (result.success) {
            setEmail('');
            setPwd('');
            authState.setIsLoggedIn(true);
        } else {
            if (result.status === 0) {
                setErrMsg("No Server Response");
            } else if (result.status === 400) {
                setErrMsg('Missing Username or Password');
            } else {
                setErrMsg('Login Failed');
            }
            errRef.current.focus();
        }
    }

    return (
        authState.isLoggedIn
        ? <Navigate to="/wishlists" replace/>
        : (
            <section>
                <p ref={errRef} className={errMsg ? "errmsg" : "offscreen"} aria-live="assertive">{errMsg}</p>
                <h1>Login</h1>
                <form onSubmit={handleSubmit}>
                    <label htmlFor="email">Email:</label>
                    <input
                        type="email"
                        id="email"
                        onChange={(e) => setEmail(e.target.value)}
                        value={email}
                        required
                    />

                    <label htmlFor="password">Password:</label>
                    <input
                        type="password"
                        id="password"
                        onChange={(e) => setPwd(e.target.value)}
                        value={pwd}
                        required
                    />
                    <button id='loginBtn'>Login</button>
                </form>
                <p>
                    Need an Account?<br />
                    <span className="line">
                        {/*put router link here*/}
                        <Link to="/register">Register</Link>
                    </span>
                </p>
            </section> 
        )
    );
}

export default Login