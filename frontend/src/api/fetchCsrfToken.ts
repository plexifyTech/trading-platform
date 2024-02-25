import axios from 'axios';
import { csrfTokenUrl } from '../config';

export const fetchCsrfToken = async () => {
  const data = await axios
    .get(csrfTokenUrl, { withCredentials: true })
    .then((res) => {
      return res.data;
    })
    .catch(() => {
      return undefined;
    });
  return data.csrf;
};
