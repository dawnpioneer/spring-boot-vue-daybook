import { api } from 'boot/axios'

const daybookCategoryService  = {
  getAll: function(category, name) {
    return api.get(`/daybookCategories?category=${category}&name=${name}`);
  },

  get: function(id) {
    return api.get(`/daybookCategories/${id}`);
  },

  create: function(data) {
    return api.post("/daybookCategories", data);
  },

  update: function(id, data) {
    return api.put(`/daybookCategories/${id}`, data);
  },

  delete: function(id) {
    return api.delete(`/daybookCategories/${id}`);
  }

}

export { daybookCategoryService };
