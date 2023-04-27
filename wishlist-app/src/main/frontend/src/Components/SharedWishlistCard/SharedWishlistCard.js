import { Link } from "react-router-dom";
import "./SharedWishlistCard.css"
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import ConfirmationDialog from "../Utils/ConfirmationDialog/ConfirmationDialog";


export default function SharedWishlistCard({id, name, owner, itemCount, removeSelf}) {

        const handleRemove = () => {
           
        }

   return(
        <div className="SharedWishlistCard">
            <Link className="SharedWishlistCard-link" to={`/shared/${id}`}>
                <header>
                    <div className="SharedWishlistCard-info-container">
                        <h3 className="SharedWishlistCard-info SharedWishlist-owner">{owner}</h3>
                        <h3 className="SharedWishlistCard-info SharedWishlist-title">{name}</h3>
                        <h4 className="SharedWishlistCard-info SharedWishlist-itemCount">Items: {itemCount}</h4>
                        <h5 className="SharedWishlistCard-info SharedWishlist-removeSelf"></h5>
                            <button id="SharedWishlist-removeSelf" onClick="handleRemove"> <FontAwesomeIcon className="removeSelf" icon="fa-solid fa-x" /></button>
                            
                    </div>
                    {showDialog && <ConfirmationDialog title="Remove yourself from Shared Wishlist" details={`Are you sure you want remove yourself from this shared wishlist? ${deleteItem.name}?`} actionLabel="Remove" action={remove} cancel={cancelRemove}/>}
                </header>
            </Link>
        </div>
    );
}
