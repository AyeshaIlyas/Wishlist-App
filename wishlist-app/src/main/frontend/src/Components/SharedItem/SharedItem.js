import "./SharedItem.css"

var expression = /[-a-zA-Z0-9@:%._+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_+.~#?&//=]*)?/gi;
var regex = new RegExp(expression);

export default function SharedItem({id, name, price, supplier, isBought, showBuyButton, buy}) {
    
    // toggle buy status 
    const handleBuy = async () => {
        await buy(id, !isBought);
    }

    const getSupplier = () => {
        if (supplier.match(regex)) {
            return <a className="Sharedtem-link" href={supplier}>Buy</a>;
        } else {
            return <span className="Shared Item-link">{supplier}</span>;
        }
    }

    const classString = `SharedItem ${isBought ? "bought" : "notBought"}`
    
    return (
        <div className={classString}>
            <h2 className="SharedItem-title">{name}</h2> 
            {price && <p className="SharedItem-price">Price: ${price}</p>}
            {supplier && getSupplier()}
                
            {showBuyButton && <button type="button" onClick={handleBuy}>{isBought ? "Unbuy" : "Buy"}</button>}
        </div>
    );
}