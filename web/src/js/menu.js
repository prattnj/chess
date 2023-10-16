import {clearStorage, LoginScreen} from "./util";
import '../css/menu.css'
import {useEffect, useState} from "react";

export function MainMenu() {

    const [joinableGames, setJoinableGames] = useState([])
    const [observableGames, setObservableGames] = useState([])
    const [finishedGames, setFinishedGames] = useState([])
    const [visibleGames, setVisibleGames] = useState([])
    const [selectedTab, setSelectedTab] = useState([])
    const [displayedData, setDisplayedData] = useState('Loading...')

    useEffect(() => {
        if (localStorage.getItem('token') == null) return

        // list games
        fetch('/game', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem('token'),
            },
        }).then(r => r.json()).then(data => {
            console.log(data)
            if (data.success) {
                if (data.games.length === 0) setDisplayedData("Currently no games.")
                else setDisplayedData(getGameList(data.games))
            } else {
                setDisplayedData("Unknown error occurred.")
            }
        })


    }, [])

    // if not logged in, return login screen
    if (localStorage.getItem('token') == null) return <LoginScreen/>

    return (
        <>
            <div className={"menu-welcome"}>Welcome, <span onClick={clearStorage}>{localStorage.getItem('username')}!</span></div>
            <div className={"menu-container"}>
                {displayedData}
            </div>
        </>
    )
}

function GameTile(props) {

    let game = props.game

    return (
        <div className={'game-tile'}>
            <div>{game.gameName}</div>
            <div>White: {game.whiteUsername}</div>
            <div>Black: {game.blackUsername}</div>
            <div>{game.gameID}</div>
        </div>
    )
}

function getGameList(games) {
    let gameList = []
    games.forEach(game => {
        gameList.push(<GameTile game={game}/>)
    })
    return gameList
}