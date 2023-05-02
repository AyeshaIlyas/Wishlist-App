import axios from "axios";

export const wishlistApp = axios.create({
    baseURL: "http://127.0.0.1:9081/api/"
})

export const authServer = axios.create({
    baseURL: "http://127.0.0.1:9082/api/"
})


