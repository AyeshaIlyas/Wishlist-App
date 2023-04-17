import {useEffect, useState} from "react";
import "./WishlistForm.css";

export default function WishlistForm(props) {
    const [name, setName] = useState("");
    const [error, setError] = useState(null);

    const handleChange = e => {
        setName(e.target.value);
    }

    const handleSubmit = e => {
        e.preventDefault();
        if (name.trim().length === 0) {
            setError("Please specifiy a name");
            return;
        } else {
            setError(null);
        }
        props.create({name});
        props.cancel();
    }

    return(
        <form className="WishlistForm">
            <label htmlFor="name">Name: </label>
            <input 
            type="text" 
            value={name} 
            onChange={handleChange}
            autoFocus
            />

            <button onClick={handleSubmit}>Submit</button>
            <button type="button" onClick={props.cancel}>Cancel</button>
            <p>{error && error}</p>
        </form>
    );
}
