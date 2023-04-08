import { Route, Routes } from 'react-router-dom';
import { useEffect, useState } from 'react';
import './App.css';
import Navbar from "./../Navbar/Navbar";
import Home from "./../Home/Home";
import Register from "./../Register/Register";
import Login from "./../Login/Login";
import Profile from "./../Profile/Profile";
import MyWishlists from "../MyWishlists/MyWishlists";
import ProtectedRoute from "./../Utils/ProtectedRoute";
import Cookies from "js-cookie";
import AuthContext from '../Contexts/AuthContext';
import Footer from "./../Footer/Footer";
import Wishlist from '../Wishlist/Wishlist';
import SharedWishlists from "./../SharedWishlists/SharedWishlists";

function App() {

  // TODO: logout when JWT expires - check if token expired whever user tries to access new route
  const [isLoggedIn, setIsLoggedIn] = useState(Cookies.get("auth-session") ? true : false);

  // since second arg is empty array, useEffect effect func is only invoked on the first render
  useEffect(() => {
    Cookies.get("auth-session") ? setIsLoggedIn(true) : setIsLoggedIn(false);
  }, [])


  return (
    <div className="App">
      <AuthContext.Provider value={{isLoggedIn, setIsLoggedIn}}>
        <Navbar/>
        <div className="App-content">
        <Routes>
          <Route path="/" element={<Home/>}/>
          <Route path="/register" element={<Register/>}/>
          <Route path="/login" element={<Login/>}/>
          <Route path="/profile" element={
            <ProtectedRoute component={<Profile/>}/>
          }/>
          <Route path="/wishlists" element={
            <ProtectedRoute component={<MyWishlists/>}/>
          }/>
           <Route path="/wishlists/:wishlistId" element={
             <ProtectedRoute component={<Wishlist/>}/>
          }/>
          <Route path="/shared" element={
            <ProtectedRoute component={<SharedWishlists/>}/>
          }/>
          <Route path="*" element={
            <p style={{textAlign: "center", margin: "3em"}}>Hmm... not quite sure what you're looking for :&lt;</p>
          }/>
        </Routes> 
        </div>
        <Footer />
      </AuthContext.Provider>
    </div>
  );
}

export default App;