import {useEffect, useState} from "react";
import SharedWishlistCard from "../SharedWishlistCard/SharedWishlistCard";
import "./SharedWishlists.css";

export default function SharedWishlists() {

    const [sWishlists, setSWishlists] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(
        () => {
            const loadData = async () => {
                // call the api and get data
                const data = [
                    {
                        "id": 100,
                        "name": "W1",
                        "owner": "Maryam",
                        "itemCount": 0
                    },
                    {
                        "id": 200,
                        "name": "W2",
                        "owner": "Ayesha",
                        "itemCount": 100
                    }
                ]
                setLoading(false);
                setSWishlists(data);
                // if error set error else set to null
            }

            loadData();
        }, []
    );
    
    const displaySharedWishlists = () => {
        return (
            <>
                {sWishlists.length === 0 && <p className="SharedWishlists-msg">{"No one has shared any wishlists yet :<"}</p>}
                {sWishlists.length !== 0 &&
                        <div className="SharedWishlists-cards">
                            {sWishlists.map((s) => (
                                <SharedWishlistCard key={s.id} {...s} />
                            ))}
                        </div>
                }
            </>
        );
    }
        
    return (
        <div className="SharedWishlists">
            <div className="SharedWishlists-container">
                <h1>Shared Wishlists</h1>
                { loading ? <p className="SharedWishlists-msg">Loading...</p> : displaySharedWishlists() }
            </div> 
        </div>
    );
}