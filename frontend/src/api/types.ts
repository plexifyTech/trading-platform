interface Asset{
    id: string,
    fields: AssetFields,
}

interface AssetFields {
    details: AssetData,
    buyUrl: String,
    sellUrl: String
}

interface AssetData {
    name: string
    availableAssets: number,
    price: number[],
}

export type {
    Asset,
    AssetFields
}