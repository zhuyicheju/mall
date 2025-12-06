package com.example.mall.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.mall.controller.dto.GoodDetailDTO;
import com.example.mall.controller.dto.GoodInsertDTO;
import com.example.mall.controller.dto.GoodSimpleDTO;

@Mapper
public interface GoodsMapper {
    public List<GoodSimpleDTO> getGoodsWithPage(int offset, int pagesize);
    public GoodDetailDTO getGoodById(Long id);
    public int updateStock(Long id, int stock);
    public int insertGood(GoodInsertDTO goodInsertDTO);
    public Long goodCount();
}
