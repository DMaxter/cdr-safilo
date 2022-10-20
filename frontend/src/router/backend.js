import UserDto from '@/models/UserDto';
import axios from 'axios'

const httpClient = axios.create();

// Set default configuration for requests
httpClient.defaults.timeout = 10000;
httpClient.defaults.baseURL = process.env.ROOT_API || 'http://localhost:8080'
httpClient.defaults.headers.post['Content-Type'] = 'application/json'
httpClient.defaults.withCredentials = true // Send cookies

export default class Backend {
  static async login(auth) {
    return httpClient.post('/auth/login', auth)
      .then(response => {
        return response.data
      })
      .catch(async error => {
        throw Error(await this.errorMessage(error.response))
      })
  }

  static async getProfile() {
    return httpClient.get("/user").then(response => {
      return new UserDto(response.data)
    })
      .catch(async error => {
      throw Error(await this.errorMessage(error.reponse))
    })
  }

  static async getUsers() {
    return httpClient.get("/auth/all").then(response => {
      return response.data
    })
      .catch(async error => {
      throw Error(await this.errorMessage(error.reponse))
    })
  }

  static async redefineWithToken(username) {
    return httpClient.post(`/auth/forgot/${username}`).then(response => {
      return response.data
    })
      .catch(async error => {
      throw Error(await this.errorMessage(error.response))
    })
  }

  static async changePassword(oldPassword, newPassword) {
    return httpClient.put(`/user/password/${oldPassword}/${newPassword}`).then(response => {
      return response.data
    })
      .catch(async error => {
      throw Error(await this.errorMessage(error.response))
    })
  }

  static async addMaterial(material, cost) {
    return httpClient.post(`/material/${material}/${cost}`).then(response => {
      return response.data
    })
      .catch(async error => {
      throw Error(await this.errorMessage(error.response))
    })
  }

  static async getMaterials() {
    return httpClient.get("/material").then(response => {
      return response.data
    })
      .catch(async error => {
      throw Error(await this.errorMessage(error.reponse))
    })
  }

  static async deleteMaterial(id) {
    return httpClient.delete(`/material/${id}`).then(response => {
      return response.data
    })
      .catch(async error => {
      throw Error(await this.errorMessage(error.reponse))
    })
  }

  static async updateMaterial(id, name, cost) {
    return httpClient.put(`/material/${id}/${name}/${cost}`).then(response => {
      return response.data
    })
      .catch(async error => {
      throw Error(await this.errorMessage(error.reponse))
    })
  }

  static async getBrands() {
    return httpClient.get("/brand").then(response => {
      return response.data
    })
      .catch(async error => {
      throw Error(await this.errorMessage(error.reponse))
    })
  }

  static async addBrand(brand) {
    return httpClient.post(`/brand/${brand}`).then(response => {
      return response.data
    })
      .catch(async error => {
      throw Error(await this.errorMessage(error.response))
    })
  }

  static async deleteBrand(id) {
    return httpClient.delete(`/brand/${id}`).then(response => {
      return response.data
    })
      .catch(async error => {
      throw Error(await this.errorMessage(error.reponse))
    })
  }

  static async addClient(client) {
    return httpClient.post("/client", client).then(response => {
      return response.data
    })
      .catch(async error => {
      throw Error(await this.errorMessage(error.response))
    })
  }

  static async getClients() {
    return httpClient.get("/client").then(response => {
      return response.data
    })
      .catch(async error => {
      throw Error(await this.errorMessage(error.reponse))
    })
  }

  static async addStore(id, store) {
    return httpClient.put(`/client/address/${id}`, store).then(response => {
      return response.data
    })
      .catch(async error => {
      throw Error(await this.errorMessage(error.response))
    })
  }

  static async updatePlafond(user, brand, ammount) {
    return httpClient.put(`/user/plafond/${user}/${brand}/${ammount}`).then(response => {
      return response.data
    })
      .catch(async error => {
      throw Error(await this.errorMessage(error.response))
    })
  }

  static async logout() {
    return httpClient.delete('/auth/logout')
      .then(response => {
        return response.data
      })
      .catch(async error => {
        throw Error(await this.errorMessage(error.response))
      })
  }

  static async addComercial(client) {
    return httpClient.post("/auth/register", client).then(response => {
      return response.data
    })
      .catch(async error => {
      throw Error(await this.errorMessage(error.response))
    })
  }

  static async placeRequest(request) {
    return httpClient.post("/request", request).then(response => {
      return response.data
    })
      .catch(async error => {
      throw Error(await this.errorMessage(error.response))
    })
  }

  static async getRequests() {
    return httpClient.get("/request").then(response => {
      return response.data
    })
      .catch(async error => {
      throw Error(await this.errorMessage(error.response))
    })
  }

  static async isLoggedIn() {
    return httpClient.get("/auth/logged").then(response => {
      return response.data
    })
      .catch(async error => {
      throw Error(await this.errorMessage(error.response))
    })
  }

  static async addImage(id, image) {
    return httpClient.post(`/brand/image/${id}`, image).then(response => {
      return response.data
    })
      .catch(async error => {
      throw Error(await this.errorMessage(error.response))
    })
  }

  static async deleteImage(id) {
    return httpClient.delete(`/brand/image/${id}`).then(response => {
      return response.data
    })
      .catch(async error => {
      throw Error(await this.errorMessage(error.response))
    })
  }

  static async updateStatus(id, op) {
    if(op == 'IN_PRODUCTION'){
    return httpClient.put(`/request/production/${id}`).then(response => {
      return response.data
    })
      .catch(async error => {
        throw Error(await this.errorMessage(error.response))
      })
    } else if(op == 'DONE'){
      return httpClient.put(`/request/finish/${id}`).then(response => {
        return response.data
      })
        .catch(async error => {
          throw Error(await this.errorMessage(error.response))
        })
    } else if( op == 'CANCELLED'){
      return httpClient.put(`/request/cancel/${id}`).then(response => {
        return response.data
      })
        .catch(async error => {
          throw Error(await this.errorMessage(error.response))
        })
    }
  }


  static async errorMessage(error) {
    if (error.status == 400) {
      return error.data.msg
    } else if (error.status == 403) {
      return 'Not enough permissions'
    } else if (error.status == 401) {
      return 'Not logged in'
    } else if (error.status == 500) {
      return 'Internal Server Error'
    } else {
      return 'UNKNOWN ' + error
    }
  }
}