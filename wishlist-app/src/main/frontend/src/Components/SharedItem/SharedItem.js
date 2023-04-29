import "./SharedItem.css"

var expression = /[-a-zA-Z0-9@:%._+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_+.~#?&//=]*)?/gi;
var regex = new RegExp(expression);

export default function SharedItem({id, name, price, supplier, purchased, showBuyButton, buy}) {
    
    // toggle buy status 
    const handleBuy = async () => {
        await buy(id, !purchased);
    }

    const getSupplier = () => {
        if (supplier.match(regex)) {
            return <a className="SharedItem-link" href={supplier}>Buy</a>;
        } else {
            return <span className="SharedItem-link">{supplier}</span>;
        }
    }

    const classString = `SharedItem ${purchased ? "bought" : "notBought"}`
    
    return (
        <div className={classString}>
            <div className="SharedItem-container">
                <h4 className="SharedItem-title SharedItem-info">{name}</h4> 
                {price && <p className="SharedItem-price SharedItem-info">Price: ${price}</p>}
                {supplier && getSupplier()}

                <div className="SharedItem-buy-space">
                    {showBuyButton && <button className="SharedItem-info SharedItem-buy-button" type="button" onClick={handleBuy}>{purchased ? "Unbuy" : "Buy"}</button>}
                </div>
                
            </div>
        </div>
    );
}