import React from "react";
import {AssetFields} from "../../api/types";
import {Box, Button, Stack, Typography} from "@mui/material";
import {SparkLineChart} from "@mui/x-charts";

const AssetDetails = (asset: AssetFields) => {
    return (
        <Stack
            direction="column"
            spacing={2}
            justifyContent="space-between"
            alignItems="left"
            padding="2rem"
        >
            <Typography variant="h4">{asset.details.name}</Typography>
            <Box sx={{flexGrow: 1, backgroundColor: "#191b21", padding: "2rem"}} >
                <SparkLineChart data={asset.details.price} height={100} colors={["#6c8da3"]}/>
            </Box>
            <Stack direction="row" spacing={2}>
                <Typography><span>Current price:</span><strong>TODO</strong></Typography>
                <Button variant="contained">BUY</Button>
            </Stack>
        </Stack>
    )
};

export default AssetDetails;