import { Route, Routes } from 'react-router-dom';
import { useEffect, useState } from 'react';
import './App.css';
import Navbar from "./../Navbar/Navbar";
import Home from "./../Home/Home";
import Register from "./../Register/Register";
import Login from "./../Login/Login";
import Profile from "./../Profile/Profile";
import MyWishlists from "./../MyWishlists/MyWishLists";
import ProtectedRoute from "./../Utils/ProtectedRoute";
import Cookies from "js-cookie";
import AuthContext from '../Contexts/AuthContext';
import Footer from "./../Footer/Footer";

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
          <Route path="/shared" element={
            <ProtectedRoute component={<MyWishlists/>}/>
          }/>
          <Route path="*" element={<p>Hmmm no idea what youre asking for...</p>}/>
        </Routes>
        <Footer />
      </AuthContext.Provider>
    </div>
  );
}

export default App;