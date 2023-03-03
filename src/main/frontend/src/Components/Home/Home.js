import {Link} from "react-router-dom";

export default function Home() {
    return (
        <div className="Home">
             <h1 id="home">HOME</h1>
            <div id="homebuttondiv">
                <Link to="/login" id="homebutton">
                    <button type="button">LOGIN</button>
                </Link>
            </div>
        </div>
    );
}