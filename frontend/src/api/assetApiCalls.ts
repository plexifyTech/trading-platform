import {pubTradingApiUrl} from "../config";
import axios, {AxiosError} from "axios";
import {AssetFields} from "./types";
import {fetchCsrfToken} from "./fetchCsrfToken";

export const assetApiCalls = async () => {
    return await axios
        .get(`${pubTradingApiUrl}/assets`, {withCredentials: true})
        .then((res) => {
            return res.data;
        })
        .catch(() => {
            return undefined;
        });
};

export const buyAsset = async (asset: AssetFields, price: number | undefined)=> {
    if (!price){
        throw Error("No transaction without a defined price!")
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
            if (err.response){
                return err.response.data
            }
        })
}