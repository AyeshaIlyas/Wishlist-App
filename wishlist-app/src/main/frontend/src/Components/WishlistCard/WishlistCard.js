import { useState, useEffect } from "react";
import {Link} from "react-router-dom";
import { faCheck } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

import "./WishlistCard.css";

export default function WishlistCard(props)  {
    const [isEditing, setIsEditing] = useState(false);
    const [name, setName] = useState(props.name);
    const [isNameValid, setIsNameValid] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        setIsNameValid(name.trim().length !== 0);
        setError(null);
    }, [name])


    const handleEdit = () => {
        setIsEditing(true);
    }

    const handleRemove = () => {
        props.remove(props.id);
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
            setError("Please specifiy a name");
            return;
        }
        props.update(props.id, {name});
        setIsEditing(false);
    }

    return(
        <div className="WishlistCard">
            <h2 className="Wishlist-title">{props.name}</h2>
            <div>
                {isEditing ?
                (
                    <form onSubmit={handleSubmit}>
                        <label htmlFor="name">
                            Name: 
                            <FontAwesomeIcon icon={faCheck} className={isNameValid ? "valid" : "hide"} />
                        </label>
                        <input 
                        type="text" 
                        value={name} 
                        onChange={handleChange}
                        />

                        <div>
                            <button>Sumbit</button>
                            <button type="button" onClick={handleCancel}>Cancel</button>
                        </div>
                        <p>{error && error}</p>
                    </form>
                ) : (
                    <>
                        <p><Link className="WishlistCard-link" to={`/wishlists/${props.id}`}>View</Link></p>
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

