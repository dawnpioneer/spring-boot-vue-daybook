<template>
  <q-card>
    <q-card-section class="q-pa-none">
      <div class="title">記帳本</div>
      <div class="row tool-bar">
        <q-btn icon="add" color="blue" flat @click="openDataFormDialog"/>
        <q-select outlined 
          v-model="searchModel.year" 
          :options="daybookYearList"
          label="年" 
          @update:model-value="searchByYear"
        />
        <q-select outlined 
          v-model="searchModel.month" 
          :options="monthList"
          label="月" 
          @update:model-value="search"
        />
        <q-select outlined 
          v-model="searchModel.category" 
          :options="searchOptions"
          label="收支" 
          @update:model-value="search"
        />
        <q-select outlined 
          v-model="searchModel.daybookCategory" 
          :options="daybookCategories"
          label="類別" 
          @update:model-value="search"
        />
        <q-input 
          v-model="searchModel.title" 
          label="搜尋名稱"
          @update:model-value="search"
        >
          <template v-slot:append>
            <q-icon name="search" />
          </template>
        </q-input>
      </div>
      <q-table
        :rows="daybooks"
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
import { daybookService } from "../../service"
import { daybookCategoryService } from "../../service"
import CustomDialog from "src/components/CustomDialog.vue"
import {date} from "quasar"

export default {
  name: "daybook-list",
  data() {
    return {
      select: null,
      category: "",
      originDaybooks: [],
      daybooks: [],
      name: "",
      columns: [
        { name: 'category', align: 'left', label: '收支', field: val => val.daybookCategory.category, sortable: true },
        { name: 'name', align: 'left', label: '類別', field: val => val.daybookCategory.name, sortable: true },
        { name: 'title', align: 'left', label: '名稱', field: 'title', sortable: true },
        { name: 'amount', align: 'left', label: '金額', field: 'amount', sortable: true },
        { name: 'recordDate', align: 'left', label: '記帳日期', field: 'recordDate', sortable: true, format: val => date.formatDate(val, 'YYYY-MM-DD') },
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
        year: new Date().getFullYear(),
        month: new Date().getMonth() + 1, // Because getmonth() start from 0.
        category: '全部',
        daybookCategory: {id: 0, name: '全部', label: '全部'},
        title: null
      },
      searchOptions: ['全部','支出', '收入'],
      daybookYearList: null,
      monthList: Array.from({ length:13 }, (v, k) => k == 0 ? '全部' : k),
      originDaybookCategories: [],
      daybookCategories: [],
      existDataYearList: [],
    }
  },
  methods: {
    // 讀取記帳類別資料
    getDaybooks() {
      this.existDataYearList.push(this.searchModel.year)
      daybookService.getAll(this.searchModel.year) // 呼叫API進行取得資料動作
        .then(response => {
          const table = response.data
          this.originDaybooks = table
          this.daybooks = table
          this.search()
        })
        .catch(e => {
          console.log(e)
        })
      // 取得記帳本的年列表  
      daybookService.getDaybookYearList() // 呼叫API進行取得資料動作
        .then(response => {
          this.daybookYearList = response.data
          this.daybookYearList.unshift('全部')
        })
        .catch(e => {
          console.log(e)
        })
      // 取得記帳類別
      daybookCategoryService.getAll() // 呼叫API進行取得資料動作
        .then(response => {
          this.originDaybookCategories = response.data
          this.originDaybookCategories.forEach( function ( item, index, array)  {
            item.label = item.name;
          });
          this.originDaybookCategories.unshift({id: 0, name: '全部', label: '全部'})
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
        daybookService.create(this.dataForm.data) // 呼叫API進行新增動作
        .then(() => {
          this.getDaybooks() // 新增後重新同步畫面上的資料
        })
      }
      // 修改
      else {
        daybookService.update(this.dataForm.data.id ,this.dataForm.data) // 呼叫API進行修改動作
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
        daybookService.delete(row.id) // 呼叫API進行刪除動作
        .then(() => {
          this.getDaybooks() // 刪除後重新同步畫面上的資料
          this.$q.notify({ message: '刪除成功', color: 'negative', position: 'bottom-right' , timeout: 1000 })
        })
      }).onCancel(() => {
        // 取消事件
      }).onDismiss(() => {
        // finally
      })
    },
    // 以年搜尋(為避免資料量太大，取得記帳本資料時僅預設取得當年度資料，欲取得其他年度資料時才再透過API取得)
    searchByYear () {
      // 於已存在資料中搜尋，則直接呼叫搜尋方法
      if (this.existDataYearList.indexOf(this.searchModel.year) >= 0) {
        this.search()
      }
      // 若搜尋資料非已存在年份資料，則需透過API取得
      else {
        let searchAllYears = this.searchModel.year === '全部'
        let searchYear = searchAllYears ? '' : this.searchModel.year
        daybookService.getAll(searchYear) // 呼叫API進行取得資料動作
        .then(response => {
          const table = response.data
          this.originDaybooks = this.originDaybooks.concat(table)
          this.daybooks = this.daybooks.concat(table)
          if (searchAllYears) {
            this.existDataYearList = this.daybookYearList
          }
          else {
            this.existDataYearList.push(this.searchModel.year)
          }
          this.search()
        })
        .catch(e => {
          console.log(e)
        })
      }
    },
    // 搜尋
    search () {
      if (this.searchModel.category === '全部') { // 收支為全部，則不須選擇類別，類別固定為全部
        this.daybookCategories = this.originDaybookCategories.filter(item => { return item.label === '全部' })
      }
      else {
        this.daybookCategories = this.originDaybookCategories.filter(
          item => 
          { 
            return item.category === this.searchModel.category || this.searchModel.category === '全部' || item.label === '全部'
          }
        )
      }
      this.daybooks = this.originDaybooks.filter(
        item =>
        {
          return ((this.searchModel.year === new Date(item.recordDate).getFullYear()) || this.searchModel.year === '全部') && // 篩選年
            // 篩選月
            ((this.searchModel.month === new Date(item.recordDate).getMonth() + 1) || this.searchModel.month === '全部') &&
            // 篩選類別
            (((this.daybookCategories.map((obj) => obj.id).indexOf(item.daybookCategory.id) >= 0) && this.searchModel.daybookCategory.name === '全部')
            || item.daybookCategory.id === this.searchModel.daybookCategory.id
            || this.searchModel.category === '全部') &&
            // 篩選名稱
            (!!this.searchModel.title && item.title.indexOf(this.searchModel.title) >= 0 || !!!this.searchModel.title)
            ;
        }
      )
    },
    formatDate(dateString) {
      const date = new Date(dateString);
      return new Intl.DateTimeFormat('default', {dateStyle: 'long'}).format(date);
    }
  },
  mounted () {
    this.getDaybooks() // 載入記帳類別資料
  }
}
</script>

<style scoped>

</style>
