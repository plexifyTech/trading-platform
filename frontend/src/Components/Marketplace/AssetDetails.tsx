import React, { useEffect } from 'react';
import { Account, AssetFields, TransactionResponse } from '../../api/types';
import { Box, Button, Stack, Typography } from '@mui/material';
import { SparkLineChart } from '@mui/x-charts';
import { buyAsset } from '../../api/assetApiCalls';
import { FixedGrid } from './styles';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogActions from '@mui/material/DialogActions';

interface AssetDetailsProps {
  asset: AssetFields;
  onTxSuccessCallback: (account: Account) => void;
}

const AssetDetails = (props: AssetDetailsProps) => {
  const [currentPrice, setCurrentPrice] = React.useState<number>();
  const [availableAssets, setAvailableAssets] = React.useState<number>(
    props.asset.details.availableAssets,
  );
  const [dialogOpen, setDialogOpen] = React.useState<boolean>(false);
  const [dialogTitle, setDialogTitle] = React.useState<string>('');
  const [dialogMessage, setDialogMessage] = React.useState<string>('');

  useEffect(() => {
    setCurrentPrice(getCurrentPrice);
  });
  const getCurrentPrice = () => {
    const prices = props.asset.details.prices;
    const raw = prices[prices.length - 1];
    return Math.round((raw + Number.EPSILON) * 100) / 100;
  };

  const handleBuy = async () => {
    await buyAsset(props.asset, getCurrentPrice())
      .then((res: TransactionResponse) => {
        if (res.success) {
          setAvailableAssets(availableAssets - 1);
          setDialogTitle('SUCCESS');
          props.onTxSuccessCallback(res.account);
        } else {
          setDialogTitle('TRANSACTION FAILURE');
        }
        setDialogMessage(res.message);
        setDialogOpen(true);
      })
      .catch(() => {
        alert('kappoott');
      });
  };

  const assetUnavailable = () => {
    return availableAssets === 0;
  };

  const handleCloseDialog = () => {
    setDialogOpen(false);
  };

  return (
    <>
      <Dialog
        open={dialogOpen}
        onClose={handleCloseDialog}
        aria-labelledby='alert-dialog-title'
        aria-describedby='alert-dialog-description'
      >
        <DialogTitle id='alert-dialog-title'>{dialogTitle}</DialogTitle>
        <DialogContent>
          <DialogContentText id='alert-dialog-description'>
            {dialogMessage}
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog} autoFocus>
            OK
          </Button>
        </DialogActions>
      </Dialog>
      <Stack
        direction='column'
        spacing={2}
        justifyContent='space-between'
        alignItems='left'
        padding='2rem'
      >
        <Typography variant='h4'>{props.asset.details.name}</Typography>
        <Box sx={{ flexGrow: 1, backgroundColor: '#191b21', padding: '2rem' }}>
          <SparkLineChart
            data={props.asset.details.prices}
            height={100}
            colors={['#6c8da3']}
          />
        </Box>
        <FixedGrid>
          <Typography variant='h6'>CURRENT PRICE</Typography>
          <Typography variant='h5'>{currentPrice} $</Typography>
          <Typography variant='h6'>AVAILABLE</Typography>
          <Typography variant='h6'>{availableAssets}</Typography>
          <div>
            <Button
              onClick={handleBuy}
              disabled={assetUnavailable()}
              variant='contained'
              sx={{ width: '150px' }}
            >
              BUY
            </Button>
          </div>
        </FixedGrid>
      </Stack>
    </>
  );
};

export default AssetDetails;
