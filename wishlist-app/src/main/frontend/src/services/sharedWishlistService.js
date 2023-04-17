import axios from "axios";

export const getSharedWishlists = async (token) => {
    try {
        const res = await axios.get("http://127.0.0.1:9081/api/sharedlists", {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });
        return res.data;
    } catch (e) {
        console.log("Error: " + e);
        if (!e.response || e.response.status === 500) {
            console.log(!e.response ? "no server response" : "server error");
            return [];
        } else if (e.response.status === 401) {
            throw new Error(401);
        }
    }
}

export const getWishlist = async (token, wishlistId) => {
    try {
        const res = await axios.get(`http://127.0.0.1:9081/api/wishlists/${wishlistId}`, {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });
        return res.data;
    } catch (e) {
        console.log("Error: " + e);
        if (!e.response || e.response.status === 500) {
            console.log(!e.response ? "no server response" : "server error");
            return {};
        } else if (e.response.status === 401) {
            throw new Error(401);
        }
    }
}

export const updateWishlist = async (token, wishlistId, newWishlist) => {
    try {
        const res = await axios.patch(`http://127.0.0.1:9081/api/wishlists/${wishlistId}`,
        newWishlist, 
        {
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            }
        });
        return {success: true, wishlist: res.data};
    } catch (e) {
        console.log("Error: " + e);
        if (!e.response || e.response.status === 500) {
            console.log(!e.response ? "no server response" : "server error");
            return {success: false, statusCode: !e.response ? 0 : 500};
        } else if (e.response.status === 401) {
            throw new Error(401);
        }
    }
}


export const getSharedItems = async (token, wishlistId) => {
    try {
        const res = await axios.get(`http://127.0.0.1:9081/api/wishlists/${wishlistId}/items?shared=true`, {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });
        return res.data;
    } catch (e) {
        console.log("Error: " + e);
        if (!e.response || e.response.status === 500) {
            console.log(!e.response ? "no server response" : "server error");
            return [];
        } else if (e.response.status === 401) {
            throw new Error(401);
        }
    }
}

