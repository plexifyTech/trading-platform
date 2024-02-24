import axios from "axios";
import {fetchCsrfToken} from "./fetchCsrfToken";
import {logoutUrl} from "../config";

export const logout = async () => {
    const csrf = await fetchCsrfToken();
    if (csrf) {
        axios
            .post(
                logoutUrl,
                {},
                {
                    withCredentials: true,
                    headers: {
                        'X-XSRF-TOKEN': csrf,
                    }
                },
            )
            .catch((err) => {
                console.log(`Ohh no! ${err.message}`)
            })
    }
};