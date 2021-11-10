<template>
  <div id="app">
    <ul id="dropdown-profile" class="dropdown-content">
        <li><a href="/profile">個人資料</a></li>
        <li><a href="#" @click.prevent="logOut">登出</a></li>
    </ul>
    <nav class="white" role="navigation">
        <div class="nav-wrapper">
            <a id="logo-container" href="/" class="brand-logo">Daybook</a>
            <a href="#" data-target="nav-mobile" class="sidenav-trigger" style="color:black;"><i class="material-icons">menu</i></a>
            <ul class="right hide-on-med-and-down">
                <li><a href="/news"><i class="fa fa-bullhorn" aria-hidden="true"></i> 更新歷程</a></li>
                <li v-if="currentUser"><a href="/daybook"><i class="fa fa-sticky-note" aria-hidden="true"></i> 記帳本</a></li>
                <li v-if="currentUser"><a href="/daybookCategory"><i class="fa fa-list-alt" aria-hidden="true"></i> 記帳類別</a></li>
                <li v-if="currentUser"><a href="/chart"><i class="fa fa-chart-bar" aria-hidden="true"></i> 統計圖</a></li>
                <li v-if="currentUser"><a href="/user"><i class="fa fa-chart-bar" aria-hidden="true"></i> User</a></li>
                <li v-if="showModeratorBoard"><a href="/mod"><i class="fa fa-chart-bar" aria-hidden="true"></i> Mod</a></li>
                <li v-if="showAdminBoard"><a href="/admin"><i class="fa fa-chart-bar" aria-hidden="true"></i> Admin</a></li>
                <li v-if="!currentUser"><a href="/register"><i class="fas fa-user-plus"></i> 註冊</a></li>
                <li v-if="!currentUser"><a href="/login"><i class="fas fa-sign-in-alt"></i> 登入</a></li>
                <li v-if="currentUser">
                    <a class="dropdown-trigger" href="#" data-target="dropdown-profile">
                        <i class="fa fa-user" aria-hidden="true"></i> {{ currentUser.username }}
                        <i class="material-icons right" style="margin-left:0px">arrow_drop_down</i>
                    </a>
                </li>
            </ul>
        </div>
    </nav>
          
    <!-- Mobile Menu -->
    <ul id="nav-mobile" class="sidenav">
        <li><a href="/news"><i class="fa fa-bullhorn" aria-hidden="true"></i> 更新歷程</a></li>
        <li v-if="currentUser"><a href="/daybook"><i class="fa fa-sticky-note" aria-hidden="true"></i> 記帳本</a></li>
        <li v-if="currentUser"><a href="/daybookCategory"><i class="fa fa-list-alt" aria-hidden="true"></i> 記帳類別</a></li>
        <li v-if="currentUser"><a href="/chart"><i class="fa fa-chart-bar" aria-hidden="true"></i> 統計圖</a></li>
        <li v-if="currentUser"><a href="#" @click.prevent="logOut">登出</a></li>
    </ul>

    <div class="container">
      <router-view />
    </div>

  </div>
</template>

<script>
import { ref } from 'vue';
import M from 'materialize-css';

export default {
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
    showAdminBoard() {
      if (this.currentUser && this.currentUser['roles']) {
        return this.currentUser['roles'].includes('ROLE_ADMIN');
      }

      return false;
    },
    showModeratorBoard() {
      if (this.currentUser && this.currentUser['roles']) {
        return this.currentUser['roles'].includes('ROLE_MODERATOR');
      }

      return false;
    }
  },
  methods: {
    logOut() {
      this.$store.dispatch('auth/logout');
      this.$router.push('/login');
    }
  },
  setup() {
    const collapse = ref(false);
    const dropdown = ref(false);

    return {
      collapse,
      dropdown
    }
  },
  mounted () {
    M.AutoInit();
  },
};
</script>