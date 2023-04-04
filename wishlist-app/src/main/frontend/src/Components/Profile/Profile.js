import { useContext, useEffect, useState } from 'react';
import './Profile.css';
import {authWrapper} from "./../../services/utils";
import { getProfile } from '../../services/profileService';
import AuthContext from '../Contexts/AuthContext';

export default function Profile() {
    const [profileInfo, setProfileInfo] = useState(null);
    const {setIsLoggedIn} = useContext(AuthContext);
    
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
        }, [setIsLoggedIn]
    );


    return (
        <section>
            <h1>PROFILE</h1>
            { profileInfo ? 
                (
                    <>
                        <p>{profileInfo.firstName} {profileInfo.lastName}</p>
                        <p>Email: {profileInfo.email}</p>
                    </>
                )
                : <p>Loading profile info...</p>
            }

        </section>
    );
}
