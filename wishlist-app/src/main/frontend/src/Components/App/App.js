import { BrowserRouter, Route, Routes, Link } from 'react-router-dom';
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
      <Link to="/" style={{ padding: "10px" }}>Home</Link>
      <Link to="/login" style={{ padding: "10px" }}>Login</Link>
      <Link to="/register" style={{ padding: "10px" }}>Register</Link>
    </nav>
  } else {
    nav = <nav>
      <Link to="/" style={{ padding: "10px" }}>Home</Link>
      <Link to="/profile" style={{ padding: "10px" }}>Profile</Link>
      <Link to="/wishlists" style={{ padding: "10px" }}>My Wishlists</Link>
      <Link to="/shared" style={{ padding: "10px" }}>Shared With Me</Link>
      <Logout setVerified={setVerified}/>
    </nav>
  }

  return (
    <div className="App">
      <BrowserRouter>
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
      </BrowserRouter>
    </div>
  );
}

export default App;
