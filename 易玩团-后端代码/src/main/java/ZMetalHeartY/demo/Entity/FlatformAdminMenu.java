package ZMetalHeartY.demo.Entity;


import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FlatformAdminMenu {
   private Integer au_id;

   private String au_url;

   private String au_title;

   private String au_info;

   private Integer au_level;

   private Integer au_clickable;

   private Integer au_p_id;

   private Integer au_sort;

   private Integer au_state;

   private String au_class;

   private Integer au_type;

   private Integer u_id;

   private Integer checked;

   private String create_time;

   private Integer delete_status;

   private String delete_time;

   private List<FlatformAdminMenu> sub_list;

   public FlatformAdminMenu(){

   }


    public Integer getAu_id() {
        return au_id;
    }

    public void setAu_id(Integer au_id) {
        this.au_id = au_id;
    }

    public String getAu_url() {
        return au_url;
    }

    public void setAu_url(String au_url) {
        this.au_url = au_url;
    }

    public String getAu_title() {
        return au_title;
    }

    public void setAu_title(String au_title) {
        this.au_title = au_title;
    }

    public String getAu_info() {
        return au_info;
    }

    public void setAu_info(String au_info) {
        this.au_info = au_info;
    }

    public Integer getAu_level() {
        return au_level;
    }

    public void setAu_level(Integer au_level) {
        this.au_level = au_level;
    }

    public Integer getAu_clickable() {
        return au_clickable;
    }

    public void setAu_clickable(Integer au_clickable) {
        this.au_clickable = au_clickable;
    }

    public Integer getAu_p_id() {
        return au_p_id;
    }

    public void setAu_p_id(Integer au_p_id) {
        this.au_p_id = au_p_id;
    }

    public Integer getAu_sort() {
        return au_sort;
    }

    public void setAu_sort(Integer au_sort) {
        this.au_sort = au_sort;
    }

    public Integer getAu_state() {
        return au_state;
    }

    public void setAu_state(Integer au_state) {
        this.au_state = au_state;
    }

    public String getAu_class() {
        return au_class;
    }

    public void setAu_class(String au_class) {
        this.au_class = au_class;
    }

    public Integer getAu_type() {
        return au_type;
    }

    public void setAu_type(Integer au_type) {
        this.au_type = au_type;
    }

    public Integer getU_id() {
        return u_id;
    }

    public void setU_id(Integer u_id) {
        this.u_id = u_id;
    }

    public Integer getChecked() {
        return checked;
    }

    public void setChecked(Integer checked) {
        this.checked = checked;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public Integer getDelete_status() {
        return delete_status;
    }

    public void setDelete_status(Integer delete_status) {
        this.delete_status = delete_status;
    }

    public String getDelete_time() {
        return delete_time;
    }

    public void setDelete_time(String delete_time) {
        this.delete_time = delete_time;
    }

    public List<FlatformAdminMenu> getSub_list() {
        return sub_list;
    }

    public void setSub_list(List<FlatformAdminMenu> sub_list) {
        this.sub_list = sub_list;
    }
}