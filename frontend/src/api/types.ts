interface Asset {
  id: string;
  fields: AssetFields;
}

interface AssetFields {
  details: AssetData;
  buyUrl: string;
}

interface AssetData {
  name: string;
  availableAssets: number;
  prices: number[];
}

interface Account {
  portfolio: Share[];
  balance: number;
}

interface Share {
  id: string;
  boughtForPrice: number;
  label: string;
  quantity: number;
  sellUrl: string;
}

interface TransactionResponse {
  success: boolean;
  message: string;
  account: Account;
}

export type { Asset, AssetFields, Account, Share, TransactionResponse };
