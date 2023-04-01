import React, { Component } from "react";
import Wishlist from "./WishlistCard";
import WishlistForm from "./WishlistForm";
import './WishlistsDeck.css';



class WishlistsDecks extends Component{
    constructor(props){
        super(props);
        this.state = {
            wishlists: [
                { id: 1, name: "Wishlist1" },
                { id: 2, name: "Wishlist2" },
                { id: 3, name: "Wishlist3" }
            ],
            isFormDisplaying: false,
        }
        this.handleAdd = this.handleAdd.bind(this);
        this.cancel = this.cancel.bind(this);
        this.create = this.create.bind(this);
        this.edit = this.edit.bind(this);
        this.delete = this.delete.bind(this);
    }

    handleAdd() {
        this.setState({
            isFormDisplaying: true
        })
    }

    cancel() {
        this.setState({
            isFormDisplaying: false
        })
    }

    create(wishlistName) {
        this.setState( s => ({wishlists: [...s.wishlists, {id: this.state.wishlists.length + 1, name: wishlistName}]}))
    }

    edit(id, editName) {
        const updatedWishlists = this.state.wishlists.map((w) => w.id === id ? {...w, name: editName} : w);
        this.setState({wishlists: updatedWishlists});
    }

    delete(id) {
        const updatedWishlists = this.state.wishlists.filter((w) => ((w.id !== id)))
        this.setState({wishlists: updatedWishlists});
    }
        
    render() {
        return (
            <div className="WishlistsDeck">
                <h1>Wishlists</h1>
                {this.state.isFormDisplaying ?
                <WishlistForm create={this.create} cancel={this.cancel}/> 
                : <button onClick={this.handleAdd}>Add</button>}
                <div className="WishlistsDeck-cards">
                    {this.state.wishlists.map((w) => (
                        <Wishlist key={w.id} id={w.id} name={w.name} edit={this.edit} delete={this.delete}/>
                    ))}
                </div>
            </div>
        );
    }
}

export default WishlistsDecks;