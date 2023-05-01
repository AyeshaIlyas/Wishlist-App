import {useContext, useEffect, useState} from "react";
import SharedWishlistCard from "../SharedWishlistCard/SharedWishlistCard";
import "./SharedWishlists.css";
import { getSharedWishlists, removeSelfFromList } from "../../services/sharedWishlistService";
import AuthContext from "../Contexts/AuthContext";
import { authWrapper } from "../../services/utils";
import Spinner from "../Utils/Spinner";
import ConfirmationDialog from "../Utils/ConfirmationDialog/ConfirmationDialog";

export default function SharedWishlists() {
    const {setIsLoggedIn} = useContext(AuthContext);
    const [sWishlists, setSWishlists] = useState([]);
    const [loading, setLoading] = useState(true);
    const [showDialog, setShowDialog] = useState(false);
    const [deleteWishlist, setDeleteWishlist] = useState({});

    useEffect(
        () => {
            const loadData = async () => {
                const safeGet = authWrapper(setIsLoggedIn, getSharedWishlists);
                const wishlists = await safeGet();
                if (wishlists) {
                    setSWishlists(wishlists);
                    setLoading(false);
                }
            }
            loadData();
            setInterval(loadData, 10 * 1000); // increase reload interval for performance
        }, [setIsLoggedIn]
    );

    const removeSelf = async () => {
        const safeRemoveSelf = authWrapper(setIsLoggedIn, removeSelfFromList);
        const response = await safeRemoveSelf(deleteWishlist.id);
        if (response) {
            if (response.success) {
                const newWishlists = sWishlists.filter(i => i.id !== deleteWishlist.id);
                setSWishlists(newWishlists);
            }
        } 
        setShowDialog(false);       

    }

    const confirmRemove = wishlist => {
        setShowDialog(true);
        setDeleteWishlist(wishlist);
    }

    const cancelRemove = () => {
        setShowDialog(false);
    }
    
    const displaySharedWishlists = () => {
        return (
            <>
                {showDialog && <ConfirmationDialog title="Remove Self From Wishlist" details={`Are you sure you want to remove yourself from ${deleteWishlist.name}? If you do, the person who shared the wishlist will have to reshare for you to have access again.`} actionLabel="Remove Myself" action={removeSelf} cancel={cancelRemove}/>}
                {sWishlists.length === 0 && <p className="SharedWishlists-msg">{"No one has shared any wishlists yet :<"}</p>}
                {sWishlists.length !== 0 &&
                    <>
                        <div className="SharedWishlists-card-header-container">
                            <h2 className="SharedWishlists-card-header-info">Shared by:</h2>
                            <h2 className="SharedWishlists-card-header-info">Title:</h2>
                            <h2 className="SharedWishlists-card-header-info">Item Count:</h2>
                            <h2 className="SharedWishlists-card-header-remove-space"></h2>
                        </div>
                        <div className="SharedWishlists-cards">
                            {sWishlists.map((s) => (
                                <SharedWishlistCard key={s.id} {...s} removeSelf={confirmRemove}/>
                            ))}
                        </div>
                    </>
                }
            </>
        );
    }
        
    return (
        <div className="SharedWishlists">
            <div className="SharedWishlists-container">
                <h1>Wishlists Shared with Me</h1>        
                { loading ? (
                     <div>
                        <p className="SharedWishlists-msg">Loading...</p>
                        <Spinner/>
                     </div>
                ) : displaySharedWishlists() }
            </div> 
        </div>
    );
}