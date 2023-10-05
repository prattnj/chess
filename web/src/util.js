import {useEffect, useState} from "react";
import './util.css'

export function LoginScreen() {

    const buttonDisabled = 'login-button-disabled'
    const buttonEnabled = 'login-button-enabled'
    const [usernameText, setUsernameText] = useState()
    const [passwordText, setPasswordText] = useState()
    const [emailText, setEmailText] = useState()
    const [loginButtonStyle, setLoginButtonStyle] = useState(buttonDisabled)
    const [registerButtonStyle, setRegisterButtonStyle] = useState(buttonDisabled)
    const [errorMessage, setErrorMessage] = useState(null)

    useEffect(() => {
        if (usernameText !== undefined && usernameText !== '' && passwordText !== undefined && passwordText !== '') {
            setLoginButtonStyle(buttonEnabled)
            if (emailText !== undefined && emailText !== '') setRegisterButtonStyle(buttonEnabled)
            else setRegisterButtonStyle(buttonDisabled)
        } else {
            setLoginButtonStyle(buttonDisabled)
            setRegisterButtonStyle(buttonDisabled)
        }
    }, [usernameText, passwordText, emailText])

    function login() {
        if (loginButtonStyle === 'login-button-disabled') return
        fetch('/session', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username: usernameText,
                password: passwordText,
            })
        }).then(r => r.json()).then(data => {
            if (!data.success) {
                setErrorMessage(data.message)
            } else {
                console.log(data)
                localStorage.setItem('token', data.authToken)
                window.location.reload()
            }
        })
    }

    function register() {
        if (registerButtonStyle === 'login-button-disabled') return
        fetch('/user', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username: usernameText,
                password: passwordText,
                email: emailText,
            })
        }).then(r => r.json()).then(data => {
            if (!data.success) {
                setErrorMessage(data.message)
            } else {
                console.log(data)
                localStorage.setItem('token', data.authToken)
                window.location.reload()
            }
        })
    }

    return (
        <>
            <div className={'login-all'}>
                <span style={{marginBottom: '.5rem'}}>Welcome to online chess! Login or register below...</span>
                <input id={'username-input'}
                       className={'login-input'}
                       type={'text'}
                       placeholder={'Username'}
                       onChange={(e) => setUsernameText(e.target.value)}
                />
                <input id={'password-input'}
                       className={'login-input'}
                       type={'text'}
                       placeholder={'Password'}
                       onChange={(e) => setPasswordText(e.target.value)}
                />
                <input id={'email-input'}
                       className={'login-input'}
                       type={'text'}
                       placeholder={'Email (register only)'}
                       onChange={(e) => setEmailText(e.target.value)}
                />
                <div className={'error-message'}>{errorMessage}</div>
                <div className={'login-buttons'}>
                    <div className={loginButtonStyle + ' login-button'} onClick={() => login()}>Login</div>
                    <div className={registerButtonStyle + ' login-button'} onClick={() => register()}>Register</div>
                </div>
            </div>
        </>
    )
}

export function clearStorage() {
    localStorage.clear()
    sessionStorage.clear()
}