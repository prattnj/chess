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
          <input type={"text"}/>
          <input type={"text"}/>
          <div className={"login-buttons"}>
            <div>Login</div>
            <div>Register</div>
          </div>
        </div>
      </>

  )
}

export default App