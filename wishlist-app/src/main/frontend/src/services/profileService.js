import { wishlistApp } from "./configuredAxios";
import { requestHelper } from "./utils";

export const getProfile = requestHelper(async (token) => {
    const res = await wishlistApp.get("/user", {
        headers: {"Authorization": `Bearer ${token}`}
    });
    return {success: true, data: res.data};
})
