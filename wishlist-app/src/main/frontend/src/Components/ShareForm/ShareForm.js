import { useState } from "react";
import "./ShareForm.css";

export function NewItemForm(props) {
    const [email, setEmail] = useState("");
    const [error, setError] = useState(null);

    const handleCancel = () => {
        props.cancel();
    }

    const handleSubmit = e => {
        e.preventDefault();
        if (email.trim().length === 0) { // email must exist
            setError("Please specify an email");
        } else {
            setError(null);
            props.add(email);
            props.cancel();
        }
    }

    return (
        <form className="ShareForm" onSubmit={handleSubmit}>
            <h2>{"New Email"}</h2>
            <section>
                <label htmlFor="email">Email: </label>
                <input type="email" 
                    id="email" 
                    name="email" 
                    value={email}
                    autoFocus
                    onChange={e => setEmail(e.target.value)}/>
            </section>

            <p>{error}</p>

            <section>
                <button>Submit</button>
                <button type="button" onClick={handleCancel}>Cancel</button>
            </section>
        </form>
    );
}