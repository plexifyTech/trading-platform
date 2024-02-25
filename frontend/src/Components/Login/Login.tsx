import React, { useEffect } from 'react';
import { loginUrl } from '../../config';
import { CircularProgress, Container } from '@mui/material';
import { CenertedDiv } from '../Common/styles';

const Login = () => {
  useEffect(() => {
    window.location.assign(loginUrl);
  }, []);
  return (
    <Container>
      <CenertedDiv>
        <CircularProgress />
      </CenertedDiv>
    </Container>
  );
};

export default Login;
