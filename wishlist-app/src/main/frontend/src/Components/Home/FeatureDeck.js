import React, {Component} from "react";
import Feature from "./Feature";
import "./FeatureDeck.css"

class FeatureDeck extends Component {
    static defaultProps = {
    features: [
        { name: "Streamlined", des: "Streamline the gift giving process" },
        { name: "Personalized", des: "Create a personalized Wishlist" },
        { name: "Anonyomus Giving", des: "Completely anonymous gift purchasing" },
        { name: "Share Lists", des: "Send your list to family and friends" }    ]
};
    render() {
        return (
            <div className="FeatureDeck">
                <h1>Features</h1>
                <div className="FeatureDeck-cards">
                    {this.props.features.map((f) => (
                        <Feature name={f.name} des={f.des}/>
                    ))}
                </div>
            </div>
        );
    }
}

export default FeatureDeck;

