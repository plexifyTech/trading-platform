import axios, { AxiosError } from 'axios';
import { userUrl } from '../config';

export const checkIsAuth = async () => {
  const data = await axios
    .get(userUrl, { withCredentials: true })
    .then((res) => {
      return res.data;
    })
    .catch((error: AxiosError) => {
      if (error.response?.status === 401) {
        return undefined;
      } else if (error.request != null) {
        throw Error('The requested service is unavailable.');
      } else {
        throw Error('An unexpected error occured.');
      }
    });
  return data != null && data.username != null;
};
