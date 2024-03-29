import { privTradingApiUrl, pubTradingApiUrl } from '../config';
import axios, { AxiosError } from 'axios';
import { AssetFields, Share } from './types';
import { fetchCsrfToken } from './fetchCsrfToken';

export const fetchAssets = async () => {
  return await axios
    .get(`${pubTradingApiUrl}/assets`, { withCredentials: true })
    .then((res) => {
      return res.data;
    });
};

export const fetchAccount = async () => {
  return await axios
    .get(`${privTradingApiUrl}/assets/account`, { withCredentials: true })
    .then((res) => {
      return res.data;
    });
};

export const buyAsset = async (
  asset: AssetFields,
  price: number | undefined,
) => {
  if (price == null) {
    throw Error('No transaction without a defined price!');
  }
  const csrf = await fetchCsrfToken();
  return axios
    .put(
      asset.buyUrl,
      { price },
      {
        withCredentials: true,
        headers: {
          'X-XSRF-TOKEN': csrf,
        },
      },
    )
    .then((res) => {
      return res.data;
    })
    .catch((err: AxiosError) => {
      if (err.response) {
        return err.response.data;
      }
    });
};

export const sellAsset = async (share: Share) => {
  const csrf = await fetchCsrfToken();
  return axios
    .put(
      share.sellUrl,
      {},
      {
        withCredentials: true,
        headers: {
          'X-XSRF-TOKEN': csrf,
        },
      },
    )
    .then((res) => {
      return res.data;
    })
    .catch((err: AxiosError) => {
      if (err.response) {
        return err.response.data;
      }
    });
};
