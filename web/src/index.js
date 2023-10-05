import React from 'react';
import ReactDOM from 'react-dom/client';
import {BrowserRouter, Route, Routes} from "react-router-dom"
import './index.css';
import {MainMenu} from "./menu";
import {Play} from "./play";
import {Watch} from "./watch";

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
