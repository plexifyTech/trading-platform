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
  Menu,
  MenuItem,
  Toolbar,
  Typography,
} from "@mui/material";
import React from "react";
import { AccountCircle } from "@mui/icons-material";
import MenuIcon from "@mui/icons-material/Menu";
import { Link } from "react-router-dom";
import AutoGraphIcon from "@mui/icons-material/AutoGraph";
import CurrencyExchangeIcon from "@mui/icons-material/CurrencyExchange";
import { NavbarBtnWrapper, NavbarList } from "./styles";

const Navbar = ({ isAuth }: { isAuth: boolean }) => {
  const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
  const [drawerOpen, setDrawerOpen] = React.useState<boolean>(false);
  const handleMenu = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const toggleDrawer = (newOpen: boolean) => (_: any) => {
    setDrawerOpen(newOpen);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  const DrawerList = (
    <Box sx={{ width: 250 }} role="presentation" onClick={toggleDrawer(false)}>
      <NavbarList>
        <ListItem
          component={Link}
          to="overview"
          key={"overview"}
          disablePadding
        >
          <ListItemButton>
            <ListItemIcon>
              <AutoGraphIcon />
            </ListItemIcon>
            <ListItemText primary={"Overview"} />
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
              <CurrencyExchangeIcon />
            </ListItemIcon>
            <ListItemText primary={"Buy and Sell"} />
            <Link to="buyandsell" />
          </ListItemButton>
        </ListItem>
      </NavbarList>
      <Divider />
      <NavbarBtnWrapper>
        <Button variant="contained">Logout</Button>
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
          sx={{ mr: 2 }}
        >
          <MenuIcon />
        </IconButton>
        <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
          TRADING 3000
        </Typography>
        {isAuth && (
          <div>
            <IconButton
              size="large"
              aria-label="account of current user"
              aria-controls="menu-appbar"
              aria-haspopup="true"
              onClick={handleMenu}
              color="inherit"
            >
              <AccountCircle />
            </IconButton>
            <Menu
              id="menu-appbar"
              anchorEl={anchorEl}
              anchorOrigin={{
                vertical: "top",
                horizontal: "right",
              }}
              keepMounted
              transformOrigin={{
                vertical: "top",
                horizontal: "right",
              }}
              open={Boolean(anchorEl)}
              onClose={handleClose}
            >
              <MenuItem onClick={handleClose}>Logout</MenuItem>
            </Menu>
          </div>
        )}
      </Toolbar>
      <Drawer open={drawerOpen} onClose={toggleDrawer(false)}>
        {DrawerList}
      </Drawer>
    </AppBar>
  );
};

export default Navbar;
