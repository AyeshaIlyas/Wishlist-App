import axios from "axios";

export const getItems = async (token, wishlistId, isOwner) => {
    try {
        const res = await axios.get(`http://127.0.0.1:9081/api/wishlists/${wishlistId}/items`, {
            params: {isOwner: isOwner},
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

export const updateItem = async (token, wishlistId, itemId, newItem) => {
    try {
        const res = await axios.put(`http://127.0.0.1:9081/api/wishlists/${wishlistId}/items/${itemId}`,
        newItem, 
        {
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            }
        });
        return {success: true, item: res.data};
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

export const removeItem = async (token, wishlistId, itemId) => {
    try {
        await axios.delete(`http://127.0.0.1:9081/api/wishlists/${wishlistId}/items/${itemId}`,
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
        }
    }
}

export const createItem = async (token, wishlistId, item) => {
    try {
        const res = await axios.post(`http://127.0.0.1:9081/api/wishlists/${wishlistId}/items/`,
        item,
        {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });
        return {success: true, item: res.data};
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
