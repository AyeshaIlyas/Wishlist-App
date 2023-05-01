import { useState } from "react";
import "./Item.css";

var expression = /[-a-zA-Z0-9@:%._+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_+.~#?&//=]*)?/gi;
var regex = new RegExp(expression);

export default function Item(props) {
    const [editing, setEditing] = useState(false);

    const [name, setName] = useState(props.name);
    const [price, setPrice] = useState(props.price);
    const [supplier, setSupplier] = useState(props.supplier);
    const [error, setError] = useState(null);

    const handleRemove = () => {
        props.remove(props);
    }

    const handleUpdate = e => {
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
            props.update(props.id, item)
            setEditing(false);
        }
    }

    const handleShowEditForm = () => {
        setEditing(true);
    }

    const handleCancel = () => {
        setEditing(false);
    }

    const getSupplier = () => {
        if (props.supplier.match(regex)) {
            return <a className="Item-link" href={props.supplier}>Buy</a>;
        } else {
            return <span className="Item-link">{props.supplier}</span>;
        }
    }

    return (
        !editing ? (
            <div className="Item">
                <h2 className="Item-title">{props.name}</h2> 
                {props.price && <p className="Item-price">Price: ${props.price}</p>}

                <div>
                    {props.supplier && getSupplier()}
                    <button onClick={handleShowEditForm}>Edit</button>
                    <button onClick={handleRemove}>Delete</button>
                </div>
                
            </div>
        ) : (
            <form className="Item" onSubmit={handleUpdate}>
                <h2>{name.trim() ? name : "_"}</h2>
                <section>
                    <label htmlFor="name">Name: </label>
                    <input type="text" 
                        id="name" 
                        name="name" 
                        value={name}
                        onChange={e => setName(e.target.value)}/>

                    <label htmlFor="price">Price: </label>
                    <input type="number" 
                        id="price" 
                        name="price" 
                        value={price}
                        onChange={e => setPrice(e.target.value)}/>
                </section>
                
                <section>
                    <label htmlFor="supplier">Location: </label>
                    <input type="text" 
                        id="supplier" 
                        name="supplier" 
                        value={supplier}
                        onChange={e => setSupplier(e.target.value)}/>
                </section>

                <p>{error && error}</p>

                <section>
                    <button>Update</button>
                    <button type="button" onClick={handleCancel}>Cancel</button>
                </section>
            </form>
        )
    );
}
