import {clearStorage, LoginScreen} from "./util";
import '../css/menu.css'
import {useCallback, useEffect, useState} from "react";

export function MainMenu() {

    const [joinableGames, setJoinableGames] = useState([])
    const [observableGames, setObservableGames] = useState([])
    const [finishedGames, setFinishedGames] = useState([])
    const [selectedTab, setSelectedTab] = useState('joinable')
    const [displayedData, setDisplayedData] = useState('Loading...')
    const [createGamePopup, setCreateGamePopup] = useState(false)

    // styles
    const [joinableStyle, setJoinableStyle] = useState('tab-selected')
    const [observableStyle, setObservableStyle] = useState('tab-unselected')
    const [finishedStyle, setFinishedStyle] = useState('tab-unselected')

    const initializeGames = useCallback((games) => {
        let joinable = []
        let observable = []
        let finished = []
        games.sort((a, b) => {
            return a.gameID - b.gameID
        })
        games.forEach(game => {
            let username = localStorage.getItem('username')
            let gameJSX = <GameTile game={game} hasUser={game.whiteUsername === username || game.blackUsername === username}/>
            if (game.isOver) finished.push(gameJSX)
            else {
                observable.push(gameJSX)
                if (game.whiteUsername == null || game.blackUsername == null) joinable.push(gameJSX)
            }
        })
        setJoinableGames(joinable)
        setObservableGames(observable)
        setFinishedGames(finished)
        switch (selectedTab) {
            case 'joinable': {
                if (joinable.length === 0) setDisplayedData("Currently no joinable games.")
                else setDisplayedData(joinable)
                return
            }
            case 'observable': {
                if (observable.length === 0) setDisplayedData("Currently no observable games.")
                else setDisplayedData(observable)
                return
            }
            case 'finished': {
                if (finished.length === 0) setDisplayedData("Currently no finished games.")
                else setDisplayedData(finished)
                return
            }
            default: return
        }
    }, [selectedTab])

    const refreshGames = useCallback(() => {
        setDisplayedData('Loading...')
        fetch('/game', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem('token'),
            },
        }).then(r => r.json()).then(data => {
            console.log(data)
            if (data.success) {
                initializeGames(data.games)
                // setDisplayedData(joinableGames)
            } else {
                setDisplayedData("Unknown error occurred.")
            }
        })
    }, [initializeGames])

    useEffect(() => {
        if (localStorage.getItem('token') == null) return
        refreshGames()
    }, [refreshGames])

    function setToJoinable() {
        setJoinableStyle('tab-selected')
        setObservableStyle('tab-unselected')
        setFinishedStyle('tab-unselected')
        if (joinableGames.length === 0) setDisplayedData("Currently no joinable games.")
        else setDisplayedData(joinableGames)
        setSelectedTab('joinable')
    }

    function setToObservable() {
        setJoinableStyle('tab-unselected')
        setObservableStyle('tab-selected')
        setFinishedStyle('tab-unselected')
        if (observableGames.length === 0) setDisplayedData("Currently no observable games.")
        else setDisplayedData(observableGames)
        setSelectedTab('observable')
    }

    function setToFinished() {
        setJoinableStyle('tab-unselected')
        setObservableStyle('tab-unselected')
        setFinishedStyle('tab-selected')
        if (finishedGames.length === 0) setDisplayedData("Currently no finished games.")
        else setDisplayedData(finishedGames)
        setSelectedTab('finished')
    }

    // if not logged in, return login screen
    if (localStorage.getItem('token') == null) return <LoginScreen/>

    function GameTile(props) {

        let game = props.game
        let style = 'game-tile'
        if (props.hasUser) style += ' game-tile-user'

        return (
            <div className={style}>
                <div style={{fontSize: '1.5rem'}}><b>{game.gameName}</b></div>
                <div><b>White:</b> {game.whiteUsername}</div>
                <div><b>Black:</b> {game.blackUsername}</div>
                <div style={{fontSize: '.65rem'}}>{game.gameID}</div>
            </div>
        )
    }

    return (
        <>
            <div className={'menu-welcome'}>Welcome, <span onClick={clearStorage}>{localStorage.getItem('username')}!</span></div>
            <div style={{width: '50%', margin: '0 auto'}}>
                <div className={'tab-row'}>
                    <div className={'tab-group'}>
                        <div className={joinableStyle + ' tab'} onClick={setToJoinable}>Joinable Games</div>
                        <div className={observableStyle + ' tab'} onClick={setToObservable}>Observable Games</div>
                        <div className={finishedStyle + ' tab'} onClick={setToFinished}>Finished Games</div>
                    </div>
                    <div className={'tab-group'}>
                        <div className={'tab-action tab'} onClick={() => setCreateGamePopup(true)}>Create Game</div>
                        <div className={'tab-action tab no-right-margin'} onClick={refreshGames}>Refresh</div>
                    </div>
                </div>
                <div className={'menu-container'}>
                    {displayedData}
                </div>
            </div>
            {createGamePopup && (
                <CreateGamePopup
                    closePopup={() => setCreateGamePopup(false)}
                    refreshGames={() => refreshGames()}
                />
            )}
        </>
    )
}

function CreateGamePopup(props) {

    const [gameName, setGameName] = useState()
    const [selectedRadio, setSelectedRadio] = useState('n')
    const [errorMessage, setErrorMessage] = useState()

    function createGame() {
        fetch('/game', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem('token'),
            },
            body: JSON.stringify({
                "gameName": gameName
            })
        }).then(r => r.json()).then(data => {
            if (data.success) joinGame(data.gameID)
            else setErrorMessage(data.message)
        })
    }

    function joinGame(gameID) {
        if (selectedRadio === 'n') {
            props.refreshGames()
            props.closePopup()
            return;
        }
        let color = ""
        switch (selectedRadio) {
            case 'w': color = "WHITE"; break
            case 'b': color = "BLACK"; break
            default: break;
        }
        fetch('/game', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem('token')
            },
            body: JSON.stringify({
                "playerColor": color,
                "gameID": gameID
            })
        }).then(r => r.json()).then(data => {
            props.refreshGames()
            if (data.success) props.closePopup()
            else setErrorMessage("Game created, error when joining.")
        })
    }

    return (
        <div className={'cg-popup'}>
            <div className={'popup-content'}>
                <div className={'popup-title'}><b>Create Game</b></div>
                <input id={'name-input'}
                       className={'game-name-input'}
                       type={'text'}
                       placeholder={'Game name...'}
                       onChange={(e) => setGameName(e.target.value)}
                />
                <div className={'popup-title'}>Join game as...</div>
                <div className={'popup-radios'}>
                    <label>
                        <input
                            type={'radio'}
                            value={'white'}
                            checked={selectedRadio === 'w'}
                            onChange={() => setSelectedRadio('w')}
                        />
                        White player
                    </label>
                    <label>
                        <input
                            type={'radio'}
                            value={'black'}
                            checked={selectedRadio === 'b'}
                            onChange={() => setSelectedRadio('b')}
                        />
                        Black player
                    </label>
                    <label>
                        <input
                            type={'radio'}
                            value={'none'}
                            checked={selectedRadio === 'n'}
                            onChange={() => setSelectedRadio('n')}
                        />
                        Don't join
                    </label>
                </div>
                <div className={'popup-error'}>{errorMessage}</div>
                <div className={'popup-actions'}>
                    <div className={'popup-action tab-unselected'} onClick={props.closePopup}>Cancel</div>
                    <div className={'popup-action tab-action'} onClick={createGame}>Create</div>
                </div>
            </div>
        </div>
    )
}
