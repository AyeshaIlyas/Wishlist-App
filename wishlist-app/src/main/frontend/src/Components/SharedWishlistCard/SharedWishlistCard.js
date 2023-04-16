import { Link } from "react-router-dom";
import "./SharedWishlistCard.css"

export default function SharedWishlistCard({id, name, owner, itemCount}) {
   return(
        <div className="SharedWishlistCard">
            <Link className="SharedWishlistCard-link" to={`/shared/${id}`}>
                <header>
                    <h2 className="SharedWishlist-title">{name}</h2>
                    <p>Shared by {owner}</p>
                    <p>Items: {itemCount}</p>
                </header>
            </Link>
        </div>
    );
}