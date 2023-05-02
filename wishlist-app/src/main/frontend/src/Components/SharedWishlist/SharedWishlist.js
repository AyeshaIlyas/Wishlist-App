import { useState, useEffect, useCallback } from "react";
import { Link, useParams } from "react-router-dom";
import SharedItem from "../SharedItem/SharedItem";
import "./SharedWishlist.css"
import { faArrowLeft } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { getWishlist } from "../../services/wishlistService";
import { getItems } from "../../services/itemService";
import { useContext } from "react";
import AuthContext from "../Contexts/AuthContext";
import { getProfile } from "../../services/profileService";
import { authWrapper } from "../../services/utils";
import { buyItem } from "../../services/sharedWishlistService";
import Spinner from "../Utils/Spinner";

export default function SharedWishlist(props) {
    const {wishlistId} = useParams();
    const {setIsLoggedIn} = useContext(AuthContext);
    const [sWishlist, setSWishlist] = useState({});
    const [items, setItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [user, setUser] = useState({}); 

    const loadContent = useCallback(async (setIsLoggedIn) => {
        // get wishlist data
        let safeGet = authWrapper(setIsLoggedIn, getWishlist);
        let res = await safeGet(wishlistId, false);
        if (res.success) {
            setSWishlist(res.data);

            // get item data
            safeGet = authWrapper(setIsLoggedIn, getItems);
            res = await safeGet(wishlistId, false);
            if (res.success) {
                setItems(res.data);
            } else {
                console.log("STATUS CODE: " + res.code)
                props.announce("We could load the data for this wishlist :<...", "error");
            }

        } else {
            console.log("STATUS CODE: " + res.code)
            props.announce("We could load wishlist data :<...", "error");
        }
        setLoading(false);
    }, [props, wishlistId])

    useEffect(
        () => {
            const getUser = async () => {
                const safeGetProfile = authWrapper(setIsLoggedIn, getProfile);
                const res = await safeGetProfile();
                if (res.success) {
                    setUser(res.data);
                } else {
                    console.log("STATUS CODE: " + res.code)
                    props.announce("We could not fetch your profile info :<...", "error");
                }
            }

            getUser();
            loadContent(setIsLoggedIn);
    }, [setIsLoggedIn, loadContent, props]);

    
    const buy = async (itemId, buy) => {
        const safeBuyItem = authWrapper(setIsLoggedIn, buyItem);
        const res = await safeBuyItem(wishlistId, itemId, buy);
        if (res.success) {
            const newItems = items.map(i => i.id === itemId ? {...i, purchased: buy, gifter: buy ? user.email : null} : i);
            setItems(newItems);
        } else {
            console.log("STATUS CODE: " + res.code)
            props.announce("Sorry, someone else already purchased this item :<...", "error");
            if (res.code === 403) {
                loadContent(setIsLoggedIn);
            }
        }     
    }
    
    const displayContent = () => {
        return (
            <div className="SharedWishlist-container">
                {loading ? (
                    <div>
                        <p className="SharedWishlist-error">Loading...</p>
                        <Spinner/>
                    </div>
                ) : (
                <>
                    <header className="SharedWishlist-header">
                        <Link className="SharedWishlist-header-item SharedWishlist-list-link-back" id="back" to="/shared">
                                <FontAwesomeIcon icon={faArrowLeft}/>
                                <span><h2>Shared Wishlists</h2></span>
                        </Link>
                        <h1 className="SharedWishlist-header-item SharedWishlist-list-title">{sWishlist.name}</h1>
                        <h2 className="SharedWishlist-header-item SharedWishlist-header-owner">Shared by {sWishlist.owner}</h2>
                    </header>
                    
                    <div className="SharedWishlist-content-container">
                        {items.length === 0 && <p>No items yet...</p>}
                        {items.length > 0 && 
                            <div className="SharedWishlist-items">
                                {items.map(i => {
                                    // show button if not bought or this user bought it
                                    const showBuyButton = !i.purchased || user.email === i.gifter;
                                    return <SharedItem {...i} key={i.id} showBuyButton={showBuyButton} buy={buy}/>
                                })}
                            </div>
                        }
                    </div>
                </>
                )}
            </div>
        );
    }

    return (
        <div className="SharedWishlist">
            {displayContent()}
        </div>
    );
}