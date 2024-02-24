import React, {useEffect} from "react";
import "./App.css";
import {RouterProps} from "./types";
import {CircularProgress, Container, Typography} from "@mui/material";
import Navbar from "./Components/Navbar/Navbar";
import {AppContent} from "./styles";
import {Navigate, Route, Routes} from "react-router-dom";
import Login from "./Components/Login/Login";
import Overview from "./Components/Overview/Overview";
import Marketplace from "./Components/Marketplace/Marketplace";
import {checkIsAuth} from "./api/checkIsAuth";
import {CenertedDiv} from "./Components/Common/styles";

const AppRoutes = ({isAuth, isLoading}: RouterProps) => {
    if (isLoading) {
        return (
            <Container>
                <CenertedDiv>
                    <CircularProgress/>
                </CenertedDiv>
            </Container>
        );
    } else {
        return (
            <>
                <Navbar />
                <AppContent>
                    <Routes>
                        <Route
                            index
                            element={
                                isAuth ? (
                                    <Navigate to="/overview" replace/>
                                ) : (
                                    <Navigate to="/login"/>
                                )
                            }
                        />
                        <Route
                            path="login"
                            element={isAuth ? <Navigate to="/overview" replace/> : <Login/>}
                        />
                        <Route
                            path="overview"
                            element={isAuth ? <Overview/> : <Navigate to="/login"/>}
                        />
                        <Route
                            path="buyandsell"
                            element={isAuth ? <Marketplace/> : <Navigate to="/login"/>}
                        />
                    </Routes>
                </AppContent>
            </>
        );
    }
};

const App = () => {
    const [isAuth, setIsAuth] = React.useState<boolean>(false);
    const [isLoading, setIsLoading] = React.useState<boolean>(true);
    const [error, setError] = React.useState<Error>();

    useEffect(() => {
        if (!isAuth && !error) {
            setIsLoading(true)
            checkIsAuth()
                .then((isAuth) => {
                    setIsAuth(isAuth)
                })
                .catch((error) => {
                    setError(error)
            }).finally(() => {
                setIsLoading(false)
            })
        }
    }, [isAuth, error]);

    if (error){
        return <Container>
            <CenertedDiv>
                <Typography>Sorry! This app encountered a problem...</Typography>
                <p>${error.message}</p>
            </CenertedDiv>
        </Container>
    } else {
        return <AppRoutes isAuth={isAuth} isLoading={isLoading}/>;
    }
};

export default App;
