import { app, BrowserWindow ,ipcMain,shell,Menu,Tray } from 'electron'
const fs = require('fs')
const os = require('os')
const path = require('path')
import '../renderer/store'

/**
 * Set `__static` path to static files in production
 * https://simulatedgreg.gitbooks.io/electron-vue/content/en/using-static-assets.html
 */
if (process.env.NODE_ENV !== 'development') {
  global.__static = require('path').join(__dirname, '/static').replace(/\\/g, '\\\\')
}


let mainWindow
let todayWorkList
let calendar
let clock
let noteBook
let pdfWin
let conference
let tray
let enter

let login=false
let hide=false
let todayWorkListVisible=false
let calendarVisible=false
let clockVisible=false
let noteBookVisible=false

let closeAll=false

const winURL = process.env.NODE_ENV === 'development'
  ? `http://localhost:9080`
  : `file://${__dirname}/index.html`




function loading() {
  const electron = require('electron')
  /*获取electron窗体的菜单栏*/
  const Menu = electron.Menu
  /*隐藏electron创听的菜单栏*/
  Menu.setApplicationMenu(null)

    //登入界面
    enter=new BrowserWindow({
      height:450,
      width:750,
      useContentSize: true,
      frame:false,
      show:false,
      resizable:false,
      webPreferences: {
        nodeIntegration: true,
        webSecurity: false
     }
  })
  
    enter.loadURL(`file://${__static}/loading.html`)
    enter.on('ready-to-show',()=>{
      enter.show()
    })
    enter.webContents.closeDevTools()
}

function createWindow () {

  const electron = require('electron')
  /*获取electron窗体的菜单栏*/
  const Menu = electron.Menu
  /*隐藏electron创听的菜单栏*/
  Menu.setApplicationMenu(null)


  /**
   * Initial window options
   */

  //主窗口
  mainWindow = new BrowserWindow({
    height: 700,
    useContentSize: true,
    width: 1000,
    frame:false,
    show:false,
    backgroundColor: '#00000000',
    webPreferences: {
      nodeIntegration: true,
      webSecurity: false
   }
  })

  mainWindow.webContents.closeDevTools()
  mainWindow.loadURL(winURL+'#/workbench')
  mainWindow.setMinimumSize(950,665)
  mainWindow.setMaximumSize(1600,1120)

  mainWindow.on('close',(e)=>{
    if(!closeAll){
      e.preventDefault()
      mainWindow.hide()
    }
  })

  mainWindow.on('closed', () => {
    mainWindow = null
  })

  mainWindow.on('hide',()=>{
    hide=true
  })



  mainWindow.on('show',()=>{
    hide=false
  })
  
  mainWindow.on('ready-to-show',()=>{
    mainWindow.show()
    enter.close()
  })




  //组件一：今日任务列表
  todayWorkList=new BrowserWindow({
    height: 400,
    useContentSize: true,
    width: 500,
    frame:false,
    show:false,
    backgroundColor: '#00000000',
    resizable:true,
    webPreferences: {
      nodeIntegration: true,
      webSecurity: false
    }
  })
  todayWorkList.webContents.closeDevTools()
  todayWorkList.loadURL(winURL+'#/todayWorkComponent')

  todayWorkList.setMinimumSize(500,300)
  todayWorkList.setMaximumSize(500,800)
  




  todayWorkList.on('hide',()=>{
    todayWorkListVisible=false
  })

  todayWorkList.on('show',()=>{
    todayWorkListVisible=true
  })

  todayWorkList.on('close',(e)=>{
    if(!closeAll){
      e.preventDefault()
      todayWorkList.hide()
    }
  })

   //组件二：计划日历
   calendar=new BrowserWindow({
    height: 800,
    useContentSize: true,
    width: 900,
    frame:false,
    show:false,
    backgroundColor: '#00000000',
    resizable:true,
    webPreferences: {
      nodeIntegration: true,
      webSecurity: false
    }
  })
  calendar.webContents.closeDevTools()
  calendar.loadURL(winURL+'#/calendarComponent')

  calendar.setMinimumSize(900,750)
  calendar.setMaximumSize(900,1000)
  
 

  calendar.on('hide',()=>{
    calendarVisible=false
  })

  calendar.on('show',()=>{
    calendarVisible=true
  })

  calendar.on('close',(e)=>{
    if(!closeAll){
      e.preventDefault()
      calendar.hide()
    }
  })

  //组件三：任务时钟
  clock=new BrowserWindow({
    height: 199,
    useContentSize: true,
    width: 750,
    frame:false,
    show:false,
    backgroundColor: '#00000000',
    resizable:false,
    webPreferences: {
      nodeIntegration: true,
      webSecurity: false
    }
  })
  clock.webContents.closeDevTools()
  clock.loadURL(winURL+'#/clockComponent')

  


  clock.on('hide',()=>{
    clockVisible=false
  })

  clock.on('show',()=>{
    clockVisible=true
  })

  clock.on('close',(e)=>{
    if(!closeAll){
      e.preventDefault()
      clock.hide()
    }
  })

  //组件四：任务小记
  noteBook=new BrowserWindow({
    useContentSize: true,
    width: 920,
    height: 550,
    minWidth: 920,
    minHeight: 400,
    maxHeight:600,
    maxWidth:920,
    frame:false,
    show:false,
    maximizable: false,
    webPreferences: {
      nodeIntegration: true,
      webSecurity: false
    }
  })
  noteBook.webContents.closeDevTools()
  noteBook.loadURL(winURL+'#/noteBookComponent')


  


  noteBook.on('hide',()=>{
    noteBookVisible=false
  })

  noteBook.on('show',()=>{
    noteBookVisible=true
  })

  noteBook.on('close',(e)=>{
    if(!closeAll){
      e.preventDefault()
      noteBook.hide()
    }
  })


}




app.on('ready',  () => {
  loading()
  setTimeout(() => {
  createWindow() //解决Linux下背景无法透明问题
  
  tray = new Tray(path.join(__static,'./main.png'))
  const contextMenu = Menu.buildFromTemplate([
    {
      label:'主程序',
      click:function () {
       if(hide){
         mainWindow.show()
       }
      }
    },
    {
      label:'桌面组件：今日任务',
      click:function () {
        if(!todayWorkListVisible){
          todayWorkList.show()
          todayWorkList.webContents.send('changeDb',login?'./data/main/cloud.db':'./data/main/local.db')
        }else{
          todayWorkList.hide()
        }
      }
    },
    {
      label:'桌面组件：计划日历',
      click:function () {
        if(!calendarVisible){
          calendar.show()
          calendar.webContents.send('changeDb',login?'./data/main/cloud.db':'./data/main/local.db')
        }else{
          calendar.hide()
        }
      }
    },
    {
      label:'桌面组件：任务时钟',
      click:function () {
        if(!clockVisible){
          clock.show()
        }else{
          clock.hide()
        }
      }
    },
    {
      label:'桌面组件：任务小记',
      click:function () {
        if(!noteBookVisible){
          noteBook.show()
        }else{
          noteBook.hide()
        }
      }
    },
    { 
      label: '退出', 
      click:function () {
         closeAll=true
         app.quit()
      },
    },
])
  tray.setToolTip('MyWork——计划执行与管理平台')
  tray.setContextMenu(contextMenu)

  tray.on('click', () => {
    if(hide){
      mainWindow.show()
    }else{
      mainWindow.hide()
    }
  });

},100)

})  

app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') {
    app.quit()
  }
})

app.on('activate', () => {
  if (mainWindow === null) {
    createWindow()
  }
})



//主进程与渲染进程之间的通信

ipcMain.on('login',function (event) {
  login=true
  todayWorkList.webContents.send('changeDb',login?'./data/main/cloud.db':'./data/main/local.db')
  calendar.webContents.send('changeDb',login?'./data/main/cloud.db':'./data/main/local.db')
})

ipcMain.on('logOut',function (event) {
  login=false
  todayWorkList.webContents.send('changeDb',login?'./data/main/cloud.db':'./data/main/local.db')
  calendar.webContents.send('changeDb',login?'./data/main/cloud.db':'./data/main/local.db')
})




ipcMain.on('print-to-pdf', function (event) {
  const pdfPath = 'print.pdf'
  const win = BrowserWindow.fromWebContents(event.sender)
  // 使用默认打印选项
  win.webContents.printToPDF({
    pageSize:'A4',
    printBackground: true
  }, function (error, data) {
    if (error) throw error
    fs.writeFile(pdfPath, data, function (error) {
      if (error) {
        throw error
      }
      shell.openExternal(pdfPath)
    })
  })
  event.sender.send('reply', 'success')
})

ipcMain.on('createPDF',function(event,plan){
 pdfWin=new BrowserWindow({
    title:'计划清单',
    height: 700,
    useContentSize: true,
    width: 1000,
    webPreferences: {
      nodeIntegration: true,
   }
  })
  pdfWin.webContents.closeDevTools()
  pdfWin.on('closed', () => {
    pdfWin= null
  })
  Menu.setApplicationMenu(null)
  pdfWin.loadURL(winURL+'#/pdf')
  pdfWin.webContents.on('did-finish-load',function(){
    pdfWin.webContents.send('plan', plan)
  })
})

ipcMain.on('createConference',function(event,plan,id){
  conference = new BrowserWindow({
    title:'视频测试',
    height: 701,
    minHeight:701,
    resizable:true,
    frame:true,
    useContentSize: true,
    width: 1000,
    minWidth:1000,
    webPreferences: {
      nodeIntegration: true,
    }
  })
   conference.webContents.closeDevTools()
   conference.on('closed', () => {
    conference= null
   })
  //  conference.setMinimumSize(1000,701)
   Menu.setApplicationMenu(null)
   conference.loadURL(winURL+'#/conference')
   conference.webContents.on('did-finish-load',function(){
    conference.webContents.send('plan', plan,id)
  })
 })

ipcMain.on('openMainAndShowTodayPro',function (e) {
  if(hide){
    mainWindow.show()
  }
})



