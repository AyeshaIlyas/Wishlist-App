import React, {Component} from 'react';
import "./Feature.css"

class Feature extends Component {
    render() {
        return (

            <div className="Feature">
                <h1 className="Feature-title">{this.props.name}</h1>
                {/*<img src={} />*/}
                <div className="Feature-des">{this.props.des}</div>
            </div>
        );
    }
}

export default Feature;