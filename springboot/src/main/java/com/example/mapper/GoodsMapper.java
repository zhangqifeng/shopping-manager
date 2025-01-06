package com.example.mapper;

import com.example.entity.Goods;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface GoodsMapper {

    @Select("select * from goods where business_id = #{id}")
    List<Goods> selectByBusinessId(Integer id);
}
