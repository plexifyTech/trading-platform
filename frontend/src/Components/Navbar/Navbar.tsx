import {
    AppBar,
    Box,
    Button,
    Divider,
    Drawer,
    IconButton,
    ListItem,
    ListItemButton,
    ListItemIcon,
    ListItemText,
    Toolbar,
    Typography,
} from "@mui/material";
import React from "react";
import MenuIcon from "@mui/icons-material/Menu";
import {Link} from "react-router-dom";
import AutoGraphIcon from "@mui/icons-material/AutoGraph";
import CurrencyExchangeIcon from "@mui/icons-material/CurrencyExchange";
import {NavbarBtnWrapper, NavbarList} from "./styles";
import {frontendBaseUrl} from "../../config";
import {logout} from "../../api/logout";

const Navbar = () => {
    const [drawerOpen, setDrawerOpen] = React.useState<boolean>(false);

    const toggleDrawer = (newOpen: boolean) => (_: any) => {
        setDrawerOpen(newOpen);
    };

    const handleLogout = () => {
        logout()
            .then(() => {
                window.location.assign(frontendBaseUrl)
            })
    };

    const DrawerList = (
        <Box sx={{width: 250}} role="presentation" onClick={toggleDrawer(false)}>
            <NavbarList>
                <ListItem
                    component={Link}
                    to="overview"
                    key={"overview"}
                    disablePadding
                >
                    <ListItemButton>
                        <ListItemIcon>
                            <AutoGraphIcon/>
                        </ListItemIcon>
                        <ListItemText primary={"Overview"}/>
                    </ListItemButton>
                </ListItem>
                <ListItem
                    component={Link}
                    to="buyandsell"
                    key={"buyandsell"}
                    disablePadding
                >
                    <ListItemButton>
                        <ListItemIcon>
                            <CurrencyExchangeIcon/>
                        </ListItemIcon>
                        <ListItemText primary={"Buy and Sell"}/>
                    </ListItemButton>
                </ListItem>
            </NavbarList>
            <Divider/>
            <NavbarBtnWrapper>
                <Button variant="contained" onClick={handleLogout}>Logout</Button>
            </NavbarBtnWrapper>
        </Box>
    );

    return (
        <AppBar position="static">
            <Toolbar>
                <IconButton
                    size="large"
                    edge="start"
                    color="inherit"
                    aria-label="menu"
                    onClick={toggleDrawer(true)}
                    sx={{mr: 2}}
                >
                    <MenuIcon/>
                </IconButton>
                <Typography variant="h6" component="div" sx={{flexGrow: 1}}>
                    TRADING 3000
                </Typography>
            </Toolbar>
            <Drawer open={drawerOpen} onClose={toggleDrawer(false)}>
                {DrawerList}
            </Drawer>
        </AppBar>
    );
};

export default Navbar;
