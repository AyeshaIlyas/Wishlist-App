import { wishlistApp } from "./configuredAxios";
import { requestHelper } from "./utils";

export const createWishlist = requestHelper(async (token, wishlist) => {
    const res = await wishlistApp.post("/wishlists",
    wishlist,
    {
        headers: {"Authorization": `Bearer ${token}`}
    });
    return {success: true, data: res.data};
})

export const getWishlists = requestHelper(async (token) => {
    const res = await wishlistApp.get("/wishlists", {
        headers: {"Authorization": `Bearer ${token}`}
    });
    return {success: true, data: res.data}
})

export const getWishlist = requestHelper(async (token, wishlistId, isOwner) => {
    const res = await wishlistApp.get(`/wishlists/${wishlistId}`, {
        params : {isOwner},
        headers: {
            "Authorization": `Bearer ${token}`
        }
    });
    return {success: true, data: res.data}
})

export const updateWishlist = requestHelper(async (token, wishlistId, newWishlist) => {
    const res = await wishlistApp.patch(`/wishlists/${wishlistId}`,
    newWishlist, 
    {
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        }
    });
    return {success: true, data: res.data};
})

export const removeWishlist = requestHelper(async (token, wishlistId) => {
    await wishlistApp.delete(`/wishlists/${wishlistId}`,
    {
        headers: {
            "Authorization": `Bearer ${token}`
        }
    });
    return {success: true};
})

export const unshareWishlist = requestHelper(async (token, wishlistId, email) => {
    const res = await wishlistApp.delete(`/wishlists/${wishlistId}/shared-with/${email}`,
        { 
            headers: {"Authorization": `Bearer ${token}` }       
        }
    );
    return { success: true, wishlist: res.data };
})


