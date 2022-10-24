// pages/home_page/home_page.js
const app = getApp()
const {auth} = require('../../utils/auth')
const {toMePage, toHomePage, toActivityPage} = require('../../utils/nav')

Page({
	toMePage,
	toHomePage,
	toActivityPage,
  data: {
    my_task_number:4,/**显示任务栏条件 */
    my_task:[{/*当前任务框内显示的*/
      "name" : "制作数据库",
      "group_id": "115222",}, 
      {"name" :"制作ppt",
       "group_id" : "352222",},
      {"name" :"开发前端",
       "group_id" : "1555445",},
      {"name" :"摆烂",
       "group_id" : "441445",}]/**通过group——id（group创建时生成）建立连接 */
  },

  ToGroupDetail(e){
    var task = e.currentTarget.dataset.task;
    //console.log(task.group_id);
    /**（未做）跳转到group_id对应group_detail界面 */
  },
})