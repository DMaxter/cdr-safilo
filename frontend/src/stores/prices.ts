import { AxiosError } from "axios";
import { defineStore } from "pinia";
import { ref } from "vue";

import { API } from "@router/backend";
import type { Error, APIResponse } from "@router/backend/types";
import { getErrorMessage } from "@router/backend/errors";
import { Price } from "@router/backend/services/price/types";

export const usePriceStore = defineStore("priceStore", () => {
  const prices = ref<Price[]>([]);

  function init(data: Price[]) {
    prices.value = data;
  }

  function _update(price: Price) {
    const index = prices.value.findIndex((p) => p.id === price.id);

    if (index === -1) {
      console.error(`Price ${price.id} not in store`);
      return;
    }

    prices.value[index] = price;
  }

  function remove(id: number) {
    prices.value = prices.value.filter((p) => p.id !== id);
  }

  async function getPrices(): Promise<APIResponse<string | null>> {
    try {
      const { status, data } = await API.prices.getAllPrices();

      if (status === 200) {
        init((data as Price[]).map((p) => new Price(p)));
        return {
          success: true,
          content: null,
        };
      } else {
        return {
          success: false,
          content: getErrorMessage(data),
          status: status,
        };
      }
    } catch (error) {
      const _error = error as AxiosError<Error>;
      return {
        success: false,
        status: _error.response?.status,
        content: getErrorMessage(_error.response?.data),
      };
    }
  }

  async function addPrice(price: Price): Promise<APIResponse<Price | string>> {
    try {
      const { status, data } = await API.prices.addPrice(price);

      if (status === 200) {
        const newPrice = new Price(data as Price);
        prices.value.push(newPrice);
        return {
          success: true,
          content: newPrice,
        };
      } else {
        return {
          success: false,
          content: getErrorMessage(data),
          status: status,
        };
      }
    } catch (error) {
      const _error = error as AxiosError<Error>;
      return {
        success: false,
        status: _error.response?.status,
        content: getErrorMessage(_error.response?.data),
      };
    }
  }

  async function editPrice(price: Price): Promise<APIResponse<Price | string>> {
    try {
      const { status, data } = await API.prices.updatePrice(price);

      if (status === 200) {
        const updatedPrice = new Price(data as Price);
        _update(updatedPrice);
        return {
          success: true,
          content: updatedPrice,
        };
      } else {
        return {
          success: false,
          content: getErrorMessage(data),
          status: status,
        };
      }
    } catch (error) {
      const _error = error as AxiosError<Error>;
      return {
        success: false,
        status: _error.response?.status,
        content: getErrorMessage(_error.response?.data),
      };
    }
  }

  async function deletePrice(id: number): Promise<APIResponse<string | null>> {
    try {
      const { status, data } = await API.prices.deletePrice(id);

      if (status === 200) {
        remove(id);
        return {
          success: true,
          content: null,
        };
      } else {
        return {
          success: false,
          content: getErrorMessage(data),
          status: status,
        };
      }
    } catch (error) {
      const _error = error as AxiosError<Error>;
      return {
        success: false,
        status: _error.response?.status,
        content: getErrorMessage(_error.response?.data),
      };
    }
  }

  return {
    prices,
    getPrices,
    addPrice,
    editPrice,
    deletePrice,
  };
});
