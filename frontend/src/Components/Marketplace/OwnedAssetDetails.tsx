import React from 'react';
import { Button, Stack, Typography } from '@mui/material';
import { Account, Share } from '../../api/types';
import { sellAsset } from '../../api/assetApiCalls';

interface OwnedAssetDetailsProps {
  share: Share;
  onSoldCallback: (account: Account) => void;
}
const OwnedAssetDetails = (props: OwnedAssetDetailsProps) => {
  const handleSale = async () => {
    sellAsset(props.share)
      .then((res) => {
        props.onSoldCallback(res.account);
      })
      .catch(() => {
        alert('kappoott');
      });
  };

  return (
    <Stack spacing={1}>
      <Typography>
        {props.share.label}: {props.share.quantity}{' '}
        {props.share.quantity > 1 ? 'shares' : 'share'}
      </Typography>
      <Typography>BOUGHT FOR</Typography>
      <Typography>{props.share.boughtForPrice} $</Typography>
      <div>
        <Button onClick={handleSale} variant='contained'>
          SELL
        </Button>
      </div>
    </Stack>
  );
};

export default OwnedAssetDetails;
