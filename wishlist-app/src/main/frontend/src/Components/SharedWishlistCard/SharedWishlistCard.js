import { Link } from "react-router-dom";
import "./SharedWishlistCard.css"

export default function SharedWishlistCard({id, name, owner, itemCount}) {
   return(
        <div className="SharedWishlistCard">
            <Link className="SharedWishlistCard-link" to={`/shared/${id}`}>
                <header>
                    <div className="SharedWishlistCard-info-container">
                        <h3 className="SharedWishlistCard-info SharedWishlist-owner">{owner}</h3>
                        <h3 className="SharedWishlistCard-info SharedWishlist-title">{name}</h3>
                        <h4 className="SharedWishlistCard-info SharedWishlist-itemCount">Items: {itemCount}</h4>
                    </div>
                </header>
            </Link>
        </div>
    );
}