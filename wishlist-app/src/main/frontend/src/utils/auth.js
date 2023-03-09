export function loggedIn() {
    // TODO: check token validity first
    return localStorage.getItem("token") != null
}

export function logout() {
    if (loggedIn) {
        localStorage.removeItem("token")
    }
}

export function logIn(token) {
    localStorage.setItem("token", token)
}

export function getAuthToken() {
    // TODO: if loggedIn (token exists and is valid) then get token otherwise return null
    return localStorage.getItem("token")
}