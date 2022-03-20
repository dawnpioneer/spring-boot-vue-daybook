<template>
  <q-card>
    <q-card-section class="q-pa-none">
      <div class="title">記帳類別</div>
      <div class="row tool-bar">
        <q-btn icon="add" color="blue" flat @click="openDataFormDialog"/>
        <q-select outlined 
          v-model="searchModel.category" 
          :options="searchOptions"
          label="類別" 
          @update:model-value="search"
        />
        <q-input 
          v-model="searchModel.name" 
          label="搜尋名稱"
          @update:model-value="search"
        >
          <template v-slot:append>
            <q-icon name="search" />
          </template>
        </q-input>
      </div>
      <q-table
        :rows="daybookCategories"
        :columns="columns"
        row-key="id"
        virtual-scroll
        :rows-per-page-options="[0]"
      >
        <template v-slot:body-cell-function="props">
          <q-td :props="props">
            <div>
              <q-btn icon="edit" color="secondary" flat @click="openDataFormDialog(props.row)" />
              <q-btn icon="delete" color="accent" flat @click="openDeleteDialog('警告', '確定要刪除？', props.row)" />
            </div>
          </q-td>
        </template>
      </q-table>
      <q-dialog v-model="dataForm.isOpenDataFormDialog">
        <q-card class="q-pa-lg" style="max-width: 500px width: 100%">
          <q-form
            @submit="saveData"
            class="q-gutter-md"
          >
          <h5 class="text-center text-bold q-mb-lg">{{dataForm.isCreate ? '新增' : '編輯'}}</h5>
          <div class="row q-col-gutter-md">
            <div class="col-12">
              <q-select outlined v-model="dataForm.model.category" :options="options" label="類別" />
            </div>
            <div class="col-12">
              <q-input outlined label="名稱" v-model="dataForm.model.name"
                lazy-rules
                :rules="[ val => val && val.length > 0 || '請輸入名稱']"
              />
            </div>
            <div class="col-6">
              <q-btn unelevated color="primary" class="full-width" label="儲存"  type="submit"></q-btn>
            </div>
            <div class="col-6">
              <q-btn v-close-popup unelevated color="grey-7" class="full-width" label="取消"></q-btn>
            </div>
          </div>
           </q-form>
        </q-card>
      </q-dialog>
    </q-card-section>
  </q-card>
</template>

<script>
import { daybookCategoryService } from "../../service"
import CustomDialog from 'src/components/CustomDialog.vue'

export default {
  name: "daybook-category-list",
  data() {
    return {
      select: null,
      category: "",
      originDaybookCategories: [],
      daybookCategories: [],
      name: "",
      columns: [
        { name: 'category', align: 'left', label: '收支', field: 'category', sortable: true },
        { name: 'name', align: 'left', label: '名稱', field: 'name', sortable: true },
        { name: 'function', align: 'left', label: '功能', field: '', sortable: false }
      ],
      dataForm: {
        data: null,
        model: {
          category: null,
          name: null,
        },
        isOpenDataFormDialog: false, // 是否為編輯
        isCreate: false, // 是否為新增
      },
      options: ['支出', '收入'],
      searchModel: {
        category: '全部',
        name: null
      },
      searchOptions: ['全部','支出', '收入']
    }
  },
  methods: {
    // 讀取記帳類別資料
    getDaybookCategories () {
      daybookCategoryService.getAll() // 呼叫API進行取得資料動作
        .then(response => {
          const table = response.data
          this.originDaybookCategories = table
          this.daybookCategories = table
        })
        .catch(e => {
          console.log(e)
        })
    },
    // 開啟新增、修改視窗
    openDataFormDialog (row) {
      this.dataForm.isOpenDataFormDialog = true
      // 新增
      if (row.target !== undefined && row.target.innerText === 'add') {
        this.dataForm.data = {}
        this.dataForm.model = {}
        this.dataForm.model.category = '支出'
        this.dataForm.isCreate = true
      }
      // 修改
      else {
        for (let field in row) {
          this.dataForm.model[field] = row[field]
        }
        this.dataForm.data = row
        this.dataForm.isCreate = false
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
        this.dataForm.isCreate = false // 還原isCreate旗標
        daybookCategoryService.create(this.dataForm.data) // 呼叫API進行新增動作
        .then(() => {
          this.getDaybookCategories() // 新增後重新同步畫面上的資料
        })
      }
      // 修改
      else {
        daybookCategoryService.update(this.dataForm.data.id ,this.dataForm.data) // 呼叫API進行修改動作
      }
      this.$q.notify({ message: '儲存成功', color: 'positive', position: 'bottom-right' , timeout: 1000 })
      this.dataForm.isOpenDataFormDialog = false
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
          this.$q.notify({ message: '刪除成功', color: 'negative', position: 'bottom-right' , timeout: 1000 })
        })
      }).onCancel(() => {
        // 取消事件
      }).onDismiss(() => {
        // finally
      })
    },
    search () {
      let isCategoryFilterred = '全部' !== this.searchModel.category
      let isNameFilterred = this.searchModel.name !== null && this.searchModel.name.trim() !== ''
      if (!(isCategoryFilterred || isNameFilterred)) {
        this.daybookCategories = this.originDaybookCategories
      }
      else {
        this.daybookCategories = this.originDaybookCategories.filter(
          item => {
            let isCategoryMatched = true
            let isNameMatched = true
            if (isCategoryFilterred) {
              isCategoryMatched = item.category === this.searchModel.category
            }
            if (isNameFilterred) {
              isNameMatched = item.name.indexOf(this.searchModel.name) > -1
            }
            return isCategoryMatched && isNameMatched
          }
        )
      }
    }
  },
  mounted () {
    this.getDaybookCategories() // 載入記帳類別資料
  }
}
</script>

<style scoped>

</style>
