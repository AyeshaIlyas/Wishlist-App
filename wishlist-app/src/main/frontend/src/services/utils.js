import axios from "axios";
import Cookies from "js-cookie";

export const getNewToken = async (setLoggedIn) => {
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
        return token;
    } catch (e) {
        // if (!e.response || e.reponse.status === 401 || e.response.status == 500) 
        Cookies.remove("auth-session")
        setLoggedIn(false);
    }
}

export const getToken =  async (setLoggedIn) => {
    const token = sessionStorage.getItem("token");
    return token ? token : await getNewToken(setLoggedIn);
}