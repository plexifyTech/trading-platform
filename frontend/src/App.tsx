import React from "react";
import "./App.css";
import { RouterProps } from "./types";
import { CircularProgress, Container } from "@mui/material";
import Navbar from "./Components/Navbar/Navbar";
import { AppContent } from "./styles";
import { Navigate, Route, Routes } from "react-router-dom";
import Login from "./Components/Login/Login";
import Overview from "./Components/Overview/Overview";
import Marketplace from "./Components/Marketplace/Marketplace";

const AppRoutes = ({ isAuth, isLoading }: RouterProps) => {
  if (isLoading) {
    return (
      <Container>
        <CircularProgress />
      </Container>
    );
  } else {
    return (
      <>
        <Navbar isAuth={isAuth} />
        <AppContent>
          <Routes>
            <Route
              index
              element={
                isAuth ? (
                  <Navigate to="/overview" replace />
                ) : (
                  <Navigate to="/login" />
                )
              }
            />
            <Route
              path="login"
              element={isAuth ? <Navigate to="/overview" replace /> : <Login />}
            />
            <Route
              path="overview"
              element={isAuth ? <Overview /> : <Navigate to="/login" />}
            />
            <Route
              path="buyandsell"
              element={isAuth ? <Marketplace /> : <Navigate to="/login" />}
            />
          </Routes>
        </AppContent>
      </>
    );
  }
};

const App = () => {
  return <AppRoutes isAuth={true} isLoading={false} />;
};

export default App;
