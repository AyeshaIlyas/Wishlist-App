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
        console.log(response.data.cookie);
        // since cookie isnt automatically being set....
        const {name, value, ...info} = response.data.cookie;
        Cookies.set(name, value, info);
        return {success: true};
    } catch (err) {
        console.log(err)
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
                setErrMsg('Login Failed, Incorrect Email or Password');
            }
            errRef.current.focus();
        }
    }

    return (
        authState.isLoggedIn
        ? <Navigate to="/wishlists" replace/>
        : (
            <div className="Login">
                {/* <div className="Login-blue"> 
                    <h1> More Stuff To Say</h1>
                    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, 
                    sed do eiusmod tempor incididunt ut labore et dolore magna 
                    aliqua. Ut enim ad minim veniam, quis nostrud exercitation 
                    ullamco laboris nisi ut aliquip ex ea commodo consequat. </p>
                </div> */}

            <section className="Login-container">
                <p ref={errRef} className={errMsg ? "errmsg" : "offscreen"} aria-live="assertive">{errMsg}</p>
                <h1>Login</h1>
                <form className="Login-form" onSubmit={handleSubmit}>
                    <label htmlFor="email"></label>
                    <input
                        type="email"
                        id="email"
                        placeholder='Email'
                        onChange={(e) => setEmail(e.target.value)}
                        value={email}
                        required
                    />

                    <label htmlFor="password"></label>
                    <input
                        type="password"
                        id="password"
                        placeholder='Password'
                        onChange={(e) => setPwd(e.target.value)}
                        value={pwd}
                        required
                    />
                    <button className='Login-button'>Login</button>
                </form>
                <p className="Login-regLink">
                    Need an Account?<br />
                    <span className="line">
                        <Link to="/register">Register</Link>
                    </span>
                </p>
            </section> 
            </div>
        )
    );
}

export default Login