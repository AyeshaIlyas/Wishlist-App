import { authServer, wishlistApp } from "./configuredAxios";

import Cookies from "js-cookie";

export const login = async (creds) => {
    try {
        const response = await authServer.post("/login",
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

export const register = async (data) => {
    try {
        // register in auth-server
        let response = await authServer.post("/register",
            data,
            {headers: {'Content-Type': 'application/json'}}
        );
        const registrationToken = response.data.token;
        // add info to wishlist-app
        response = await wishlistApp.post("/user",
            data,
            {headers: {'Content-Type': 'application/json', "Authorization": "Bearer " + registrationToken} }
        );
        return {success: true};
    } catch (err) {
        console.log(err);
        return {success: false, 
                status: !err.response 
                ? 0 
                : err.response.status};
    }

}
