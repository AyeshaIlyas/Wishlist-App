import {wishlistApp} from "./configuredAxios";
import {requestHelper} from "./utils";

export const getItems = requestHelper(async (token, wishlistId, isOwner) => {
    const res = await wishlistApp.get(`/wishlists/${wishlistId}/items`, {
        params: {isOwner: isOwner},
        headers: {"Authorization": `Bearer ${token}`}
    });
    return {success: true, data: res.data};
})

export const updateItem = requestHelper(async (token, wishlistId, itemId, newItem) => {
    const res = await wishlistApp.put(`/wishlists/${wishlistId}/items/${itemId}`,
    newItem, 
    {
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        }
    });
    return {success: true, data: res.data};
})

export const removeItem = requestHelper(async (token, wishlistId, itemId) => {
    await wishlistApp.delete(`/wishlists/${wishlistId}/items/${itemId}`,
    {
        headers: {"Authorization": `Bearer ${token}`}
    });
    return {success: true};
})

export const createItem = requestHelper(async (token, wishlistId, item) => {
    const res = await wishlistApp.post(`/wishlists/${wishlistId}/items/`,
    item,
    {
        headers: {"Authorization": `Bearer ${token}`}
    });
    return {success: true, data: res.data};
})
