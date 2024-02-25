import {pubTradingApiUrl} from "../config";
import axios from "axios";

export const fetchAssets = async () => {
    return await axios
        .get(`${pubTradingApiUrl}/assets`, {withCredentials: true})
        .then((res) => {
            return res.data;
        })
        .catch(() => {
            return undefined;
        });
};