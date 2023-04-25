import { useState } from "react";
import "./UnshareForm.css";

export default function UnshareForm(props) {
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
            props.unshare(email);
            props.cancel();
        }
    }

    return (
        <form className="UnshareForm" onSubmit={handleSubmit}>
            
            <section>
                <label htmlFor="email"> </label>
                <input type="email" 
                    id="email" 
                    placeholder='Email'
                    name="email" 
                    value={email}
                    autoFocus
                    onChange={e => setEmail(e.target.value)}/>
            </section>

            <p>{error}</p>

            <section>
                <button> Submit</button>
                <button type="button" onClick={handleCancel}>Cancel</button>
            </section>
        </form>
    );
}