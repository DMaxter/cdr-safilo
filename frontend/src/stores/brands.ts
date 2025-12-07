import { AxiosError } from "axios";
import { defineStore } from "pinia";
import { ref } from "vue";

import { API } from "@router/backend";
import type { Error, APIResponse } from "@router/backend/types";
import { Brand } from "@router/backend/services/brand/types";
import { Image } from "@router/backend/services/image/types";

export const useBrandStore = defineStore("brandStore", () => {
  const brands = ref<Brand[]>([]);

  function init(data: Brand[]) {
    brands.value = data;
  }

  function add(brand: Brand) {
    brands.value.push(brand);
  }

  function _update(brand: Brand) {
    const index = brands.value.findIndex((b) => b.id === brand.id);

    if (index === -1) {
      console.error(`Brand ${brand.id} not in store`);
      return;
    }

    brands.value[index] = brand;
  }

  function remove(id: number) {
    brands.value = brands.value.filter((b) => b.id !== id);
  }

  function addImage(brandId: number, image: Image) {
    const brand = brands.value.find((b) => b.id === brandId);

    if (!brand) {
      console.error(`Brand ${brandId} not in store`);
      return;
    }

    brand.images.push(image);
  }

  function removeImage(imageId: number) {
    for (const brand of brands.value) {
      const imageIndex = brand.images.findIndex((i) => i.id === imageId);
      if (imageIndex !== -1) {
        brand.images.splice(imageIndex, 1);
        return;
      }
    }
    console.error(`Image ${imageId} not found in store`);
  }

  async function getBrands(): Promise<APIResponse<string | null>> {
    try {
      const { status, data } = await API.brands.getBrands();

      if (status === 200) {
        init(data as Brand[]);
        return {
          success: true,
          content: null,
        };
      } else {
        return {
          success: false,
          content: (data as Error).message,
          status: status,
        };
      }
    } catch (error) {
      const _error = error as AxiosError<Error>;
      return {
        success: false,
        status: _error.response?.status,
        content: _error.response?.data?.message,
      };
    }
  }

  async function addBrand(name: string): Promise<APIResponse<Brand | string>> {
    try {
      const { status, data } = await API.brands.createBrand(name);

      if (status === 200) {
        const newBrand = data as Brand;
        add(newBrand);
        return {
          success: true,
          content: newBrand,
        };
      } else {
        return {
          success: false,
          content: (data as Error).message,
          status: status,
        };
      }
    } catch (error) {
      const _error = error as AxiosError<Error>;
      return {
        success: false,
        status: _error.response?.status,
        content: _error.response?.data?.message,
      };
    }
  }

  async function editBrand(id: number, name: string): Promise<APIResponse<Brand | string>> {
    try {
      const { status, data } = await API.brands.editBrand(id, name);

      if (status === 200) {
        const updatedBrand = data as Brand;
        _update(updatedBrand);
        return {
          success: true,
          content: updatedBrand,
        };
      } else {
        return {
          success: false,
          content: (data as Error).message,
          status: status,
        };
      }
    } catch (error) {
      const _error = error as AxiosError<Error>;
      return {
        success: false,
        status: _error.response?.status,
        content: _error.response?.data?.message,
      };
    }
  }

  async function deleteBrand(id: number): Promise<APIResponse<string | null>> {
    try {
      const { status, data } = await API.brands.deleteBrand(id);

      if (status === 200) {
        remove(id);
        return {
          success: true,
          content: null,
        };
      } else {
        return {
          success: false,
          content: (data as Error).message,
          status: status,
        };
      }
    } catch (error) {
      const _error = error as AxiosError<Error>;
      return {
        success: false,
        status: _error.response?.status,
        content: _error.response?.data?.message,
      };
    }
  }

  async function addBrandImage(id: number, image: string): Promise<APIResponse<Image | string>> {
    try {
      const { status, data } = await API.brands.addBrandImage(id, image);

      if (status === 200) {
        const newImage = data as Image;
        addImage(id, newImage);
        return {
          success: true,
          content: newImage,
        };
      } else {
        return {
          success: false,
          content: (data as Error).message,
          status: status,
        };
      }
    } catch (error) {
      const _error = error as AxiosError<Error>;
      return {
        success: false,
        status: _error.response?.status,
        content: _error.response?.data?.message,
      };
    }
  }

  async function deleteBrandImage(id: number): Promise<APIResponse<string | null>> {
    try {
      const { status, data } = await API.brands.deleteBrandImage(id);

      if (status === 200) {
        removeImage(id);
        return {
          success: true,
          content: null,
        };
      } else {
        return {
          success: false,
          content: (data as Error).message,
          status: status,
        };
      }
    } catch (error) {
      const _error = error as AxiosError<Error>;
      return {
        success: false,
        status: _error.response?.status,
        content: _error.response?.data?.message,
      };
    }
  }

  async function makeImageObsolete(id: number): Promise<APIResponse<string | null>> {
    try {
      const { status, data } = await API.images.makeImageObsolete(id);

      if (status === 200) {
        let found = false;
        for (const brand of brands.value) {
          const image = brand.images.find((i) => i.id === id);
          if (image) {
            image.obsolete = true;
            found = true;
            break;
          }
        }
        if (!found) {
          console.error(`Image ${id} not found in store`);
        }

        return {
          success: true,
          content: null,
        };
      } else {
        return {
          success: false,
          content: (data as Error).message,
          status: status,
        };
      }
    } catch (error) {
      const _error = error as AxiosError<Error>;
      return {
        success: false,
        status: _error.response?.status,
        content: _error.response?.data?.message,
      };
    }
  }

  return {
    brands,
    getBrands,
    addBrand,
    editBrand,
    deleteBrand,
    addBrandImage,
    deleteBrandImage,
    makeImageObsolete,
  };
});
