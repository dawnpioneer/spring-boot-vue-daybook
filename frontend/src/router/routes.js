const routes = [
  {
    path: "/login",
    name: "login",
    component: () => import("pages/Login.vue")
  },
  {
    path: "/403",
    name: "403",
    component: () => import("pages/Error403")
  },
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('pages/Index.vue') },
      { path: '/daybookCategoryList', component: () => import('pages/daybookCategory/DaybookCategoryList.vue') },
      { path: '/profile', component: () => import('pages/user/Profile.vue') },
    ]
  },

  // Always leave this as last one,
  // but you can also remove it
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/Error404.vue')
  }
]

export default routes