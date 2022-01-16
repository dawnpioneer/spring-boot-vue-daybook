import { api } from 'boot/axios'
import { authService } from "../../service";

export const login = async ({ commit }, payload) => {
  await api.post('/auth/signin/', payload).then(response => {
    const token = response.data.accessToken;
    commit('setToken', token)
    api.defaults.headers.common.Authorization = 'Bearer ' + token
    commit('setUser', response.data)
  })
}

export const logout = ({ commit }) => {
  api.defaults.headers.common.Authorization = ''
  commit('removeToken')
}

export const init = async ({ commit }) => {
  const token = localStorage.getItem('token')
  if (token) {
    await commit('setToken', token)
    api.defaults.headers.common.Authorization = 'Bearer ' + token
    await authService.get()
    .then(response => {
      console.log(response.data);
    })
    .catch(e => { // Unauthorized error
      commit('removeToken')
    });
  } else {
    commit('removeToken')
  }
}