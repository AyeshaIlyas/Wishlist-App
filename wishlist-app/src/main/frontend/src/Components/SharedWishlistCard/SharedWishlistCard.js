import { Link } from "react-router-dom";
import "./SharedWishlistCard.css"
import { faCircle, faCircleXmark } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

export default function SharedWishlistCard({id, name, owner, itemCount, removeSelf, ...props}) {
   
    const handleRemove = () => {
        removeSelf({
            id, name, owner, itemCount
        });
   }
   
    return(
        <div className="SharedWishlistCard">
                <header>
                    <div className="SharedWishlistCard-info-container">
                        <Link className="SharedWishlistCard-link" to={`/shared/${id}`}>
                            <h3 className="SharedWishlistCard-info SharedWishlist-owner">{owner}</h3>
                            <h3 className="SharedWishlistCard-info SharedWishlist-title">{name}</h3>
                            <h4 className="SharedWishlistCard-info SharedWishlist-itemCount">Items: {itemCount}</h4>
                        </Link>
                        <FontAwesomeIcon id="deleteBtn" icon={faCircleXmark} onClick={handleRemove}/>
                    </div>
                </header>
        </div>
    );
}