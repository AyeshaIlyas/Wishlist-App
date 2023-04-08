import { useEffect, useContext, useState } from "react";
import { createWishlist, getWishlists, removeWishlist, updateWishlist } from "../../services/wishlistService";
import WishlistCard from "../WishlistCard/WishlistCard";
import WishlistForm from "../WishlistForm/WishlistForm";
import {authWrapper} from "../../services/utils";
import './MyWishlists.css';
import AuthContext from "../Contexts/AuthContext";

export default function MyWishlists() {
    const {setIsLoggedIn} = useContext(AuthContext);
    const [wishlists, setWishlists] = useState([]);
    const [isFormDisplaying, setIsFormDisplaying] = useState(false);
    const [error, setError] = useState(null);
    const [isLoading, setIsLoading] = useState(true);



    useEffect(() => {
        const loadWishlists = async () => {
            const safeGet = authWrapper(setIsLoggedIn, getWishlists);
            const wishlists = await safeGet();
            if (wishlists) {
                setWishlists(wishlists);
                setIsLoading(false);
            }
        }
        loadWishlists();
    }, [setIsLoggedIn])  

    const handleAdd = () => {
       setIsFormDisplaying(true);
    }

    const cancel = () => {
       setIsFormDisplaying(false);
    }

    const create = async (wishlist) => {
        const safeCreate = authWrapper(setIsLoggedIn, createWishlist);
        const response = await safeCreate(wishlist);
        if (response) {
            if (response.success) {
                setWishlists([...wishlists, response.wishlist]);
                setError(null);
            }
        } else {
            // error
            setError("We couldn't create your item :<...")
        }
    }

    const update = async (id, newWishlist) => {
        const safeUpdate = authWrapper(setIsLoggedIn, updateWishlist);
        // updateWishlist takes an object with the updates. the object must contain the wishlist id.
        const response = await safeUpdate(id, {...newWishlist});
        if (response) {
            if (response.success) {
                const updatedWishlists = wishlists.map(w => w.id === id ? response.wishlist : w);
                setWishlists(updatedWishlists);
                setError(null);
            }
        } else {
            // error
            setError("We couldn't create your item :<...")
        }
    }

    const remove = async id => {
        const safeRemove = authWrapper(setIsLoggedIn, removeWishlist);
        const response = await safeRemove(id);
        if (response) {
            if (response.success) {
                const updatedWishlists = wishlists.filter((w) => ((w.id !== id)))
                setWishlists(updatedWishlists);
                setError(null);
            }
        } else {
            // error
            setError("We couldn't delete your item :<...")
        }

    }

    const displayWishlists = () => {
        return (
            <>
                {wishlists.length === 0 && <p className="MyWishlists-msg">{"No wishlists yet :<"}</p>}
                { isFormDisplaying && <WishlistForm create={create} cancel={cancel}/> }
                { wishlists.length !== 0 &&
                        <div className="MyWishlists-cards">
                            {wishlists.map((w) => (
                                <WishlistCard key={w.id} id={w.id} {...w} update={update} remove={remove} />
                            ))}
                        </div>
                }
                <button id="MyWishlists-add-button" onClick={handleAdd}>Add</button>
            </>
        );
    }
        
    return (
        <div className="MyWishlists">
            <div className="MyWishlists-container">
                <h1>Wishlists</h1>
                {error && <p>{error}</p>}
                { isLoading ? <p className="MyWishlists-msg">Loading...</p> : displayWishlists() }
            </div> 
        </div>
    );
}
