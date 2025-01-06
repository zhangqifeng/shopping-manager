package com.example.service;

import com.example.entity.Goods;
import com.example.mapper.GoodsMapper;

import javax.annotation.Resource;
import java.util.List;

public class GoodsService {
    @Resource
    private GoodsMapper goodsMapper;

    public List<Goods> selectByBusinessId(Integer id) {
        return goodsMapper.selectByBusinessId(id);
    }
}
