import { useState } from "react";
import "./ShareForm.css";

export default function ShareForm(props) {
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
            props.share(email);
        }
        setEmail("");
        setError(null);
    }

    return (
        <form className="ShareForm" onSubmit={handleSubmit}>
            <div className="ShareForm-header">
                <div>
                    <h2>Share Wishlist</h2>
                    <p>Add people to this list by email. In order to add people they must have an account.</p>
                </div>
                <button type="button" onClick={props.cancel} className='ShareForm-exit-button'>x</button>
            </div>
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

            {/* <section>
                <button> Submit</button>
                <button type="button" onClick={handleCancel}>Cancel</button>
            </section> */}
        </form>
    );
}