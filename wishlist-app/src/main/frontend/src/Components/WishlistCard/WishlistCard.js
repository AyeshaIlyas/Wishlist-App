import { useState, useEffect } from "react";
import {Link} from "react-router-dom";

import "./WishlistCard.css";

export default function WishlistCard(props)  {
    const [isEditing, setIsEditing] = useState(false);
    const [name, setName] = useState(props.name);
    const [error, setError] = useState(null);
    

    useEffect(() => {
        if (name.trim().length === 0) {
            setError("Please specify a name");
        } else {
            setError(null);
        }
    }, [name]);

    const handleEdit = () => {
        setIsEditing(true);
    }

    const handleRemove = () => {
        props.remove(props);
    } 

    const handleCancel = () => {
        setIsEditing(false);
    }

    const handleChange = e => {
        setName(e.target.value);
    }


    const handleSubmit = e => {
        e.preventDefault();
        if (name.trim().length === 0) {
            return;
        }
        props.update(props.id, {name});
        setIsEditing(false);
    }

    return(
        <div className="WishlistCard">                            
        <Link className="WishlistCard-link" to={`/wishlists/${props.id}`}>
            <header>
                <h2 className="Wishlist-title">{props.name}</h2>
                <p>Items: {props.itemCount}</p>
            </header>
        </Link>
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
                            <button>Submit</button>
                            <button type="button" onClick={handleCancel}>Cancel</button>
                        </div>
                    </form>
                ) : (
                        <>
                            <div>
                                <button onClick={handleEdit}>Edit</button>
                                <button onClick={handleRemove}>Delete</button>
                            </div>
                        </>
                )}
            </div>
        </div>
    );
    
}

