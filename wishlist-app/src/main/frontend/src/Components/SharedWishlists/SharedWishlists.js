import {useContext, useEffect, useState} from "react";
import SharedWishlistCard from "../SharedWishlistCard/SharedWishlistCard";
import "./SharedWishlists.css";
import { getSharedWishlists } from "../../services/sharedWishlistService";
import AuthContext from "../Contexts/AuthContext";
import { authWrapper } from "../../services/utils";
import Spinner from "../Utils/Spinner";

export default function SharedWishlists() {
    const {setIsLoggedIn} = useContext(AuthContext);
    const [sWishlists, setSWishlists] = useState([]);
    const [loading, setLoading] = useState(true);

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
        }, [setIsLoggedIn]
    );
    
    const displaySharedWishlists = () => {
        return (
            <>
                {sWishlists.length === 0 && <p className="SharedWishlists-msg">{"No one has shared any wishlists yet :<"}</p>}
                {sWishlists.length !== 0 &&
                    <>
                        <div className="SharedWishlists-card-header-container">
                            <h2 className="SharedWishlists-card-header-info">Shared by:</h2>
                            <h2 className="SharedWishlists-card-header-info">Title:</h2>
                            <h2 className="SharedWishlists-card-header-info">Item Count:</h2>
                        </div>
                        <div className="SharedWishlists-cards">
                            {sWishlists.map((s) => (
                                <SharedWishlistCard key={s.id} {...s} />
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