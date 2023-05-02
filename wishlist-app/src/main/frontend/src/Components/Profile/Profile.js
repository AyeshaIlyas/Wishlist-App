import { useContext, useEffect, useState } from 'react';
import './Profile.css';
import {authWrapper} from "./../../services/utils";
import { getProfile } from '../../services/profileService';
import AuthContext from '../Contexts/AuthContext';
import Spinner from "./../Utils/Spinner";

export default function Profile(props) {
    const [profileInfo, setProfileInfo] = useState({});
    const {setIsLoggedIn} = useContext(AuthContext);
    const [loading, setLoading] = useState(true);

    
    useEffect(
        () => {
            const makeRequest = async () => {
                const safeGetProfile = authWrapper(setIsLoggedIn, getProfile);
                const res = await safeGetProfile();
                if (res.success) {
                    setProfileInfo(res.data);
                } else {
                    console.log("STATUS CODE: " + res.code)
                    props.announce("We could not fetch your profile info :<...", "error");                   
                }

                setLoading(false);
            }
            makeRequest();
        }, [setIsLoggedIn, props]
    );

    return (
        <div id='Profile'>
            {/* banner */}
            <div id='Profile-banner-container'>
                <div id='Profile-banner'></div>
            </div>

            {/* profile info section */}
            <div id='Profile-info-container'>
                <div id='Profile-info'>
                    {/* My profile header*/}
                    <div id='Profile-info-header'>
                        <h1>MY PROFILE</h1>
                    </div>
                    {/* profile info rows and columns */}
                    <div className='Profile-info-row'> 
                        <div className='Profile-info-column-1'><p>First Name:</p></div>
                        <div className='Profile-info-column-2'><p className='Profile-users-data'>{profileInfo.firstName}</p></div>
                    </div>

                    <div className="Profile-info-row">
                        <div className='Profile-info-column-1'><p>Last Name:</p></div>
                        <div className='Profile-info-column-2'><p className='Profile-users-data'>{profileInfo.lastName}</p></div>
                    </div>
                        
                    <div className="Profile-info-row">
                        <div className='Profile-info-column-1'><p>Email:</p></div>
                        <div className='Profile-info-column-2'><p className='Profile-users-data'>{profileInfo.email}</p></div>
                    </div>
                </div>
            </div>
            {loading && <Spinner/>}
        </div>
    );
}
