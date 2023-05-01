import axios from "axios";

export const getSharedWishlists = async (token) => {
    try {
        const res = await axios.get("http://127.0.0.1:9081/api/shared-wishlists", {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });
        return res.data;
    } catch (e) {
        console.log(e);
        if (!e.response || e.response.status === 500) {
            console.log(!e.response ? "no server response" : "server error");
            return [];
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
        console.log(e);
        if (!e.response || e.response.status === 500) {
            console.log(!e.response ? "no server response" : "server error");
            return [];
        } else if (e.response.status === 401) {
            throw new Error(401);
        }
    }
}

export const buyItem = async (token, wishlistId, itemId, shouldBuy) => {
    try {
        const res = await axios.patch(`http://127.0.0.1:9081/api/shared-wishlists/${wishlistId}/items/${itemId}`,
        null, 
        {
            params: {buy: shouldBuy},
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            }
        });
        return {success: true, wishlist: res.data};
    } catch (e) {
        console.log(e);
        if (!e.response || e.response.status === 500) {
            console.log(!e.response ? "no server response" : "server error");
            return {success: false, statusCode: !e.response ? 0 : 500};
        } else if (e.response.status === 401) {
            throw new Error(401);
        }
    }
}

export const removeSelfFromList = async (token, wishlistId) => {
    try {
        const res = await axios.delete(`http://127.0.0.1:9081/api/shared-wishlists/${wishlistId}`,
        {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });
        return {success: true};
    } catch (e) {
        console.log(e);
        if (!e.response || e.response.status === 500) {
            console.log(!e.response ? "no server response" : "server error");
            return {success: false, statusCode: !e.response ? 0 : 500};
        } else if (e.response.status === 401) {
            throw new Error(401);
        } else if (e.response.status === 400) {
            return {success: false, statusCode: 400};
        }
    }
}