import axios from "axios";

export const getProfile = async (token) => {
    try {
        const res = await axios.get("http://127.0.0.1:9081/api/user", 
        {
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            }
        });
        const profile = res.data;
        return profile;
    } catch (e) {
        console.log(e);
    } 
}
