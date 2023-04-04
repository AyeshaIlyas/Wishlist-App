import { useContext, useEffect, useState } from 'react';
import './Profile.css';
import {authWrapper} from "./../../services/utils";
import { getProfile } from '../../services/profileService';
import AuthContext from '../Contexts/AuthContext';

export default function Profile() {
    const [profileInfo, setProfileInfo] = useState(null);
    const {setIsLoggedIn} = useContext(AuthContext);
    const [loading, setLoading] = useState(false);

    
    useEffect(
        () => {
            const makeRequest = async () => {
                const safeGetProfile = authWrapper(setIsLoggedIn, getProfile);
                const profile = await safeGetProfile();
                if (profile) {
                    setProfileInfo(profile);
                }
            }
            makeRequest();
            setLoading(false);
        }, [setIsLoggedIn]
    );

    const getProfile = () => {
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

    return (
        <>
            {loading ? <p>Loading...</p> : getProfile()}
        </>
    );
}
