import { AxiosError } from "axios";
import { defineStore } from "pinia";
import { ref } from "vue";

import { API } from "@router/backend";
import type { Error, APIResponse } from "@router/backend/types";
import { getErrorMessage } from "@router/backend/errors";
import { Material } from "@router/backend/services/material/types";

export const useMaterialStore = defineStore("materialStore", () => {
  const materials = ref<Material[]>([]);

  function init(data: Material[]) {
    materials.value = data;
  }

  function add(material: Material) {
    materials.value.push(material);
  }

  function _update(material: Material) {
    const index = materials.value.findIndex((m) => m.id === material.id);

    if (index === -1) {
      console.error(`Material ${material.id} not in store`);
      return;
    }

    materials.value[index] = material;
  }

  function remove(id: number) {
    materials.value = materials.value.filter((m) => m.id !== id);
  }

  async function getMaterials(): Promise<APIResponse<string | null>> {
    try {
      const { status, data } = await API.materials.getAllMaterials();

      if (status === 200) {
        init(data as Material[]);
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

  async function addMaterial(material: Material): Promise<APIResponse<Material | string>> {
    try {
      const { status, data } = await API.materials.addMaterial(material);

      if (status === 200) {
        const newMaterial = new Material(data as Material);
        add(newMaterial);
        return {
          success: true,
          content: newMaterial,
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

  async function editMaterial(material: Material): Promise<APIResponse<Material | string>> {
    try {
      const { status, data } = await API.materials.updateMaterial(material);

      if (status === 200) {
        const updatedMaterial = new Material(data as Material);
        _update(updatedMaterial);
        return {
          success: true,
          content: updatedMaterial,
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

  async function deleteMaterial(id: number): Promise<APIResponse<string | null>> {
    try {
      const { status, data } = await API.materials.deleteMaterial(id);

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

  async function makeMaterialObsolete(id: number): Promise<APIResponse<string | null>> {
    try {
      const { status, data } = await API.materials.makeObsolete(id);

      if (status === 200) {
        const material = materials.value.find((m) => m.id === id);
        if (material) {
          material.obsolete = true;
        } else {
          console.error(`Material ${id} not found in store`);
        }

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
    materials,
    getMaterials,
    addMaterial,
    editMaterial,
    deleteMaterial,
    makeMaterialObsolete,
  };
});
