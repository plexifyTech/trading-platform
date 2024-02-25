import React, {useEffect} from "react";
import {pubTradingApiUrl} from "../../config";
import {fetchAssets} from "../../api/fetchAssets";
import {CenertedDiv} from "../Common/styles";
import {CircularProgress, Container} from "@mui/material";
import {Asset} from "../../api/types";
import AssetDetails from "./AssetDetails";

const Marketplace = () => {
    const [assets, setAssets] = React.useState<Asset[]>();
    const [isInitialized, setIsInitialized] = React.useState<boolean>(false);
    let eventSource

    function subscribeToAssets() {
        eventSource = new EventSource(`${pubTradingApiUrl}/assets/stream`)
        eventSource.onmessage = (e: MessageEvent) => {
            const data = JSON.parse(e.data)
            if (assets) {
                //TODO Map access would be nicer here. May try again later...
                const idx = assets.findIndex((a) => a.id === data.id );
                assets[idx]?.fields.details.price.push(data.price)
                setAssets(assets)
            }
        };
    }

    useEffect(() => {
        if (!assets) {
            fetchAssets()
                .then((res) => {
                    if (res){
                        setAssets(res)
                    }
                })
                .finally(() => setIsInitialized(true))
        }
    }, [assets]);

    useEffect(() => {
        if (assets && isInitialized){
            subscribeToAssets()
        }
    }, [assets, isInitialized]);

    return (
        <>
            {assets ? assets.map((asset: Asset) => (
                <AssetDetails key={asset.id} {...asset.fields}/>
            )) : (
                <Container>
                    <CenertedDiv>
                        <CircularProgress/>
                    </CenertedDiv>
                </Container>
            )}
        </>
    )
};

export default Marketplace;
