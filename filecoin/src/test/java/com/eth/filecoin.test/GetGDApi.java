package com.eth.filecoin.test;

import com.alibaba.fastjson.JSONObject;
import com.eth.filecoin.mapper.AreaMapper;
import com.eth.filecoin.mapper.CityMapper;
import com.eth.filecoin.mapper.ProvinceMapper;
import com.eth.filecoin.mapper.StreetMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * 获取高德最新省市区
 * https://lbs.amap.com/api/webservice/guide/api/district
 * @author aqi
 * @date 2022/10/31 10:30
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class GetGDApi {

    @Resource
    private AreaMapper areaMapper;

    @Resource
    private ProvinceMapper provinceMapper;

    @Resource
    private CityMapper cityMapper;

    @Resource
    private StreetMapper streetMapper;


    @Test
    public void test(){
//        EntityWrapper<Area> wrapper = new EntityWrapper<>();
//        wrapper.eq("id",768);
//        List<Area> areas = areaMapper.selectList(wrapper);
//        System.out.println(JSONObject.toJSONString(areas));
    }


    public static void main(String[] args) {

//        JSONObject object = getGaoDe("", "杭州");
//        System.out.println(" :" + object);
    }

    /**
     * 根据地区获取该省的所有的市区
     *
     * @param key  高德地图申请的key
     * @param name 省的名字(汉字) 如(上海,北京)
     * @return
     */
    public static JSONObject getGaoDe(String key, String name) {
        if (name == null || "".equals(name) || key == null || "".equals(key)) return null;
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://restapi.amap.com/v3/config/district?subdistrict=1&key=" + key + "&extensions=base&keywords=" + name;
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        String body = forEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(body);
        return jsonObject;
    }


}
