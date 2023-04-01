import React, {Component} from "react";
import "./WishlistCard.css";

class WishlistCard extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isEditing: false,
            name: this.props.name
        }

        this.handleEdit = this.handleEdit.bind(this);
        this.handleDelete = this.handleDelete.bind(this);
        this.handleCancel = this.handleCancel.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleEdit() 
{
        this.setState({isEditing: true });
    }

    handleDelete() {
        this.props.delete(this.props.id);
    } 

    handleCancel() {
        this.setState({isEditing: false});
    }

    handleChange(e) {
        this.setState({name: e.target.value});
    }

    handleSubmit(e) {
        e.preventDefault();
        this.props.edit(this.props.id, this.state.name);
        this.setState({isEditing: false});
    }

    render() {
        return(
            <div>
                <h2 className="Wishlist-title">{this.props.name}</h2>
                <div>
                    {this.state.isEditing ?
                    (
                        <form onSubmit={this.handleSubmit}>
                            <label htmlFor="name">Name: </label>
                            <input 
                            type="text" 
                            value={this.state.name} 
                            onChange={this.handleChange}
                            />

                            <button>Sumbit</button>
                            <button type="button" onClick={this.handleCancel}>Cancel</button>
                        </form>
                    ) : (
                        <>
                            <button onClick={this.handleEdit}>Edit</button>
                            <button onClick={this.handleDelete}>Delete</button>
                        </>
                    )}
                </div>
            </div>
        );
    }
}

export default WishlistCard;