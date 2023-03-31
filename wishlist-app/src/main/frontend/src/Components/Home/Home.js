import { Link } from "react-router-dom";
import "./Home.css";
import FeatureDeck from "./FeatureDeck";
import blue from ".//blue.png";

export default function Home() {
    
    return (
        <div className="Home">
            <header>
                <h1 className="Home-title">FOURTH WISH</h1>
                    <section className= "sect1">
                        <p className="Home-blurb" id= "blurb">ABOUT APP BLURB Lorem ipsum dolor sit amet, consectetur adipiscing elit, 
                        sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. 
                        Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris 
                        nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in 
                        reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla 
                        pariatur. Excepteur sint occaecat cupidatat non proident, sunt in 
                        culpa qui officia deserunt mollit anim id est laborum.
                        </p>                
                
                        <img id= "blue" src={blue} alt="place holder"/>
                    </section>
                
                <Link to="/register"><button id="registerBtn">Sign Up</button></Link>
            </header>
            
            <section>
                <p className="Home-blurb" id= "blurbette">ABOUT NAME BLURB Lorem ipsum dolor sit amet, consectetur adipiscing elit, 
                    sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. 
                    Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris 
                    nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in 
                    reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla 
                    pariatur. Excepteur sint occaecat cupidatat non proident, sunt in 
                    culpa qui officia deserunt mollit anim id est laborum.
                </p>
            </section>

            <FeatureDeck/>
            
            <section>
                <h2 className="Home-team">About Team Genie</h2>

                <p className="Home-blurb" id="blurbetty">ABOUT TEAM GENIE Lorem ipsum dolor sit amet, consectetur adipiscing elit, 
                    sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. 
                    Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris 
                    nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in 
                    reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla 
                    pariatur. Excepteur sint occaecat cupidatat non proident, sunt in 
                    culpa qui officia deserunt mollit anim id est laborum.
                </p>
            </section>

        </div>
    );
}
