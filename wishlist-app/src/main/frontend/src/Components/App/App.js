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
import SharedWishlist from "./../SharedWishlist/SharedWishlist";
import Announcement from '../Utils/Announcement/Announcement';
import { v4 as uuid } from 'uuid';


function App() {

  // TODO: logout when JWT expires - check if token expired whever user tries to access new route
  const [isLoggedIn, setIsLoggedIn] = useState(Cookies.get("auth-session") ? true : false);
  // used to get announcement data from localstorage
  const [a, setA] = useState([]);

  // since second arg is empty array, useEffect effect func is only invoked on the first render
  useEffect(() => {
    Cookies.get("auth-session") ? setIsLoggedIn(true) : setIsLoggedIn(false);
  }, [])

  const cancelAnnouncement = id => {
    let announcements = localStorage.getItem("announcements") ? JSON.parse(localStorage.getItem("announcements")) : [];
    const newAnnouncements = announcements.filter(a => a.id !== id);
    localStorage.setItem("announcements", JSON.stringify(newAnnouncements));
    setA(newAnnouncements);
  }

  const addAnnouncement = (msg ,type) => {
    let announcements = localStorage.getItem("announcements") ? JSON.parse(localStorage.getItem("announcements")) : [];
    const id = uuid()
    let newAnnouncements = [...announcements, {msg, type, id}];
    setTimeout(() => {
      cancelAnnouncement(id);
    }, 5 * 1000);
    if (newAnnouncements.length > 2) {
      newAnnouncements.shift(); 
    }
    localStorage.setItem("announcements", JSON.stringify(newAnnouncements));
    setA(newAnnouncements);
  }
  return (
    <div className="App">
      {
          a.map((a , index) => {
          return <Announcement key={a.id} message={a.msg} type={a.type} vPos={index} cancel={() => {cancelAnnouncement(a.id)}}/>
        })
      }
      
      <AuthContext.Provider value={{isLoggedIn, setIsLoggedIn}}>
        <Navbar/>
        <div className="App-content">
        <Routes>
          <Route path="/" element={<Home/>}/>
          <Route path="/register" element={<Register/>}/>
          <Route path="/login" element={<Login/>}/>
          <Route path="/profile" element={
            <ProtectedRoute component={<Profile announce={addAnnouncement}/>}/>
          }/>
          <Route path="/wishlists" element={
            <ProtectedRoute component={<MyWishlists announce={addAnnouncement}/>}/>
          }/>
           <Route path="/wishlists/:wishlistId" element={
             <ProtectedRoute component={<Wishlist announce={addAnnouncement}/>}/>
          }/>
          <Route path="/shared" element={
            <ProtectedRoute component={<SharedWishlists announce={addAnnouncement}/>}/>
          }/>
          <Route path="/shared/:wishlistId" element={
            <ProtectedRoute component={<SharedWishlist announce={addAnnouncement}/>}/>
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