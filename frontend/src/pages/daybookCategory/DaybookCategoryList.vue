<template>
  <q-card>
    <q-card-section class="q-pa-none">
      <q-table
        title="記帳類別"
        :rows="daybookCategories"
        :columns="columns"
        row-key="id"
        virtual-scroll
        :rows-per-page-options="[0]"
      >
      <template v-slot:top>
        <q-btn icon="add" color="blue" flat @click="openDataFormDialog"/>
        <q-input placeholder="搜尋名稱" style="margin-left: 1rem">
          <template v-slot:append>
            <q-icon name="search" />
          </template>
        </q-input>
      </template>
      <template v-slot:body-cell-function="props">
        <q-td :props="props">
          <div>
            <q-btn icon="edit" color="secondary" flat @click="openDataFormDialog(props.row)" />
            <q-btn icon="delete" color="accent" flat @click="openDeleteDialog('警告', '確定要刪除？', props.row)" />
          </div>
        </q-td>
      </template>
      </q-table>
      <q-dialog v-model="dataForm.isEdit">
        <q-card class="q-pa-lg" style="max-width: 500px; width: 100%">
          <h5 class="text-center text-bold q-mb-lg">{{dataForm.isCreate ? '新增' : '編輯'}}</h5>
          <div class="row q-col-gutter-md">
            <div class="col-12">
              <q-input label="類別" stack-label outlined v-model="dataForm.model.category"></q-input>
            </div>
            <div class="col-12">
              <q-input label="名稱" stack-label outlined v-model="dataForm.model.name"></q-input>
            </div>
            <div class="col-6">
              <q-btn v-close-popup unelevated color="primary" class="full-width" label="儲存" @click="saveData"></q-btn>
            </div>
            <div class="col-6">
              <q-btn v-close-popup unelevated color="grey-7" class="full-width" label="取消"></q-btn>
            </div>
          </div>
        </q-card>
      </q-dialog>
    </q-card-section>
  </q-card>
</template>

<script>
import { daybookCategoryService } from "../../service";
import CustomDialog from 'src/components/CustomDialog.vue';

export default {
  name: "daybook-category-list",
  data() {
    return {
      select: null,
      category: "",
      daybookCategories: [],
      currentDaybookCategory: null,
      currentIndex: -1,
      name: "",
      columns: [
        { name: 'category', align: 'left', label: '類別', field: 'category', sortable: true },
        { name: 'name', align: 'left', label: '名稱', field: 'name', sortable: true },
        { name: 'function', align: 'left', label: '功能', field: '', sortable: false }
      ],
      dataForm: {
        data: null,
        model: {
          category: null,
          name: null,
          sortNum: null,
        },
        isEdit: false,
        isCreate: false,
      },
    };
  },
  methods: {
    // 讀取記帳類別資料
    getDaybookCategories() {
      daybookCategoryService.getAll(this.category, this.name) // 呼叫API進行取得資料動作
        .then(response => {
          const table = response.data;
          this.daybookCategories = table;
        })
        .catch(e => {
          console.log(e);
        });
    },
    // 開啟新增、修改視窗
    openDataFormDialog (row) {
      if (row.target !== undefined && row.target.innerText === 'add') {
        this.dataForm.data = {}
        this.dataForm.model = {}
        this.dataForm.isCreate = true
        this.dataForm.isEdit = true
      }
      else {
        this.dataForm.isEdit = true
        for (let field in row) {
          this.dataForm.model[field] = row[field]
        }
        this.dataForm.data = row
      }
    },
    // 新增、修改的儲存動作
    saveData () {
      // 將目前的資料內容存進form的data中，後續將this.dataForm.data傳至後端
      for (let field in this.dataForm.model) {
        this.dataForm.data[field] = this.dataForm.model[field]
      }
      // 新增
      if (this.dataForm.isCreate) {
        daybookCategoryService.create(this.dataForm.data) // 呼叫API進行新增動作
        .then(() => {
          this.getDaybookCategories() // 新增後重新同步畫面上的資料
          this.dataForm.isCreate = false // 還原isCreate旗標
        })
      }
      // 修改
      else {
        daybookCategoryService.update(this.dataForm.data.id ,this.dataForm.data) // 呼叫API進行修改動作
      }
    },
    // 開啟刪除視窗
    openDeleteDialog (title, text, row) {
      // 設定dialog元件參數
      this.$q.dialog({
        component: CustomDialog,
        componentProps: {
          title: title,
          text: text
        }
      }).onOk(() => {
        daybookCategoryService.delete(row.id) // 呼叫API進行刪除動作
        .then(() => {
          this.getDaybookCategories() // 刪除後重新同步畫面上的資料
        })
      }).onCancel(() => {
        // 取消事件
      }).onDismiss(() => {
        // finally
      })
    }
  },
  mounted() {
    this.getDaybookCategories(); // 載入記帳類別資料
  }
};
</script>

<style scoped>

</style>
