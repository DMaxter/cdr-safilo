import UserDto from "@/models/UserDto.ts";
import axios from "axios";

const httpClient = axios.create();

// Set default configuration for requests
httpClient.defaults.timeout = 10000;

if (import.meta.env.VUE_APP_ROOT_API) {
  httpClient.defaults.baseURL = import.meta.env.VUE_APP_ROOT_API;
} else {
  httpClient.defaults.baseURL = "http://localhost:8080";
}

httpClient.defaults.headers.post["Content-Type"] = "application/json";
httpClient.defaults.withCredentials = true; // Send cookies

export default class Backend {
  static async login(auth) {
    return httpClient
      .post("/auth/login", auth)
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async getProfile() {
    return httpClient
      .get("/user")
      .then((response) => {
        return new UserDto(response.data);
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.reponse));
      });
  }

  static async getUsers() {
    return httpClient
      .get("/user/all")
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.reponse));
      });
  }

  static async redefineWithToken(username) {
    return httpClient
      .post(`/auth/forgot/${username}`)
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async changePasswordWithToken(username, password, token) {
    return httpClient
      .post(`/auth/forgot/${username}/${password}/${token}`)
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async changePassword(oldPassword, newPassword) {
    return httpClient
      .put(`/user/password/${oldPassword}/${newPassword}`)
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async addMaterial(material) {
    return httpClient
      .post(`/material`, material)
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async addFinish(finish) {
    return httpClient
      .post(`/finishing`, finish)
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async getMaterials() {
    return httpClient
      .get("/material")
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.reponse));
      });
  }

  static async deleteMaterial(id) {
    return httpClient
      .delete(`/material/${id}`)
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.reponse));
      });
  }

  static async updateMaterial(material) {
    return httpClient
      .put(`/material/`, material)
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.reponse));
      });
  }

  static async getFinishes() {
    return httpClient
      .get("/finishing")
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.reponse));
      });
  }

  static async getFinishGroups() {
    return httpClient
      .get("/finishing/group")
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.reponse));
      });
  }

  static async getBrands() {
    return httpClient
      .get("/brand")
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.reponse));
      });
  }

  static async addBrand(brand) {
    return httpClient
      .post(`/brand/${brand}`)
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async updateBrand(id, brand) {
    return httpClient
      .put(`/brand/${id}/${brand}`)
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async deleteBrand(id) {
    return httpClient
      .delete(`/brand/${id}`)
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.reponse));
      });
  }

  static async deleteFinish(id) {
    return httpClient
      .delete(`/finishing/${id}`)
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.reponse));
      });
  }

  static async deleteFinishGroup(id) {
    return httpClient
      .delete(`/finishing/group/${id}`)
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.reponse));
      });
  }

  static async addClient(client) {
    return httpClient
      .post("/client", client)
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async editClient(client) {
    return httpClient
      .put(`/client`, client)
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async addClients(file) {
    var formData = new FormData();
    formData.append("file", file);
    return httpClient
      .post("/client/import", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
        timeout: 300000,
      })
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async getClients() {
    return httpClient
      .get("/client")
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.reponse));
      });
  }

  static async getAllRequests() {
    return httpClient
      .get(`/request/export`, { responseType: "blob", timeout: 30000 })
      .then((response) => {
        const url = window.URL.createObjectURL(response.data);
        const a = document.createElement("a");
        a.style.display = "none";
        a.href = url;
        a.download = `clients.csv`;
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
      })
      .catch(async (error) => {
        console.log(error);
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async addStore(id, store) {
    return httpClient
      .put(`/client/address/${id}`, store)
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async cancelRequest(id) {
    return httpClient
      .put(`/request/cancel/${id}`)
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async updatePlafond(user, brand, ammount) {
    return httpClient
      .put(`/user/plafond/${user}/${brand}/${ammount}`)
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async updateFinish(finish) {
    return httpClient
      .put(`/finishing`, finish)
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async updateFinishGroup(finish) {
    return httpClient
      .put(`/finishing/group`, finish)
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async logout() {
    return httpClient
      .delete("/auth/logout")
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async addComercial(client) {
    return httpClient
      .post("/auth/register", client)
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async addFinishGroup(name, group) {
    return httpClient
      .post(`/finishing/group/${name}`, group)
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async placeRequest(request) {
    return httpClient
      .post("/request", request)
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async addPrice(price) {
    return httpClient
      .post("/price", price)
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async getRequestPrice(request) {
    return httpClient
      .post("request/price", request)
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async deletePrice(price) {
    return httpClient
      .delete(`/price/${price}`)
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async updatePrice(price) {
    return httpClient
      .put("/price", price)
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async getPrices() {
    return httpClient
      .get("/price")
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async getRequests() {
    return httpClient
      .get("/request")
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async isLoggedIn() {
    return httpClient
      .get("/auth/logged")
      .then((response) => {
        // Empty response means user is authenticated
        if (response.status == 200 && response.data.length == 0) {
          return true;
        }

        // FIXME: Remove after migration to Rust
        return response.data;
      })
      .catch(async (error) => {
        // Unauthorized is not an error in this case
        if (error.response.status == 401) {
          return false;
        }
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async addImage(id, image) {
    return httpClient
      .post(`/brand/image/${id}`, image)
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async addNote(id, note) {
    return httpClient
      .put(`/client/${id}`, note)
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async editRequest(id, request) {
    return httpClient
      .put(`/request/${id}`, request)
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async addImageToClient(id, file) {
    var formData = new FormData();
    formData.append("images", file);
    return httpClient
      .post(`/image/client/${id}`, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      })
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async deleteImage(id) {
    return httpClient
      .delete(`/brand/image/${id}`)
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async obsoleteImage(id) {
    return httpClient
      .put(`/image/obsolete/${id}`)
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async getServices(id) {
    return httpClient
      .get(`/waybill/services/${id}`, { timeout: 300000 })
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async getPackages() {
    return httpClient
      .get(`/waybill/packages`)
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async getFormats() {
    return httpClient
      .get(`/waybill/formats`)
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async createWaybill(waybill, id) {
    return httpClient
      .post(`/waybill/${id}`, waybill, { responseType: "blob", timeout: 300000 })
      .then((response) => {
        const url = window.URL.createObjectURL(response.data);
        const a = document.createElement("a");
        a.style.display = "none";
        a.href = url;
        a.download = `CartaDePorte.pdf`;
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
      })
      .catch(async (error) => {
        error.response.blob = true;
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async downloadWaybill(format, id) {
    return httpClient
      .get(`/waybill/${id}/${format}`, { responseType: "blob", timeout: 300000 })
      .then((response) => {
        const url = window.URL.createObjectURL(response.data);
        const a = document.createElement("a");
        a.style.display = "none";
        a.href = url;
        a.download = `CartaDePorte.pdf`;
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
      })
      .catch(async (error) => {
        error.response.blob = true;
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async deleteWaybill(id) {
    return httpClient
      .delete(`/waybill/${id}`, { timeout: 300000 })
      .then((response) => {
        return response.data;
      })
      .catch(async (error) => {
        throw Error(await this.errorMessage(error.response));
      });
  }

  static async updateStatus(id, op) {
    if (op == "IN_PRODUCTION") {
      return httpClient
        .put(`/request/production/${id}`)
        .then((response) => {
          return response.data;
        })
        .catch(async (error) => {
          throw Error(await this.errorMessage(error.response));
        });
    } else if (op == "DONE") {
      return httpClient
        .put(`/request/finish/${id}`)
        .then((response) => {
          return response.data;
        })
        .catch(async (error) => {
          throw Error(await this.errorMessage(error.response));
        });
    } else if (op == "CANCELLED") {
      return httpClient
        .put(`/request/cancel/${id}`)
        .then((response) => {
          return response.data;
        })
        .catch(async (error) => {
          throw Error(await this.errorMessage(error.response));
        });
    }
  }

  static async errorMessage(error) {
    if (error.blob === undefined) {
      if (error.status == 400) {
        return error.data;
      } else if (error.status == 403) {
        return "Not enough permissions";
      } else if (error.status == 401) {
        return "Not logged in";
      } else if (error.status == 500) {
        return "Internal Server Error";
      } else {
        return "UNKNOWN " + error;
      }
    } else {
      if (error.status == 400) {
        let msg = await error.data.text();
        return msg;
      } else if (error.status == 403) {
        return "Not enough permissions";
      } else if (error.status == 401) {
        return "Not logged in";
      } else if (error.status == 500) {
        return "Internal Server Error";
      } else {
        return "UNKNOWN " + error;
      }
    }
  }
}
