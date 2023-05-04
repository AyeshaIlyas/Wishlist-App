import { Link } from "react-router-dom";
import "./Home.css";
import FeatureDeck from "./FeatureDeck";
import { useContext } from "react";
import AuthContext from "../Contexts/AuthContext";

export default function Home() {
    const {isLoggedIn} = useContext(AuthContext);
    
    return (
        <div className="Home">
            <header>
                <h1 className="Home-title">FOURTH WISH</h1>
                <section>
                    <p className="Home-blurb">Welcome to our new app, Fourth Wish.
                    With this app you can set up a Wishlist with your most wanted gifts for your 
                    loved ones to see. You can create lists of any items you wish, and share it with
                    friends, family and even co-workers! All gift purchases are completely anonymous so 
                    the surprise will never be spoiled. We strive to innovate the gift giving process by 
                    offering a new, simple way to have all a person's wants in one convenient place. This
                    will offer all users the certainty that the gifts being bought are what the recipient 
                    truly wants to avoid the stress that comes with picking out gifts. Say goodbye to gift 
                    cards! 

                    </p>                
                </section>
                {!isLoggedIn && <Link to="/register" id="registerBtn"><button>Register</button></Link>}
            </header>
            
            <section>
                <p className="Home-blurb">The name for our product, "Fourth Wish", comes from the 
                old idea of genies granting three wishes to the person who finds them. Our site is the genie's "Fourth Wish", 
                since the site itself allows for as many wishes to be granted as possible. Since a “fourth wish” from a genie
                is impossible, we want to show that we can go past what is seen as “impossible” and make it possible. We hope 
                that our product can help as many people as possible get the gifts that they may not realize their loved ones 
                want.
                </p>
            </section>

            <FeatureDeck/>
            
            <section>
                <h2 className="Home-team">About Team Genie</h2>

                <p className="Home-blurb">Team Genie is a team of thirteen students who strive to create products that can be 
                of use to many people. All the members of Team Genie come from different backgrounds and have different skillsets, 
                so there are 3 subsets of the team: frontend development, backend development, and requirements gathering/testing. 
                All subsets work together and communicate using an agile method of development to bring our ideas to fruition. Above
                all else, we value teamwork, inclusivity, and communication, as well as creating a service that can benefit as many
                people as it can.
                </p>
            </section>

        </div>
    );
}
