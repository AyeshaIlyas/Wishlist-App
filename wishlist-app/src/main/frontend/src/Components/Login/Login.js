import { useRef, useState, useEffect, useContext } from 'react';
import { Link, Navigate} from 'react-router-dom';
import './Login.css';
import axios from 'axios';
import AuthContext from '../Contexts/AuthContext';
import Cookies from 'js-cookie';
import Spinner from '../Utils/Spinner';

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
    const [loading, setLoading] = useState(false);
    const [disabled, setDisabled] = useState(false);

    const errRef = useRef();

    const [email, setEmail] = useState('');
    const [pwd, setPwd] = useState('');
    const [errMsg, setErrMsg] = useState('');

    useEffect(() => {
        setErrMsg('');
    }, [email, pwd])

    const handleSubmit = async (e) => {
        console.log("Clicked!!!");
        e.preventDefault();
        setLoading(true);
        setDisabled(true);
        
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
        setLoading(false);
        setDisabled(false);
        
    }

    return (
        authState.isLoggedIn
        ? <Navigate to="/wishlists" replace/>
        : (
            <div className="Login">
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
                        <button className='Login-button' disabled={disabled}>Login</button>
                    </form>
                    <p className="Login-regLink">
                        Need an Account?<br />
                        <span className="line">
                            <Link to="/register">Register</Link>
                        </span>
                    </p>
                    {loading && <Spinner/>}
                </section> 
            </div>
        )
    );
}

export default Login