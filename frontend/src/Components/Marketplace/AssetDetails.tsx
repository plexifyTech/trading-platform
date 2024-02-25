import React from "react";
import {Asset, AssetFields} from "../../api/types";
import {Box, Container} from "@mui/material";
import {SparkLineChart} from "@mui/x-charts";

const AssetDetails = (asset: AssetFields) => {

    return (
                <Container>
                        <h1>{asset.details.name}</h1>
                        <div>{asset.details.price}</div>
                        <Box sx={{ flexGrow: 1 }}>
                            <SparkLineChart data={asset.details.price} height={100} />
                        </Box>
                </Container>
    )
};

export default AssetDetails;