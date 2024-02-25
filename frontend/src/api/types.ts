interface Asset {
  id: string;
  fields: AssetFields;
}

interface AssetFields {
  details: AssetData;
  buyUrl: string;
  sellUrl: string;
}

interface AssetData {
  name: string;
  availableAssets: number;
  prices: number[];
}

export type { Asset, AssetFields };
