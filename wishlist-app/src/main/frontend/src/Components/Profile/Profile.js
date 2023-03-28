import { useContext, useEffect, useState } from 'react';
import './Profile.css';
import {getToken} from "./../../services/utils";
import { getProfile } from '../../services/profileService';
import AuthContext from '../Contexts/AuthContext';

export default function Profile() {
    const [profileInfo, setProfileInfo] = useState();
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
        <section>
            <h1>PROFILE</h1>
            { profileInfo && 
            <>
                <p>{profileInfo.firstName} {profileInfo.lastName}</p>
                <p>Email: {profileInfo.email}</p>
            </>
            }
        </section>
    );
}
