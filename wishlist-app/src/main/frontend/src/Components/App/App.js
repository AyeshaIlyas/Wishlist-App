import { Route, Routes, NavLink } from 'react-router-dom';
import { useEffect, useState } from 'react';
import './App.css';
import Home from "./../Home/Home";
import Register from "./../Register/Register";
import Login from "./../Login/Login";
import Profile from "./../Profile/Profile";
import MyWishlists from "./../MyWishlists/MyWishLists";
import ProtectedRoute from "./../Utils/ProtectedRoute";
import Logout from '../Logout/Logout';
import Cookies from "js-cookie";
import AuthContext from '../Contexts/AuthContext';


function App() {

  // TODO: logout when JWT expires - check if token expired whever user tries to access new route
  const [isLoggedIn, setIsLoggedIn] = useState(Cookies.get("auth-session") ? true : false);

  // since second arg is empty array, useEffect effect func is only invoked on the first render
  useEffect(() => {
    Cookies.get("auth-session") ? setIsLoggedIn(true) : setIsLoggedIn(false);
  }, [])

  let nav;
  if (!isLoggedIn) {
    nav = <nav>
      <NavLink to="/" style={{ padding: "10px" }}>Home</NavLink>
      <NavLink to="/login" style={{ padding: "10px" }}>Login</NavLink>
      <NavLink to="/register" style={{ padding: "10px" }}>Register</NavLink>
    </nav>
  } else {
    nav = <nav>
      <NavLink to="/" style={{ padding: "10px" }}>Home</NavLink>
      <NavLink to="/profile" style={{ padding: "10px" }}>Profile</NavLink>
      <NavLink to="/wishlists" style={{ padding: "10px" }}>My Wishlists</NavLink>
      <NavLink to="/shared" style={{ padding: "10px" }}>Shared With Me</NavLink>
      <Logout/>
    </nav>
  }

  return (
    <div className="App">
      <AuthContext.Provider value={{isLoggedIn, setIsLoggedIn}}>
        {nav}
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
      </AuthContext.Provider>
    </div>
  );
}

export default App;