import { Container, Stack, Typography } from '@mui/material';
import { CenertedDiv } from '../Common/styles';
import React from 'react';

const Overview = () => {
  return (
    <Container>
      <CenertedDiv>
        <Stack spacing={2}>
          <Typography variant='h3'>...</Typography>
          <Typography variant='h6'>Next steps:</Typography>
          <Typography>
            1) Shift Asset state to a (Redux Toolkit/Mobx) store so it can be
            shared across tabs
          </Typography>
          <Typography>2) Implement the overview page seperately</Typography>
          <Typography>
            3) In the marketplace, give option to select number of assets to be
            sold (if user owns multiple)
          </Typography>
          <Typography>
            4) In the marketplace, give option to purchase multiple assets at
            one
          </Typography>
          <Typography>
            5) Backend: pull the auth package in a seperate service that
            functions as a reverse-proxy to provide auth to request from
            frontend to backend (auth server middleware)
          </Typography>
          <Typography>
            5) Backend: make the api a stateless (proper) REST API
          </Typography>
          <Typography>
            6) Keep a records of sales to see what you bought and sold for which
            prices.
          </Typography>
          <Typography>...</Typography>
        </Stack>
      </CenertedDiv>
    </Container>
  );
};

export default Overview;
