<template>
    <el-dialog 
    :visible.sync="visible"
    v-if="visible"
    title="联系人列表">
    
    <el-table :data="contacts">
    <el-table-column label="姓名">
    <template slot-scope="scope">
    <el-tag type="success" size="mini">{{scope.row.user_name}}</el-tag>
    </template>
    </el-table-column>
    <el-table-column prop="email" label="邮箱"></el-table-column>
    <el-table-column prop="phone" label="手机"></el-table-column>
    <el-table-column>

    <template slot-scope="scope">
    <el-button @click="selectContact(scope.row)" size="mini" type="warning">选择</el-button>
    </template>

    </el-table-column>
    </el-table>

    </el-dialog>
</template>

<script>
export default {
    created(){
        this.contacts=this.$store.state.contacts
        this.$emit("update:memInfo",null)
    },
    data(){
        return {
            contacts:[],
            member:null
        }
    },
    methods:{
        selectContact(contact){
            this.member={
                name:contact.user_name,
                email:contact.email,
                phone:contact.phone,
                user_id:contact.id
            }
            this.visible=false
        }
    },
    props:{
        visible:{
            type:Boolean,
            default:false
        },
        memInfo:{
            type:Object,
            default(){
                return {}
            }
        }
    },
    watch:{
        "visible":{
            handler:function(val){
                if(val){
                    this.member=null
                }else{
                    this.$emit("update:memInfo",JSON.parse(JSON.stringify(this.member)))
                }
                this.$emit("update:visible",val)
            }
        }
    }
}
</script>

<style scoped>

</style>