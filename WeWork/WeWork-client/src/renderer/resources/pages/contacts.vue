<template>
    <div class="main">
    <el-card class="mainCard">
    <el-page-header @back="goBack" content="联系人列表" style="margin-bottom:15px"></el-page-header>
    <el-divider></el-divider>

    <el-table height="450px" :data="contacts">
    <el-table-column>
    <template >
    <el-avatar icon="el-icon-user-solid"></el-avatar>
    </template>
    </el-table-column>
    <el-table-column label="姓名">
    <template slot-scope="scope">
    <el-tag type="success" size="mini">{{scope.row.user_name}}</el-tag>
    </template>
    </el-table-column>
    <el-table-column prop="email" label="邮箱"></el-table-column>
    <el-table-column prop="phone" label="手机"></el-table-column>
    <el-table-column>
    <template slot-scope="scope">
    <el-button @click="deleteContact(scope.row)" type="danger" size="small">删除</el-button>
    </template>
    <template slot="header" slot-scope="scope">
    <el-button @click="addContact" type="primary">添加联系人</el-button>
    </template>
    </el-table-column>
    </el-table>
    
    </el-card>

    <el-dialog title="添加联系人" v-if="addContactVisible"  :visible.sync="addContactVisible">
    <el-form label-width="auto">
    <el-form-item label="邮箱" >
       <el-input v-model="searchEmail" autocomplete="off"></el-input>
    </el-form-item>
    </el-form>

    <el-table :data="contact_search">
    <el-table-column label="姓名">
    <template slot-scope="scope">
    <el-tag type="success" size="mini">{{scope.row.user_name}}</el-tag>
    </template>
    </el-table-column>
    <el-table-column prop="email" label="邮箱"></el-table-column>
    <el-table-column prop="phone" label="手机"></el-table-column>
    <el-table-column>

    <template slot-scope="scope">
    <el-button @click="confirmAddContact(scope.row)" size="mini" type="warning">添加</el-button>
    </template>

    </el-table-column>
    </el-table>
    <div slot="footer" class="dialog-footer">
    <el-button @click="searchUser" type="primary">查 询</el-button>
    </div>
    </el-dialog>
    </div>
</template>

<script>
export default {
    created(){
       this.contacts=this.$store.state.contacts
    },
    data(){
        return{
            contacts:[],
            contact_search:[],
            addContactVisible:false,
            searchEmail:''
        }
    },
    methods:{
        goBack(){
            this.$router.push('/userCenter')
        },
        addContact(){
            this.addContactVisible=true
        },
        deleteContact(contact){
            let that=this
            this.contacts.splice(this.contacts.indexOf(contact),1)
            this.$http.post(this.$store.state.contextUrl+'/updateUserContacts',this.$qs.stringify({
                id:that.$store.state.user_id,
                contacts:JSON.stringify(that.contacts)
            })).then(function(res){
                if(res.data=='success'){
                    that.$message({
                        message:'删除成功',
                        type:'error'
                    })
                }
            })
        },
        searchUser(){
            let that=this
            this.$http.post(this.$store.state.contextUrl+'/getUserId',this.$qs.stringify({
                email:that.searchEmail
            })).then(function(res){
                if(res.data!==-1){
                    that.contact_search=[JSON.parse(JSON.stringify(res.data))]            
                }else{
                    that.contact_search=[]
                }
            })
        },
        confirmAddContact(contact){
            if(contact.id==this.$store.state.user_id){
                this.$message({
                    message:'不能添加自己为联系人',
                    type:'error'
                })
                return 
            }

            let flag=false
            this.contacts.forEach(function(item,index){
                if(flag){
                    return 
                }
                if(item.id==contact.id){
                    flag=true
                    return 
                }
            })

            if(flag){
                this.$message({
                    message:'该用户已是联系人',
                    type:'warning'
                })
                return 
            }

            let that=this
            this.contacts.push(contact)
            this.$http.post(this.$store.state.contextUrl+'/updateUserContacts',this.$qs.stringify({
                id:that.$store.state.user_id,
                contacts:JSON.stringify(that.contacts)
            })).then(function(res){
                if(res.data=='success'){
                    that.$message({
                        message:'添加成功',
                        type:'success'
                    })
                }
            })
        }
    },
    watch:{
        "addContactVisible":{
            handler:function(val){
                if(!val){
                    this.contact_search=[]
                }
            }
        }
    }
}
</script>

<style scoped>
.main{
    height:100%;
    font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB",
    "Microsoft YaHei","微软雅黑",Arial,sans-serif;
    overflow-y: hidden;
}

.mainCard{
    height: 100%;
    overflow-y: auto;
}
</style>