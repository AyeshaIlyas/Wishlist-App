import {useEffect, useState} from "react";
import { faCheck } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import "./WishlistForm";

export default function WishlistForm(props) {
    const [name, setName] = useState("");
    const [isNameValid, setIsNameValid] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        setIsNameValid(name.trim().length !== 0);
        setError(null);
    }, [name])

    const handleChange = e => {
        setName(e.target.value);
    }

    const handleSubmit = e => {
        e.preventDefault();
        if (name.trim().length === 0) {
            setError("Please specifiy a name");
            return;
        }
        props.create({name});
        props.cancel();
    }

    return(
        <form>
            <label htmlFor="name">
                Name: 
                <FontAwesomeIcon icon={faCheck} className={isNameValid ? "valid" : "hide"} />
            </label>
            <input 
            type="text" 
            value={name} 
            onChange={handleChange}
            autoFocus
            />

            <button onClick={handleSubmit}>Sumbit</button>
            <button type="button" onClick={props.cancel}>Cancel</button>
            <p>{error && error}</p>
        </form>
    );
}
