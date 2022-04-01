export const setToken = (state, token) => {
  state.token = token
  window.localStorage.setItem('token', token)
}

export const removeToken = (state) => {
  state.token = ''
  window.localStorage.setItem('token', '')
}

export const setUser = (state, user) => {
  state.user = user
}
