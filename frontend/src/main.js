import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import 'mdb-vue-ui-kit/css/mdb.min.css';
import './assets/main.css';
import '@fortawesome/fontawesome-free/css/all.css'
import '@fortawesome/fontawesome-free/js/all.js'
import { 
  MDBContainer,
  MDBNavbar,
  MDBNavbarToggler,
  MDBNavbarBrand,
  MDBNavbarNav,
  MDBNavbarItem,
  MDBCollapse,
  MDBDropdown,
  MDBDropdownToggle,
  MDBDropdownMenu,
  MDBDropdownItem,
  MDBCol,
  MDBRow,
  MDBInput,
  MDBCheckbox,
  MDBBtn
} from "mdb-vue-ui-kit";

createApp(App)
  .use(router)
  .use(store)
  .component("MDBContainer", MDBContainer)
  .component("MDBNavbar", MDBNavbar)
  .component("MDBNavbarToggler", MDBNavbarToggler)
  .component("MDBNavbarBrand", MDBNavbarBrand)
  .component("MDBNavbarNav", MDBNavbarNav)
  .component("MDBNavbarItem", MDBNavbarItem)
  .component("MDBCollapse", MDBCollapse)
  .component("MDBDropdown", MDBDropdown)
  .component("MDBDropdownToggle", MDBDropdownToggle)
  .component("MDBDropdownMenu", MDBDropdownMenu)
  .component("MDBDropdownItem", MDBDropdownItem)
  .component("MDBCol", MDBCol)
  .component("MDBRow", MDBRow)
  .component("MDBInput", MDBInput)
  .component("MDBCheckbox", MDBCheckbox)
  .component("MDBBtn", MDBBtn)
  .mount("#app");