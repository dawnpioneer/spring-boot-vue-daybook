import { api } from 'boot/axios'

const authService  = {
  get: function() {
    return api.get(`/test/user`);
  },
}

export { authService };
