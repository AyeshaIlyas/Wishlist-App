import { wishlistApp } from "./configuredAxios";
import { requestHelper } from "./utils";

export const getSharedWishlists = requestHelper(async (token) => {
    const res = await wishlistApp.get("/shared-wishlists", {
        headers: {"Authorization": `Bearer ${token}`}
    });
    return {success: true, data: res.data}; // list of wishlist objects
})

export const buyItem = requestHelper(async (token, wishlistId, itemId, shouldBuy) => {
    const res = await wishlistApp.patch(`/shared-wishlists/${wishlistId}/items/${itemId}`,
        null, 
        {
            params: {buy: shouldBuy},
            headers: {"Authorization": `Bearer ${token}`}
        }
    );
    return {success: true, data: res.data}; // wishlist object
})

export const removeSelfFromList = async (token, wishlistId) => {
    await wishlistApp.delete(`/shared-wishlists/${wishlistId}`,
    {
        headers: {"Authorization": `Bearer ${token}`}
    });
    return {success: true};
}

