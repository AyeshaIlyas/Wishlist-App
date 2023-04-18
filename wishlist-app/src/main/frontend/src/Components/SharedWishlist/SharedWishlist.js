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
import { getProfile } from "../../services/profileService";
import { authWrapper } from "../../services/utils";
import { buyItem } from "../../services/sharedWishlistService";

export default function SharedWishlist() {
    const {wishlistId} = useParams();
    const {setIsLoggedIn} = useContext(AuthContext);
    const [sWishlist, setSWishlist] = useState({});
    const [items, setItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [user, setUser] = useState({}); 
    const [error, setError] = useState(null);

    useEffect(
        () => {
            const getUser = async () => {
                const safeGetProfile = authWrapper(setIsLoggedIn, getProfile);
                const user = await safeGetProfile();
                if (user) {
                    setUser(user);
                }
            }

            const loadContent = async () => {
                // get wishlist data
                let safeGet = authWrapper(setIsLoggedIn, getWishlist);
                const wishlist = await safeGet(wishlistId, false);
                if (wishlist) {
                    setSWishlist(wishlist);
                    // get item data
                    safeGet = authWrapper(setIsLoggedIn, getItems);
                    const items = await safeGet(wishlistId, false);
                    setItems(items);
                }
                setLoading(false);
            }   

            const loadData = async () => {
                getUser();
                await loadContent();
            }

        loadData();
        setInterval(loadContent, 10 * 1000);
    }, [setIsLoggedIn, wishlistId]);



    const buy = async (itemId, buy) => {
        const safeBuyItem = authWrapper(setIsLoggedIn, buyItem);
        const response = await safeBuyItem(wishlistId, itemId, buy);
        if (response) {
            if (response.success) {
                const newItems = items.map(i => i.id === itemId ? {...i, purchased: buy, gifter: buy ? user.email : null} : i);
                setItems(newItems);
            } else {
                // error
                setError("We couldn't buy the item :<...")
            }
        }        
    }
    
    const displayContent = () => {
        return (
            <div className="SharedWishlist-container">
                {error && <p>error</p>}
                {loading ? <p className="SharedWishlist-error">Loading...</p> : (
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