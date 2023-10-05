import {clearStorage, LoginScreen} from "./util";
import './menu.css'

export function MainMenu() {

    // if not logged in, return login screen
    if (localStorage.getItem('token') == null) {
        return <LoginScreen/>
    }

    return (
        <>
            <div>Main menu <span onClick={clearStorage}>here</span></div>
        </>
    )
}