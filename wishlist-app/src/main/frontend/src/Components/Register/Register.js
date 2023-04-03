import { useRef, useState, useEffect, useContext } from "react";
import { faCheck, faTimes, faInfoCircle } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {Link, Navigate, useNavigate} from "react-router-dom";
import './Register.css';
import axios from "axios";
import AuthContext from "../Contexts/AuthContext";

const FNAME_REGEX = /[A-Za-z]{1,}/;
const LNAME_REGEX = /[A-Za-z'-]{1,}/;
const EMAIL_REGEX = /^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$/;
const PWD_REGEX = /^\S{5,}/;
// const REGISTER_URL = '/register';

const register = async (data) => {
    try {
        console.log("In register");
        // register in auth-server
        let response = await axios.post("http://127.0.0.1:9082/api/register",
            data,
            {headers: {'Content-Type': 'application/json'}}
        );
        const registrationToken = response.data.token;

        // add info to wishlist-app
        response = await axios.post("http://127.0.0.1:9081/api/user",
            data,
            {headers: {'Content-Type': 'application/json', "Authorization": "Bearer " + registrationToken} }
        );
        return {success: true};
    } catch (err) {
        return {success: false, 
                status: !err.response 
                ? 0 
                : err.response.status};
    }

}

const Register = () => {
    const navigate = useNavigate();
    const errRef = useRef();
    const authState = useContext(AuthContext);

    const [firstName, setFirstName] = useState('');
    const [validFirstName, setValidFirstName] = useState(false);

    const [lastName, setLastName] = useState('');
    const [validLastName, setValidLastName] = useState(false);

    const [email, setEmail] = useState('');
    const [validEmail, setValidEmail] = useState(false);
    const [emailFocus, setEmailFocus] = useState(false);

    const [pwd, setPwd] = useState('');
    const [validPwd, setValidPwd] = useState(false);
    const [pwdFocus, setPwdFocus] = useState(false);

    const [errMsg, setErrMsg] = useState('');

    useEffect(() => {
        setValidFirstName(FNAME_REGEX.test(firstName));
    }, [firstName])

    useEffect(() => {
        setValidLastName(LNAME_REGEX.test(lastName));
    }, [lastName])

    useEffect(() => {
        setValidEmail(EMAIL_REGEX.test(email));
    }, [email])

    useEffect(() => {
        setValidPwd(PWD_REGEX.test(pwd));
    }, [pwd])

    useEffect(() => {
        setErrMsg('');
    }, [pwd])

    const handleSubmit = async (e) => {
        e.preventDefault();
        // if button enabled with JS hack
        const v1 = PWD_REGEX.test(pwd);
        const v2 = FNAME_REGEX.test(firstName);
        const v3 = LNAME_REGEX.test(lastName);
        const v4 = EMAIL_REGEX.test(email);
        if (!v1 || !v2 || !v3 || !v4) {
            setErrMsg("Invalid Entry");
            return;
        }
        
        const result = await register({email, firstName, lastName, password: pwd, roles: ["user"]})
        if (result.success) {
            setFirstName('');
            setLastName('');
            setEmail('');
            setPwd('');
            navigate("/login");
        } else {
            if (result.status === 0) {
                setErrMsg('No Server Response');
            } else {
                setErrMsg('Registration Failed')
            }
            errRef.current.focus();
        }
    }

    return (
        authState.isLoggedIn
        ? <Navigate to="/wishlists"  replace/>
        : (
            <div className="Register">
                <div className="blue"> 
                    <h2>More Stuff To Say</h2>
                    <p>"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris 
                        nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident,
                        unt in culpa qui officia deserunt mollit anim id est laborum."</p>
                </div>
            <section className="Reigster-container">
                <p ref={errRef} className={errMsg ? "errmsg" : "offscreen"} aria-live="assertive">{errMsg}</p>
                <h1>Register</h1>
                <form onSubmit={handleSubmit}>
                    <label htmlFor="firstName">
                             
                        <FontAwesomeIcon icon={faCheck} className={validFirstName ? "valid" : "hide"} />
                        <FontAwesomeIcon icon={faTimes} className={validFirstName || !firstName ? "hide" : "invalid"} />
                    </label>
                    <input
                        type="text"
                        id="firstName"
                        placeholder="First name"
                        onChange={(e) => setFirstName(e.target.value)}
                        value={firstName}
                        required
                        aria-invalid={validFirstName ? "false" : "true"}
        
                    />


                    <label htmlFor="lastName">
                               
                        <FontAwesomeIcon icon={faCheck} className={validLastName ? "valid" : "hide"} />
                        <FontAwesomeIcon icon={faTimes} className={validLastName || !lastName ? "hide" : "invalid"} />
                    </label>
                    <input
                        type="text"
                        id="lastName"
                        placeholder="Last name"
                        onChange={(e) => setLastName(e.target.value)}
                        value={lastName}
                        required
                        aria-invalid={validLastName ? "false" : "true"}
                    />


                    <label htmlFor="Email">
                       
                        <FontAwesomeIcon icon={faCheck} className={validEmail ? "valid" : "hide"} />
                        <FontAwesomeIcon icon={faTimes} className={validEmail || !email ? "hide" : "invalid"} />
                    </label>
                    <input
                        type="email"
                        id="email"
                        placeholder="email"
                        onChange={(e) => setEmail(e.target.value)}
                        value={email}
                        required
                        aria-invalid={validEmail ? "false" : "true"}
                        aria-describedby="emailnote"
                        onFocus={() => setEmailFocus(true)}
                        onBlur={() => setEmailFocus(false)}
                    />
                    <p id="emailnote" className={emailFocus && email && !validEmail ? "instructions" : "offscreen"}>
                        <FontAwesomeIcon icon={faInfoCircle} />
                        Email must be in the format: example<span aria-label="at symbol">@</span>email.com
                    </p>


                    <label htmlFor="password">
                        
                        <FontAwesomeIcon icon={faCheck} className={validPwd ? "valid" : "hide"} />
                        <FontAwesomeIcon icon={faTimes} className={validPwd || !pwd ? "hide" : "invalid"} />
                    </label>
                    <input
                        type="password"
                        id="password"
                        placeholder="Password"
                        onChange={(e) => setPwd(e.target.value)}
                        value={pwd}
                        required
                        aria-invalid={validPwd ? "false" : "true"}
                        aria-describedby="pwdnote"
                        onFocus={() => setPwdFocus(true)}
                        onBlur={() => setPwdFocus(false)}
                    />
                    <p id="pwdnote" className={pwdFocus && !validPwd ? "instructions" : "offscreen"}>
                        <FontAwesomeIcon icon={faInfoCircle} />
                        Minimum 5 characters.
                    </p>

                    <button id="registerBtn" disabled={!validFirstName || !validLastName || !validEmail || !validPwd ? true : false}>Sign Up</button>
                </form>
                <p>
                    Already registered?<br />
                    <span className="line">
                        {/*put router link here*/}
                        <Link to="/login">Login</Link>
                    </span>
                </p>
            </section>
            </div>
        )
    );
}

export default Register