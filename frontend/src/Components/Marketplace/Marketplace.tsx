import React, {useEffect} from "react";
import {pubTradingApiUrl} from "../../config";
import {fetchAssets} from "../../api/fetchAssets";
import {CenertedDiv} from "../Common/styles";
import {CircularProgress, Container} from "@mui/material";
import {Asset} from "../../api/types";

const Marketplace = () => {
    const [assets, setAssets] = React.useState<Record<string, Asset>>();
    let eventSource

    function subscribeToAssets() {
        console.log("adding event listener...")
        eventSource = new EventSource(`${pubTradingApiUrl}/assets/stream`)
        eventSource.onmessage = (e: MessageEvent) => {
            console.log("received message!")
            console.dir(e.data)
        };
    }

    useEffect(() => {
        subscribeToAssets();
        if (!assets) {
            fetchAssets()
                .then((res) => {
                    setAssets(res)
                    if (res){
                        subscribeToAssets();
                    }
                })
        }
    }, [assets]);

    return (
        <>
            {assets ? Object.values(assets).map((asset: Asset) => (
                <div>{asset.name}</div>
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
