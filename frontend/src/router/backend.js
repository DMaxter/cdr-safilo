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