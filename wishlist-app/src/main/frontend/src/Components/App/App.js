import { Route, Routes, NavLink } from 'react-router-dom';
import {  useState } from 'react';
import './App.css';
import Home from "./../Home/Home";
import Register from "./../Register/Register";
import Login from "./../Login/Login";
import Profile from "./../Profile/Profile";
import MyWishlists from "./../MyWishlists/MyWishLists";
import Protected from "./../Utils/Protected";
import { loggedIn } from '../../utils/auth';
import Logout from '../Logout/Logout';


function App() {

  // TODO: logout when JWT expires - check if token expired whever user tries to access new route
  const [verified, setVerified]= useState();

  let nav;
  if (!loggedIn()) {
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
      <Logout setVerified={setVerified}/>
    </nav>
  }

  return (
    <div className="App">
        {nav}
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
          <Route path="*" element={<p>Hmmm no idea what youre asking for...</p>}/>
        </Routes>
    </div>
  );
}

export default App;
