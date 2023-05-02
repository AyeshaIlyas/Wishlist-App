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
import ConfirmationDialog from "../Utils/ConfirmationDialog/ConfirmationDialog";

export default function Wishlist(props) {
    const {wishlistId} = useParams();
    const [items, setItems] = useState([]);
    const [wishlist, setWishlist] = useState({});
    const [creating, setCreating] = useState(false);
    const {setIsLoggedIn} = useContext(AuthContext);
    const [loading, setLoading] = useState(true);
    const [sharing, setSharing] = useState(false);
    const [showDialog, setShowDialog] = useState(false);
    const [deleteItem, setDeleteItem] = useState({});

    
    useEffect(
        () => {
            const loadContent = async () => {
                // get wishlist data
                let safeGet = authWrapper(setIsLoggedIn, getWishlist);
                let res = await safeGet(wishlistId, true); // get wishlist object
                if (res.success) {
                    setWishlist(res.data);
                    // get item data
                    safeGet = authWrapper(setIsLoggedIn, getItems);
                    res = await safeGet(wishlistId, true);
                    if (res.success) {
                        setItems(res.data);
                    } else {
                        console.log("STATUS CODE: " + res.code)
                        props.announce("We couldnt load the wishlist items :<..", "error");
                    }
                }
                setLoading(false);
            }   
        loadContent();
    }, [setIsLoggedIn, wishlistId, props])


    const update = async (id, item) => {
        const safeUpdateItem = authWrapper(setIsLoggedIn, updateItem);
        const res = await safeUpdateItem(wishlistId, id, item);
        if (res.success) {
            const newItems = items.map(i => {
                return i.id === id ? res.data : i;
            });
            setItems(newItems);
        } else {
            console.log("STATUS CODE: " + res.code)
            props.announce("A mishap occurred when attempting to update your item...", "error");
        }
        
    }

    const remove = async () => {
        const safeRemoveItem = authWrapper(setIsLoggedIn, removeItem);
        const res = await safeRemoveItem(wishlistId, deleteItem.id);
        if (res.success) {
            const newItems = items.filter(i => i.id !== deleteItem.id);
            setItems(newItems);
        } else {
            console.log("STATUS CODE: " + res.code)
            props.announce("We couldn't delete your item :<...", "error");
        }
        setShowDialog(false);
    }

    const create = async (item) => {
        const safeCreateItem = authWrapper(setIsLoggedIn, createItem);
        const res = await safeCreateItem(wishlistId, item);
        if (res.success) {
            setItems([...items, res.data])
        } else {
            console.log("STATUS CODE: " + res.code)
            props.announce("We couldn't create your item :<...", "error");
        }
    }

    const share = async (email) => {
        if (wishlist.sharedWith.includes(email)) {
            props.announce(email + " has already been added", "info");
            return;
        }
        
        const safeUpdateWishlist = authWrapper(setIsLoggedIn, updateWishlist);
        const res = await safeUpdateWishlist(wishlistId, {
            "sharedWith": [email]
        });
        console.log(res);
        if (res.success) {
            wishlist.sharedWith.push(email);
            props.announce("Added " + email, "info");
        } else {
            console.log("STATUS CODE: " + res.code)
            props.announce("We couldn't share with the list with " + email, "error");
        }
    }

   const unshare = async e => {
        // get email from event
        const email = e.target.id;
        const safeUpdateWishlist = authWrapper(setIsLoggedIn, unshareWishlist);
        const res = await safeUpdateWishlist(wishlistId, email);
        console.log(res);
        if (res.success) {
            const index = wishlist.sharedWith.indexOf(email);
            wishlist.sharedWith.splice(index, 1);
            props.announce("Removed " + email, "info");
        } else {
            console.log("STATUS CODE: " + res.code)
            props.announce("We couldn't remove "+email, "error");
        }
    }

    // item creation form
    const handleShowForm = () => {
        if (creating) {
            setCreating(false)
        } else {
            window.scrollTo(0, 0);
            setCreating(true);
        }
    }

    const cancel = () => {
        setCreating(false);
    }

    // wishlist share form
    const handleShareForm = () => {
        if (sharing) {
            setSharing(false);
        } else {
            window.scrollTo(0, 0);
            setSharing(true);
        }
    }

    const cancelShare = () => {
        setSharing(false);
    }

    // confirmation dialog for deleting an item
    const confirmRemove = item => {
        setShowDialog(true);
        setDeleteItem(item);
    }

    const cancelRemove = () => {
        setShowDialog(false);
    }

    const displayContent = () => {
        return (
            <>
                {showDialog && <ConfirmationDialog title="Delete Item" details={`Are you sure you want to delete ${deleteItem.name}?`} actionLabel="Delete" action={remove} cancel={cancelRemove}/>}
                <header className="Wishlist-header">
                    <Link id="back" to="/wishlists">
                        <FontAwesomeIcon icon={faArrowLeft}/>
                        <span>All Wishlists</span>
                    </Link>
                    <h1>{wishlist.name}</h1>
                    
                    {wishlist.sharedWith.length > 0 && 
                    <div className="Wishlist-tag-container">
                        {wishlist.sharedWith.map(email => {
                             return (
                                <div className="Wishlist-tag">
                                    <span className="Wishlist-tag-content">{email}</span>
                                    <span id={email} className="Wishlist-tag-x" onClick={unshare}>x</span>
                                </div>
                             );
                        })}
                    </div>
                    }
                </header>

                <div className="Wishlist-content-container">

                    {sharing && <ShareForm share={share} cancel={cancelShare}/>}                            
                    {creating && <NewItemForm create={create} cancel={cancel}/>}

                    {items.length === 0 && <p>No items yet...</p>}
                    {items.length !== 0 && 
                        <div className="Wishlist-items">
                            {items.map(i => <Item {...i} key={i.id} remove={confirmRemove} update={update}/>)}
                        </div>
                    }
                </div>
                <button id="Wishlist-new-button" onClick={handleShowForm}>New Item</button>
                <button id='Wishlist-share-button' onClick={handleShareForm}>Share</button>
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
