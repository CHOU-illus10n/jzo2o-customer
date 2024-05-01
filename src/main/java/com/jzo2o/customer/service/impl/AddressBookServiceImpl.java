package com.jzo2o.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.api.customer.dto.response.AddressBookResDTO;
import com.jzo2o.api.publics.MapApi;
import com.jzo2o.api.publics.dto.response.LocationResDTO;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.common.utils.BeanUtils;
import com.jzo2o.common.utils.CollUtils;
import com.jzo2o.common.utils.NumberUtils;
import com.jzo2o.common.utils.StringUtils;
import com.jzo2o.customer.mapper.AddressBookMapper;
import com.jzo2o.customer.model.domain.AddressBook;
import com.jzo2o.customer.model.dto.request.AddressBookPageQueryReqDTO;
import com.jzo2o.customer.model.dto.request.AddressBookUpsertReqDTO;
import com.jzo2o.customer.service.IAddressBookService;
import com.jzo2o.mvc.utils.UserContext;
import com.jzo2o.mysql.utils.PageUtils;
import org.apache.tomcat.jni.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 地址薄 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2023-07-06
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements IAddressBookService {

    @Autowired
    MapApi mapApi;

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Override
    public List<AddressBookResDTO> getByUserIdAndCity(Long userId, String city) {

        List<AddressBook> addressBooks = lambdaQuery()
                .eq(AddressBook::getUserId, userId)
                .eq(AddressBook::getCity, city)
                .list();
        if(CollUtils.isEmpty(addressBooks)) {
            return new ArrayList<>();
        }
        return BeanUtils.copyToList(addressBooks, AddressBookResDTO.class);
    }

    @Override
    public void addAddress(AddressBookUpsertReqDTO addressBookUpsertReqDTO) {
        AddressBook addressBook = BeanUtil.copyProperties(addressBookUpsertReqDTO, AddressBook.class);
        Long userId = UserContext.currentUserId();
        addressBook.setUserId(userId);
        // 如果是默认地址，则将其他地址设置为非默认
        if(addressBookUpsertReqDTO.getIsDefault() == 1){
            cancelDefault(userId);
        }
        // 组装详细地址 address
        String completeAddress = addressBookUpsertReqDTO.getProvince() + addressBookUpsertReqDTO.getCity() + addressBookUpsertReqDTO.getCounty() + addressBookUpsertReqDTO.getAddress();

        //如果没有经纬度要向高德获取
        if(ObjectUtil.isEmpty(addressBookUpsertReqDTO.getLocation())){
            LocationResDTO locationResDTO = mapApi.getLocationByAddress(completeAddress);
            String location = locationResDTO.getLocation();//location就是经纬度
            addressBookUpsertReqDTO.setLocation(location);
        }
        //经纬度设置
        if(ObjectUtil.isNotEmpty(addressBookUpsertReqDTO.getLocation())){
            addressBook.setLon(NumberUtils.parseDouble(addressBookUpsertReqDTO.getLocation().split(",")[0]));
            addressBook.setLat(NumberUtils.parseDouble(addressBookUpsertReqDTO.getLocation().split(",")[1]));
        }
        baseMapper.insert(addressBook);
    }

    @Override
    public PageResult<AddressBookResDTO> page(AddressBookPageQueryReqDTO addressBookPageQueryReqDTO) {
        Page<AddressBook> page = PageUtils.parsePageQuery(addressBookPageQueryReqDTO, AddressBook.class);
        LambdaQueryWrapper<AddressBook> queryWrapper = Wrappers.lambdaQuery(AddressBook.class);
        queryWrapper.eq(AddressBook::getUserId, UserContext.currentUserId());
        Page<AddressBook> page1 = addressBookMapper.selectPage(page, queryWrapper);
        return PageUtils.toPage(page1, AddressBookResDTO.class);
    }

    /**
     * 更新地址
     * @param id
     * @param addressBookUpsertReqDTO
     */
    @Override
    public void updateAddress(Long id, AddressBookUpsertReqDTO addressBookUpsertReqDTO) {
        if(1 == addressBookUpsertReqDTO.getIsDefault()){
            cancelDefault(UserContext.currentUserId());
        }
        AddressBook addressBook = BeanUtils.copyProperties(addressBookUpsertReqDTO, AddressBook.class);
        addressBook.setId(id);

        //调用第三方，根据地址获取经纬度坐标
        String completeAddress = addressBookUpsertReqDTO.getProvince() +
                addressBookUpsertReqDTO.getCity() +
                addressBookUpsertReqDTO.getCounty() +
                addressBookUpsertReqDTO.getAddress();
        //远程请求高德获取经纬度
        LocationResDTO locationDto = mapApi.getLocationByAddress(completeAddress);
        //经纬度(字符串格式：经度,纬度),经度在前，纬度在后
        String location = locationDto.getLocation();
        if(StringUtils.isNotEmpty(location)) {
            // 经度
            addressBook.setLon(NumberUtils.parseDouble(locationDto.getLocation().split(",")[0]));
            // 纬度
            addressBook.setLat(NumberUtils.parseDouble(locationDto.getLocation().split(",")[1]));
        }
        baseMapper.updateById(addressBook);
    }

    /**
     * 默认地址
     * @param userId
     * @param id
     * @param flag
     */
    @Override
    public void setDefaultAddress(Long userId, Long id, Integer flag) {
        if(flag == 1){
            cancelDefault(userId);
        }
        AddressBook addressBook = new AddressBook();
        addressBook.setId(id);
        addressBook.setIsDefault(flag);
        baseMapper.updateById(addressBook);

    }

    /**
     * 获取默认地址
     * @param userId
     * @return
     */
    @Override
    public AddressBook getDefaultAddress(Long userId) {
        AddressBook addressBook = lambdaQuery().eq(AddressBook::getUserId, userId).eq(AddressBook::getIsDefault, 1).one();

        return addressBook;
    }

    /**
     * 取消默认地址
     * @param userId
     */
    private void cancelDefault(Long userId) {
        LambdaUpdateWrapper<AddressBook> updateWrapper = Wrappers.lambdaUpdate(AddressBook.class);
        updateWrapper.set(AddressBook::getIsDefault, 0)
                .eq(AddressBook::getUserId, userId);
        super.update(updateWrapper);
    }
}

