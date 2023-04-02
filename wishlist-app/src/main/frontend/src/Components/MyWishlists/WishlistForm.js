import React, {Component} from "react";
import "./WishlistForm";

class WishlistForm extends Component {
    constructor(props) {
        super(props);
        this.state = {
            name: ""
        }
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event)
    {
        this.setState({
            name: event.target.value
        })
    }

    handleSubmit(e)
    {
        e.preventDefault();
        this.props.create(this.state.name)
        this.props.cancel()
    }

    render(){
        return(
            <form>
                <label htmlFor="name">Name: </label>
                <input 
                type="text" 
                value={this.state.name} 
                onChange={this.handleChange}
                />

                <button onClick={this.handleSubmit}>Sumbit</button>
                <button onClick={this.props.cancel}>Cancel</button>
            </form>
        );
    }
}

export default WishlistForm;