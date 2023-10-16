import React from 'react';
import ReactDOM from 'react-dom/client';
import {BrowserRouter, Route, Routes} from "react-router-dom"
import './css/index.css';
import {MainMenu} from "./js/menu";
import {Play} from "./js/play";
import {Watch} from "./js/watch";

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <BrowserRouter>
        <Routes>
            <Route path={"/"} element={<MainMenu/>} />
            <Route path={"/play"} element={<Play/>} />
            <Route path={"/watch"} element={<Watch/>} />
        </Routes>
    </BrowserRouter>
);
