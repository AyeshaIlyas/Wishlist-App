import { useState } from "react";
import "./NewItemForm.css";

export function NewItemForm(props) {
    const [name, setName] = useState("");
    const [price, setPrice] = useState("");
    const [supplier, setSupplier] = useState("");
    const [error, setError] = useState(null);

    const handleCancel = () => {
        props.cancel();
    }

    const handleSubmit = e => {
        e.preventDefault();
        if (name.trim().length === 0) { // name must exist
            setError("Please specify a name");
        } else if (price + "".trim().length > 0 && isNaN(parseFloat(price))) { // price must be a valid decimal
            setError("Price can only be a number");
        } else {
            setError(null);
            const item = {name, supplier};
            if (price + "".trim().length !== 0) { // if no price specified, create item without a price
                item.price = parseFloat(price);
            }
            props.create(item);
            props.cancel();
        }
    }

    return (
        <form className="NewItemForm" onSubmit={handleSubmit}>
            <h2>{name.trim() ? name : "New Item"}</h2>
            <section>
                <label htmlFor="name">Name: </label>
                <input type="text" 
                    id="name" 
                    name="name" 
                    value={name}
                    autoFocus
                    onChange={e => setName(e.target.value)}/>
            </section>

            <section>
                <label htmlFor="price">Price: </label>
                <input type="number" 
                    id="price" 
                    name="price" 
                    value={price}
                    onChange={e => setPrice(e.target.value)}/>
            </section>
            
            <section>
                <label htmlFor="supplier">Location/Link: </label>
                <input type="text" 
                    id="supplier" 
                    name="supplier" 
                    value={supplier}
                    onChange={e => setSupplier(e.target.value)}/>
            </section>

            <p>{error && error}</p>

            <section>
                <button>Create</button>
                <button type="button" onClick={handleCancel}>Cancel</button>
            </section>
        </form>
    );
}