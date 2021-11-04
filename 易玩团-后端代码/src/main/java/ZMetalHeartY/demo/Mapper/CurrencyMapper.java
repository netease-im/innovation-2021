package ZMetalHeartY.demo.Mapper;


import ZMetalHeartY.demo.Entity.Kilometers;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface CurrencyMapper {

    @Select("SELECT * FROM kilometers where name = #{name}")
    Kilometers getKilometersByName(String name);

    @Select("SELECT * FROM kilometers where id = #{id}")
    Kilometers getKilometersByID(Integer id);

    @Select("SELECT * FROM kilometers")
    List<Kilometers> getAllKilometers();

    @Update("UPDATE kilometers SET kilometers = #{kilometers} WHERE id = #{id};")
    int editKilometers(Kilometers kilometers);

    @Select("SELECT count(*) FROM kilometers")
    int kilometersCount();



}
