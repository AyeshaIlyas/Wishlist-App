import { useState, useEffect } from "react";
import {Link} from "react-router-dom";

import "./WishlistCard.css";

export default function WishlistCard(props)  {
    const [isEditing, setIsEditing] = useState(false);
    const [isSharing, setIsSharing] = useState(false);
    const [name, setName] = useState(props.name);
    const [error, setError] = useState(null);
    const [email, setEmail] = useState(props.email);
    

    useEffect(() => {
        if (name.trim().length === 0) {
            setError("Please specifiy a name");
        } else {
            setError(null);
        }
    }, [name]);

    const handleEdit = () => {
        setIsEditing(true);
    }

    const handleRemove = () => {
        props.remove(props.id);
    } 

    const handleCancel = () => {
        setIsEditing(false);
        setIsSharing(false);
    }

    const handleChange = e => {
        setName(e.target.value);
    }

    const handleShareChange = e => {
        setEmail(e.target.value);
    }

    const handleSubmit = e => {
        e.preventDefault();
        if (name.trim().length === 0) {
            return;
        }
        props.update(props.id, {name});
        setIsEditing(false);
    }

    const handleShareSubmit = e => {
        e.preventDefault();
        if (email.trim().length === 0) {
            return;
        }
        //share list
        setIsSharing(false);
    }

    const handleShare = () => {
        setIsSharing(true);
    }

    return(
        <div className="WishlistCard">
            <header>
                <h2 className="Wishlist-title">{props.name}</h2>
                <p>Items: {props.itemCount}</p>
            </header>
            <div>
                {isEditing ?
                (
                    <form onSubmit={handleSubmit}>
                        <label className="WishlistCard-label" htmlFor="name"> Name: </label>
                        <input 
                        type="text" 
                        value={name} 
                        onChange={handleChange}
                        />
                        {error && <p className="WishlistCard-error">{error}</p>}
                        <div>
                            <button>Sumbit</button>
                            <button type="button" onClick={handleCancel}>Cancel</button>
                        </div>
                    </form>
                ) : (
                    <div>
                        {isSharing ?
                        (
                            <form onSubmit={handleShareSubmit}>
                                <label className="WishlistCard-label" htmlFor="email"> Email: </label>
                                <input 
                                type="text" 
                                value={email} 
                                onChange={handleShareChange}
                                />
                                {error && <p className="WishlistCard-error">{error}</p>}
                                <div>
                                    <button>Sumbit</button>
                                    <button type="button" onClick={handleCancel}>Cancel</button>
                                </div>
                            </form>
                        ) : (

                            <>
                                <p><Link className="WishlistCard-link" to={`/wishlists/${props.id}`}>View</Link></p>
                                <div>
                                    <button onClick={handleEdit}>Edit</button>
                                    <button onClick={handleRemove}>Delete</button>
                                    <button onClick={handleShare}>Share</button>
                                </div>
                            </>
                        )}
                    </div>
                )}
            </div>
        </div>
    );
    
}

