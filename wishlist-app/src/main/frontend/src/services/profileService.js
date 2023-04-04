import axios from "axios";

export const getProfile = async (token) => {
    try {
        const res = await axios.get("http://127.0.0.1:9081/api/user", {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });
        return res.data;
    } catch (e) {
        console.log("Error: " + e);
        if (!e.response || e.response.status === 500) {
            console.log(!e.response ? "no server response" : "server error");
            return null;
        } else if (e.response.status === 401) {
            throw new Error(401);
        }
    }
}
