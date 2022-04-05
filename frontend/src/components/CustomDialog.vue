<template>
  <q-dialog ref="dialog" @hide="onDialogHide">
    <q-card style="max-width: 500px; width: 100%;">
      <div class="text-h4 text-bold text-center text-white bg-red-7 q-pa-md">{{title}}</div>
      <q-card-section class="q-pt-lg">
        <p>{{text}}</p>
        <p v-show="isWarningExist" style="color:red"><b>{{warning}}</b></p>
      </q-card-section>
      <div class="row q-pa-md q-col-gutter-sm">
        <div class="col-6">
          <q-btn unelevated class="full-width" color="red-7" label="確定" @click="onOKClick" />
        </div>
        <div class="col-6">
          <q-btn unelevated class="full-width" color="grey-8" label="取消" @click="onCancelClick"/>
        </div>
      </div>
    </q-card>
  </q-dialog>
</template>

<script>
export default {
  props: ['title', 'text', 'warning'],
  emits: [
    // REQUIRED
    'ok', 'hide'
  ],
  methods: {
    // following method is REQUIRED
    // (don't change its name --> "show")
    show () {
      this.$refs.dialog.show()
    },

    // following method is REQUIRED
    // (don't change its name --> "hide")
    hide () {
      this.$refs.dialog.hide()
    },

    onDialogHide () {
      // required to be emitted
      // when QDialog emits "hide" event
      this.$emit('hide')
    },

    onOKClick () {
      // on OK, it is REQUIRED to
      // emit "ok" event (with optional payload)
      // before hiding the QDialog
      this.$emit('ok')
      // or with payload: this.$emit('ok', { ... })
      // hide dialog
      this.hide()
    },

    onCancelClick () {
      // hide dialog
      this.hide()
    }
  },
  computed: {
    isWarningExist: function () {
      return !!this.warning
    }
  }
}
</script>