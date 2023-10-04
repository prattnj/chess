import './App.css';

function App() {
  return (
    <LoginScreen/>
  );
}

function LoginScreen() {

    return (
        <>
            <div className={"login-all"}>
                <span>Welcome!</span>
                <input className={"login-input"} type={"text"}/>
                <input className={"login-input"} type={"text"}/>
                <input className={"login-input"} type={"text"}/>
                <div className={"login-buttons"}>
                    <div className={"login-button"}>Login</div>
                    <div className={"login-button"}>Register</div>
                </div>
            </div>
        </>
    )
}

export default App