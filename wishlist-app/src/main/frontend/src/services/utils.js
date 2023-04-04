import axios from "axios";
import Cookies from "js-cookie";

// get new jwt from auth server. only successful if valid cookie exists
const getNewToken = async (setLoggedIn) => {
    try {
        // get new token
        const response = await axios.get("http://127.0.0.1:9082/api/login/refresh", 
            {
                headers: {"Content-Type": "application/json"},
                withCredentials: true
            }
        );
        // save token
        const token = response.data.token;
        sessionStorage.setItem("token", token);
        console.log("Successfully got new token");
        return token;
    } catch (e) {
        // if (!e.response || e.reponse.status === 401 || e.response.status == 500) 
        console.log("You are not logged in");
        Cookies.remove("auth-session")
        sessionStorage.removeItem("token");
        setLoggedIn(false);
        return null;
    }
}

// get whatever value is stores in session storage under "token" key or if that key does not 
// exist, get a new jwt
const getToken =  async (setLoggedIn) => {
    const token = sessionStorage.getItem("token");
    return token ? token : await getNewToken(setLoggedIn);
}


// returns a new function that wraps the arg fn enhancing the arg fn by giving it token retrieval 
// ability and allows it to handle 401 from the app-server and auth-server
export const authWrapper = (setLoggedIn, fn) => {
    const wrappedFunction = async (...args) => {
        const token = await getToken(setLoggedIn);

        if (!token) {
            console.log("The auth server didnt authorize you. Dont think you can hack this app!! Take that!!!")
            return null;
        }

        console.log("Making request...");

        try {
            const result = await fn(token, ...args);
            console.log("Successfully received response");
            console.log("DATA");
            console.log(result);
            return result;
        } catch(e) {
            console.log("Error: " + e.message);

            if (parseInt(e.message) === 401) {
                console.log("Jwt was invalid. Attempting refresh token request");

                // jwt is invalid, get refresh token 
                const newToken = await getNewToken(setLoggedIn);
                if (!newToken) {
                    console.log("Refresh failed. You are not logged in validly. Get out!")
                    return null;
                }

                // remake request
                const result = await fn(token, ...args);
                console.log("Succeeded on second attempt");
                console.log("DATA");
                console.log(result);
                return result;
            }
        }
    }
    return wrappedFunction;
}