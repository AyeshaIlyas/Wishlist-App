import { useState, useEffect } from "react";
import { Link, useParams } from "react-router-dom";
import SharedItem from "../SharedItem/SharedItem";
import "./SharedWishlist.css"
import { faArrowLeft } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { getWishlist } from "../../services/wishlistService";
import { getItems } from "../../services/itemService";
import { useContext } from "react";
import AuthContext from "../Contexts/AuthContext";
import data from "./../Wishlist/items.js";
import { getProfile } from "../../services/profileService";
import { authWrapper } from "../../services/utils";

export default function SharedWishlist() {
    const {wishlistId} = useParams();
    const {setIsLoggedIn} = useContext(AuthContext);
    const [sWishlist, setSWishlist] = useState({});
    const [items, setItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [user, setUser] = useState({}); 

    useEffect(
        () => {
            const loadContent = async () => {
                const getUser = async () => {
                    const safeGetProfile = authWrapper(setIsLoggedIn, getProfile);
                    const user = await safeGetProfile();
                    if (user) {
                        setUser(user);
                    }
                }
                getUser();

                // get wishlist data
                // let safeGet = authWrapper(setIsLoggedIn, getWishlist);
                // const wishlist = await safeGet(wishlistId);
                const wishlist = {
                    id: 100,
                    name: "W1",
                    owner: "Maryam",
                    itemCount: 0
                }
                if (wishlist) {
                    setSWishlist(wishlist);
                    // get item data
                    // safeGet = authWrapper(setIsLoggedIn, getItems);
                    // const items = await safeGet(wishlistId);
                    const items = data;
                    if (items) {
                        setItems(items);
                    } 
                }
                setLoading(false);
            }   
        loadContent();
    }, [setIsLoggedIn, wishlistId])

    const buy = async (itemId, buy) => {
        // make request to buy or unbuy item
        const newItems = items.map(i => i.id === itemId ? {...i, isBought: buy, gifter: buy ? user.email : null} : i);
        setItems(newItems);
    }
    
    const displayContent = () => {
        return (
            <div className="SharedWishlist-container">
                <header className="SharedWishlist-header">
                    <Link id="back" to="/shared">
                            <FontAwesomeIcon icon={faArrowLeft}/>
                            <span>All Shared Wishlists</span>
                    </Link>
                    <h1>{sWishlist.name}</h1>
                    <p>Shared by {sWishlist.owner}</p>
                </header>
                <div className="SharedWishlist-content-container">
                    {items.length === 0 && <p>No items yet...</p>}
                    {items.length !== 0 && 
                        <div className="SharedWishlist-items">
                            {items.map(i => {
                                // show button if not bought or this user bought it
                                const showBuyButton = !i.isBought || user.email === i.gifter;
                                return <SharedItem {...i} key={i.id} showBuyButton={showBuyButton} buy={buy}/>
                            })}
                        </div>
                    }
                </div>
            </div>
        );
    }

    return (
        <div className="SharedWishlist">
            {loading ? <p>Loading...</p> : <>{displayContent()}</>}
        </div>
    );
}