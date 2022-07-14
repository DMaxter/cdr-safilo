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