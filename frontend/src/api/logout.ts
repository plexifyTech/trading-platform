import axios from 'axios';
import { fetchCsrfToken } from './fetchCsrfToken';
import { frontendBaseUrl, logoutUrl } from '../config';
import { checkIsAuth } from './checkIsAuth';

export const logout = async () => {
  const csrf = await fetchCsrfToken();
  if (csrf != null) {
    axios
      .post(
        logoutUrl,
        {},
        {
          withCredentials: true,
          headers: {
            'X-XSRF-TOKEN': csrf,
          },
        },
      )
      .then(() => {
        assertUserIsLoggedOut();
      })
      .catch((err) => {
        console.log(`Ohh no! ${err.message}`);
      });
  }
};

const assertUserIsLoggedOut = async () => {
  const result = await checkIsAuth().catch(() => false);
  assert(!result, 'Logout failed!');
  window.location.assign(frontendBaseUrl);
};

function assert(condition: boolean, msg?: string): asserts condition {
  if (!condition) throw new Error(msg);
}
