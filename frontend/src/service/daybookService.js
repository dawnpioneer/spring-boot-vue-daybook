import { api } from 'boot/axios'

const daybookService  = {
  getAll: function(year) {
    return api.get(`/daybooks?year=${year}`);
  },

  getDaybookYearList: function() {
    return api.get(`/daybookYearList`);
  },

  get: function(id) {
    return api.get(`/daybooks/${id}`);
  },

  create: function(daybookCategoryId, data) {
    return api.post(`/daybookCategories/${daybookCategoryId}/daybooks`, data);
  },

  update: function(id, data) {
    return api.put(`/daybooks/${id}`, data);
  },

  delete: function(id) {
    return api.delete(`/daybooks/${id}`);
  }

}

export { daybookService };
