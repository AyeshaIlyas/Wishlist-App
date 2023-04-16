import {v4 as uuid } from "uuid";

const items =  [
    {
        id: uuid(),
        name: "Wireless Earbuds",
        price: 22.99,
        location: "https://www.amazon.com/Wireless-Bluetooth-Headphone-Waterproof-Earphones/dp/B09YCXKCXQ?pf_rd_r=QNK3WGESKWBSGG76QC8V&pf_rd_t=Events&pf_rd_i=deals&pf_rd_p=51b56f9e-17c8-42b1-84d1-5ea4a28ff19a&pf_rd_s=slot-15&ref=dlx_deals_gd_dcl_tlt_10_28d6e176_dt_sl15_9a",
        isBought: true,
        gifter: "hello@gmail.com"
    },
    {
        id: uuid(),
        name: "iRobot Roomba",
        price: 189.00,
        location: "https://www.amazon.com/iRobot-Vacuum-Wi-Fi-Connectivity-Carpets-Self-Charging/dp/B08SP5GYJP?ref_=Oct_DLandingS_D_1d994b94_60",
        isBought: true,
        gifter: "me@example.com"
    },
    {
        id: uuid(),
        name: "Watch",
        price: 29.99,
        location: "https://www.amazon.com/Stretchy-Compatible-Adjustable-Braided-Wristbands/dp/B0B936LTKW?pf_rd_r=QNK3WGESKWBSGG76QC8V&pf_rd_t=Events&pf_rd_i=deals&pf_rd_p=51b56f9e-17c8-42b1-84d1-5ea4a28ff19a&pf_rd_s=slot-15&ref=dlx_deals_gd_dcl_img_15_2e92850f_dt_sl15_9a"
    },
    {
        id: uuid(),
        name: "Amazon Fire HD Tablet",
        price: 119.99,
        location: "https://www.amazon.com/Fire-HD-10-Plus-tablet/dp/B08F6FYN6B?ref_=Oct_DLandingS_D_f9228686_62",
    }
]

export default items;