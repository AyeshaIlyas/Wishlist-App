import { BrowserRouter, Route, Routes } from 'react-router-dom';
import './App.css';
import Home from "./../Home/Home";
import Login from "./../Login/Login";
import Profile from "./../Profile/Profile";


function App() {
  return (
    <div className="App">
      <h1>The MOST EPIC Wishlist App</h1>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Home/>}/>
          <Route path="/login" element={<Login/>}/>
          <Route path="/profile" element={<Profile/>}/>
          <Route path="*" element={<p>Hmmm no idea what youre asking for...</p>}/>
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
