import styled from "styled-components";
import { List } from "@mui/material";

const NavbarBtnWrapper = styled.div`
  display: flex;
  justify-content: center;
  width: 100%;
  padding-top: 1rem;
`;

const NavbarList = styled(List)`
  a {
    text-decoration: none;
    color: black;
  }
`;

export { NavbarBtnWrapper, NavbarList };
