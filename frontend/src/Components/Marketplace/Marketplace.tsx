import React, {useEffect} from "react";
import {pubTradingApiUrl} from "../../config";
import {assetApiCalls} from "../../api/assetApiCalls";
import {CenertedDiv} from "../Common/styles";
import {CircularProgress, Container} from "@mui/material";
import {Asset} from "../../api/types";
import AssetDetails from "./AssetDetails";

//this is very ugly but using useState sadly did not work
let eventSource: EventSource | undefined
const Marketplace = () => {
    const [assets, setAssets] = React.useState<Asset[]>();
    const [isInitialized, setIsInitialized] = React.useState<boolean>(false);

    const  subscribeToAssets = () => {
        const es = new EventSource(`${pubTradingApiUrl}/assets/stream`)
        es.onmessage = (e: MessageEvent) => {
            const data = JSON.parse(e.data)
            if (assets) {
                //TODO Map access would be nicer here. May try again later...
                const idx = assets.findIndex((a) => a.id === data.id );
                assets[idx]?.fields.details.prices.push(data.price)
                setAssets([...assets])
            }
        };
        es.onerror = (_) => {
            es.close()
        };
        return es
    }

    useEffect(() => {
        if (!assets) {
            assetApiCalls()
                .then((res) => {
                    if (res){
                        setAssets(res)
                    }
                })
                .finally(() => setIsInitialized(true))
        }
    }, [assets]);

    useEffect(() => {
        if (isInitialized && !eventSource){
            eventSource = subscribeToAssets()
        }
    }, [isInitialized]);

    //close stream on component unmount
    useEffect(() => {
        return () => {
            if (eventSource){
                eventSource.close()
                eventSource = undefined
            }
        }
    }, []);

    return (
        <Container>
            {assets ? assets.map((asset: Asset) => (
                <AssetDetails key={asset.id} {...asset.fields}/>
            )) : (
                    <CenertedDiv>
                        <CircularProgress/>
                    </CenertedDiv>
            )}
        </Container>
    )
};

export default Marketplace;
