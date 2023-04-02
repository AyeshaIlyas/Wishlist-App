import React, {Component} from "react";
import Feature from "./Feature";
import "./FeatureDeck.css"

class FeatureDeck extends Component {
    static defaultProps = {
    features: [
        { name: "Feature1", des: "Lorem ipsum dolor sit aret" },
        { name: "Feature2", des: "Lorem ipsum dolor sit aret" },
        { name: "Feature3", des: "Lorem ipsum dolor sit aret" },
        { name: "Feature4", des: "Lorem ipsum dolor sit aret" },
        { name: "Feature5", des: "Lorem ipsum dolor sit aret" }
    ]
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

