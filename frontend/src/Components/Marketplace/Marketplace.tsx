import React, { useEffect, useRef } from 'react';
import { pubTradingApiUrl } from '../../config';
import { fetchAssets, fetchAccount } from '../../api/assetApiCalls';
import { CenertedDiv } from '../Common/styles';
import { CircularProgress, Container, Stack, Typography } from '@mui/material';
import { Account, Asset, Share } from '../../api/types';
import AssetDetails from './AssetDetails';
import { AxiosError } from 'axios';
import OwnedAssetDetails from './OwnedAssetDetails';
import { Wrapper } from './styles';

const Marketplace = () => {
  const [assets, setAssets] = React.useState<Asset[]>([]);
  const [account, setAccount] = React.useState<Account>();
  const [isInitialized, setIsInitialized] = React.useState<boolean>(false);
  const [error, setError] = React.useState<string>();
  const eventSourceRef = useRef<EventSource | null>(null);

  const subscribeToAssets = () => {
    const es = new EventSource(`${pubTradingApiUrl}/assets/stream`);
    es.onmessage = (e: MessageEvent) => {
      const data = JSON.parse(e.data);
      setAssets((currentAssets: Asset[]) => {
        const updatedAssets = [...currentAssets];
        const assetIndex = updatedAssets.findIndex((a) => a.id === data.id);
        if (assetIndex !== -1) {
          const updatedAsset = { ...updatedAssets[assetIndex] };
          updatedAsset.fields.details.prices.push(data.price);
          updatedAssets[assetIndex] = updatedAsset;
        }
        return updatedAssets;
      });
    };
    es.onerror = () => {
      es.close();
    };
    return es;
  };

  useEffect(() => {
    fetchAssets()
      .then((res) => {
        if (res != null) {
          setAssets(res);
        }
      })
      .catch((err: AxiosError) => setError(err.message))
      .finally(() => setIsInitialized(true));
  }, []);

  useEffect(() => {
    fetchAccount()
      .then((res) => {
        if (res != null) {
          setAccount(res);
        }
      })
      .catch((err: AxiosError) => setError(err.message));
  }, []);

  useEffect(() => {
    if (isInitialized && eventSourceRef.current == null) {
      eventSourceRef.current = subscribeToAssets();
    }
    return () => {
      eventSourceRef.current?.close();
    };
  }, [isInitialized]);

  if (error != null) {
    return (
      <Container>
        <CenertedDiv>
          <Stack>
            <Typography variant='h3'>
              Sorry! This app encountered a problem...
            </Typography>
            <Typography variant='h6'>{error}</Typography>
          </Stack>
        </CenertedDiv>
      </Container>
    );
  }

  return (
    <Wrapper>
      {account != null && (
        <Stack paddingTop='2rem' spacing={2}>
          <Typography variant='h3'>BALANCE</Typography>
          <Typography variant='h3'>
            {Math.round((account.balance + Number.EPSILON) * 100) / 100} $
          </Typography>
          {account.portfolio.map((share: Share) => (
            <OwnedAssetDetails
              share={share}
              onSoldCallback={function (account: Account): void {
                setAccount(account);
              }}
            />
          ))}
        </Stack>
      )}
      <Container>
        {assets.length > 0 ? (
          assets.map((asset: Asset) => (
            <AssetDetails
              asset={asset.fields}
              onTxSuccessCallback={function (account: Account): void {
                setAccount(account);
              }}
              key={asset.id}
            />
          ))
        ) : (
          <CenertedDiv>
            <CircularProgress />
          </CenertedDiv>
        )}
      </Container>
    </Wrapper>
  );
};

export default Marketplace;
