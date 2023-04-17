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
                        "name": "Birthday Wishlist",
                        "owner": "Maryam",
                        "itemCount": 5
                    },
                    {
                        "id": 200,
                        "name": "Graduation Gift Ideas",
                        "owner": "Ayesha",
                        "itemCount": 10
                    },
                    {
                        "id": 300,
                        "name": "Valentine’s Day Wishlist",
                        "owner": "Jennifer",
                        "itemCount": 15
                    },
                    {
                        "id": 400,
                        "name": "Christmas Wishlist",
                        "owner": "Wendy",
                        "itemCount": 30
                    },
                    {
                        "id": 500,
                        "name": "New Year's Eve Party",
                        "owner": "Sam",
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
                <h1>Wishlists Shared with Me</h1>
                {/* This div appears even when you have no shared list this need to be changed */}
                <div className="SharedWishlists-card-header-container">
                    <h2 className="SharedWishlists-card-header-info">Shared by:</h2>
                    <h2 className="SharedWishlists-card-header-info">Title:</h2>
                    <h2 className="SharedWishlists-card-header-info">Item Count:</h2>
                </div>
                { loading ? <p className="SharedWishlists-msg">Loading...</p> : displaySharedWishlists() }
            </div> 
        </div>
    );
}