import { useContext, useEffect, useState } from 'react';
import './Profile.css';
import {getToken} from "./../../services/utils";
import { getProfile } from '../../services/profileService';
import AuthContext from '../Contexts/AuthContext';

export default function Profile() {
    const [profileInfo, setProfileInfo] = useState({firstName:"Jenny", lastName:"Elie",email:"jenny@gmail.com"});
    const authState = useContext(AuthContext);
    
    useEffect(
        () => {
            const makeRequest = async () => {
                const token = await getToken(authState.setIsLoggedIn);
                console.log(token);
                const profile = await getProfile(token);
                setProfileInfo(profile)
            }
            makeRequest();
        }, [authState, setProfileInfo]
    );


    return (
        <div id='Profile'>
            {/* banner */}
            <div id='Profile-banner-container'>
                <div id='Profile-banner'></div>
            </div>
            {/* profile picture */}
            {/* <div id='Profile-picture-container'>
                <div id='Profile-picture'></div>
            </div> */}

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

                    <div class="Profile-info-row">
                        <div className='Profile-info-column-1'><p>Last Name:</p></div>
                        <div className='Profile-info-column-2'><p className='Profile-users-data'>{profileInfo.lastName}</p></div>
                    </div>
                        
                    <div class="Profile-info-row">
                        <div className='Profile-info-column-1'><p>Email:</p></div>
                        <div className='Profile-info-column-2'><p className='Profile-users-data'>{profileInfo.email}</p></div>
                    </div>
                </div>
            </div>   
        </div>
    );
}
