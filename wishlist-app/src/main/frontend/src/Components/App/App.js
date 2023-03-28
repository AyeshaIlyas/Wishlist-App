import { Route, Routes, NavLink } from 'react-router-dom';
import {  useState } from 'react';
import './App.css';
import Navbar from "./../Navbar/Navbar";
import Home from "./../Home/Home";
import Register from "./../Register/Register";
import Login from "./../Login/Login";
import Profile from "./../Profile/Profile";
import MyWishlists from "./../MyWishlists/MyWishLists";
import Footer from "./../Footer/Footer";
import Protected from "./../Utils/Protected";
import { loggedIn } from '../../utils/auth';


function App() {

  // TODO: logout when JWT expires - check if token expired whenever user tries to access new route
  const [verified, setVerified]= useState();
 

  return (
    <div className="App">
        <Navbar isLoggedIn={verified}/>
        <Routes>
          <Route path="/" element={<Home/>}/>
          <Route path="/register" element={<Register/>}/>
          <Route path="/login" element={<Login setVerified={setVerified}/>}/>
          <Route path="/profile" element={
            <Protected>
              <Profile/>
            </Protected>
          }/>
          <Route path="/wishlists" element={
            <Protected>
              <MyWishlists/>
            </Protected>
          }/>
          <Route path="/shared" element={
            <Protected>
              <MyWishlists/>
            </Protected>
          }/>
          <Route path="*" element={<p>Hmmm no idea what you're asking for...</p>}/>
        </Routes>
        <Footer />
    </div>
  );
}

export default App;
