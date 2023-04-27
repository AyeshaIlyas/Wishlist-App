import {useState, useEffect, useContext} from "react";
import {useParams, Link} from "react-router-dom";
import { faArrowLeft } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import Item from "../Item/Item";
import "./Wishlist.css";
import { getItems, removeItem, updateItem, createItem} from "./../../services/itemService";
import { NewItemForm } from "../NewItemForm/NewItemForm";
import AuthContext from "../Contexts/AuthContext";
import { getWishlist, updateWishlist, unshareWishlist } from "../../services/wishlistService";
import { authWrapper } from "../../services/utils";
import ShareForm from "../ShareForm/ShareForm"
import Spinner from "../Utils/Spinner";
import UnshareForm from "../Unshare/UnshareForm";

export default function Wishlist() {
    const {wishlistId} = useParams();
    const [items, setItems] = useState([]);
    const [wishlist, setWishlist] = useState({});
    const [error, setError] = useState(null);
    const [feedback, setFeedback] = useState(null);
    const [creating, setCreating] = useState(false);
    const {setIsLoggedIn} = useContext(AuthContext);
    const [loading, setLoading] = useState(true);
    const [sharing, setSharing] = useState(false);
    const [unsharing, setUnsharing] = useState(false);

    
    useEffect(
        () => {
            const loadContent = async () => {
                // get wishlist data
                let safeGet = authWrapper(setIsLoggedIn, getWishlist);
                const wishlist = await safeGet(wishlistId, true);
                if (wishlist) {
                    setWishlist(wishlist);
                    // get item data
                    safeGet = authWrapper(setIsLoggedIn, getItems);
                    const items = await safeGet(wishlistId, true);
                    if (items) {
                        setItems(items);
                    } 
                }
                setLoading(false);
            }   
        loadContent();
    }, [setIsLoggedIn, wishlistId])


    const update = async (id, item) => {
        const safeUpdateItem = authWrapper(setIsLoggedIn, updateItem);
        const response = await safeUpdateItem(wishlistId, id, item);
        if (response) {
            if (response.success) {
                const newItems = items.map(i => {
                    return i.id === id ? response.item : i;
                });
                setItems(newItems);
                setError(null);
            } else {
                // error
                setError("A mishap occurred when attempting to update your item...")
            }
        }
    }

    const remove = async id => {
        const safeRemoveItem = authWrapper(setIsLoggedIn, removeItem);
        const response = await safeRemoveItem(wishlistId, id);
        if (response) {
            if (response.success) {
                const newItems = items.filter(i => i.id !== id);
                setItems(newItems);
                setError(null);
            } else {
                // error
                setError("We couldn't delete your item :<...")
            }
        }
    }

    const create = async (item) => {

        const safeCreateItem = authWrapper(setIsLoggedIn, createItem);
        const response = await safeCreateItem(wishlistId, item);
        console.log(item);
        if (response) {
            if (response.success) {
                setItems([...items, response.item])
                console.log(response.item);
                setError(null);
            } else {
                setError("We couldn't create your item :<...")
            }
        }
        
    }

    const share = async (email) => {
        const safeUpdateWishlist = authWrapper(setIsLoggedIn, updateWishlist);
        const response = await safeUpdateWishlist(wishlistId, {
            "sharedWith": [email]
        });
        console.log(response);
        if (response) {
            if (response.success) {
                if (!wishlist.sharedWith.includes(email)) {
                    wishlist.sharedWith.push(email);
                    setFeedback("Added " + email);
                    setTimeout(() => {
                        setFeedback(null);
                    }, 3000);
                } else {
                    setFeedback(email + " has already been added");
                    setTimeout(() => {
                        setFeedback(null);
                    }, 3000);
                }
            } else {
                setError("We couldn't share with the list with "+email)
                setTimeout(() => {
                    setError(null);
                }, 3000);
            }
        }
    }

   const unshare = async (email) => {
        const safeUpdateWishlist = authWrapper(setIsLoggedIn, unshareWishlist);
        const response = await safeUpdateWishlist(wishlistId, 
            email
        );
        console.log(response);
        if (response) {
            if (response.success) {
                if (wishlist.sharedWith.includes(email)) {
                    
                    const index = wishlist.sharedWith.indexOf(email);
                    wishlist.sharedWith.splice(index, 1);
                    

                    setFeedback("Removed " + email);
                    setTimeout(() => {
                        setFeedback(null);
                    }, 3000);
                } else {
                    setFeedback(email + "is not being shared with");
                    setTimeout(() => {
                        setFeedback(null);
                    }, 3000);
                }
            } else {
                setError("We couldn't share with the list with "+email)
                setTimeout(() => {
                    setError(null);
                }, 3000);
            }
        }
    }

    const handleShowForm = () => {
        window.scrollTo(0, 0);
        setCreating(true);
    }

    const cancel = () => {
        setCreating(false);
    }

    const handleShareForm = () => {
        window.scrollTo(0, 0);
        setSharing(true);
    }

    const cancelShare = () => {
        setSharing(false);
    }

    const handleUnshareForm = () => {
        window.scrollTo(0, 0);
        setUnsharing(true);
    }

    const cancelUnshare = () => {
        setUnsharing(false);
    }

    const [isOpen, setIsOpen] = useState(false);
    const [activeAdd, setActiveAdd] = useState(false);
    const handleAddClick = () => {
        setActiveAdd(!activeAdd);
    }
    const [activeRemove, setActiveRemove] = useState(false);
    const handleRemoveClick = () => {
        setActiveRemove(!activeRemove);
    }

    const displayContent = () => {
        return (
            <>
                <header className="Wishlist-header">
                    <Link id="back" to="/wishlists">
                            <FontAwesomeIcon icon={faArrowLeft}/>
                            <span>All Wishlists</span>
                    </Link>
                    <h1>{wishlist.name}</h1>
                </header>

                <div className="Wishlist-content-container">
                    <div className='Wishlist-share-container'>
                        {isOpen &&
                        <div className="Wishlist-share">
                            <div className="Wishlist-share-form-container">
                                <div className="Wishlist-share-form-buttons-container">
                                    <button className='Wishlist-share-add-button'
                                    onClick={()=> {
                                        handleAddClick();
                                        setActiveRemove(false);
                                        handleShareForm();
                                        // add an onClick function that will close the other form that way it swaps instead of opening both
                                        // and an onClick function that closes the current form like a toggle
                                    }}
                                    style={{ backgroundColor: activeAdd ? "#a6bbd2" : "rgb(213, 213, 213)" }}
                                    >Add an Email</button>


                                    <button className='Wishlist-share-remove-button'
                                    onClick={()=> {
                                        handleRemoveClick();
                                        setActiveAdd(false);
                                        handleUnshareForm();
                                        // add an onClick function that will close the other form that way it swaps instead of opening both
                                        // and an onClick function that closes the current form like a toggle
                                    }}
                                    style={{ backgroundColor: activeRemove ? "#a6bbd2" : "rgb(213, 213, 213)" }}
                                    >Remove an Email</button>
                                </div><br/>
                                {sharing && <ShareForm share={share} cancel={cancelShare}/>}
                                {unsharing && <UnshareForm unshare={unshare} cancel={cancelUnshare}/>}
                            </div>
                            <div className='Wishlist-shared-with'>
                                <h2>Shared with</h2>
                                {wishlist.sharedWith.length > 0 && <h4> {wishlist.sharedWith.join(' | ')}</h4>}
                            </div>
                            <div className="Wishlist-share-exit-button-container">
                                <button className='Wishlist-share-exit-button' onClick={() => setIsOpen(!isOpen)}>X</button>
                            </div>
                        </div>
                        }
                    </div>

                    {error && <p className="Wishlist-error">{error}</p>}
                    {feedback && <p className="Wishlist-feedback">{feedback}</p>}
                    {items.length === 0 && <p>No items yet...</p>}
                    {creating && <NewItemForm create={create} cancel={cancel}/>}
                    {items.length !== 0 && 
                        <div className="Wishlist-items">
                            {items.map(i => <Item {...i} key={i.id} remove={remove} update={update}/>)}
                        </div>
                    }
                </div>
                <button id="Wishlist-new-button" onClick={handleShowForm}>New Item</button>
                <button className='Wishlist-share-button' onClick={() => {
                    setIsOpen(!isOpen);
                    setActiveRemove(false);
                    setActiveAdd(false);
                    // add an onClick function that closes any open from
                    }}>Share</button>
            </>
        );
    }

    return (
        <div className="Wishlist">
            <div className="Wishlist-container">
            {
                loading ? (
                    <div>
                        <p className="Wishlist-msg">Loading...</p>
                        <Spinner/>
                    </div>
                )
                : displayContent()
            }
            </div>
        </div>
    );
}
