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
            return <a className="SharedItem-link" href={supplier} target="_blank">Buy Here</a>;
        } else {
            return <span className="SharedItem-link">{supplier}</span>;
        }
    }

    const classString = `SharedItem ${purchased ? "bought" : "notBought"}`
    
    return (
        <div className={classString}>
            <div className="SharedItem-container">
                <h3 className="SharedItem-info">{name}</h3> 
                <div className="SharedItem-price SharedItem-info">
                    {price && <h4>Price: ${price}</h4>}
                </div>
                <div className="SharedItem-info SharedItem-info-link">
                    {supplier && getSupplier()}
                </div>

                <div className="SharedItem-buy-space SharedItem-info">
                    {showBuyButton && <button className="SharedItem-buy-button" type="button" onClick={handleBuy}>{purchased ? "Unbuy" : "Buy"}</button>}
                </div>
                
            </div>
        </div>
    );
}