package com.jzo2o.customer.controller.consumer;

import cn.hutool.core.bean.BeanUtil;
import com.jzo2o.api.customer.dto.response.AddressBookResDTO;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.customer.model.domain.AddressBook;
import com.jzo2o.customer.model.dto.request.AddressBookPageQueryReqDTO;
import com.jzo2o.customer.model.dto.request.AddressBookUpsertReqDTO;
import com.jzo2o.customer.service.IAddressBookService;
import com.jzo2o.mvc.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zwy
 * @version 1.0
 * @description: TODO
 * @date 2024/5/1 13:58
 */
@RestController("consumerAddressBookController")
@RequestMapping("/consumer/address-book")
@Api(tags = "用户端 - 地址簿相关接口")
public class AddressBookController {

    @Autowired
    private IAddressBookService addressBookService;

    @PostMapping
    @ApiOperation("添加地址")
    public void addAddress(@RequestBody AddressBookUpsertReqDTO addressBookUpsertReqDTO) {
        addressBookService.addAddress(addressBookUpsertReqDTO);
    }

    @GetMapping("/page")
    @ApiOperation("分页查询地址")
    public PageResult<AddressBookResDTO> page( AddressBookPageQueryReqDTO addressBookPageQueryReqDTO) {
        return addressBookService.page(addressBookPageQueryReqDTO);
    }

    @GetMapping("/{id}")
    @ApiOperation("地址详情")
    public AddressBookResDTO getAddressById(@PathVariable("id") Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        return BeanUtil.toBean(addressBook, AddressBookResDTO.class);
    }

    @PutMapping("/{id}")
    @ApiOperation("修改地址")
    public void updateAddress(@PathVariable("id") Long id,@RequestBody AddressBookUpsertReqDTO addressBookUpsertReqDTO) {
        addressBookService.updateAddress(id, addressBookUpsertReqDTO);
    }

    @DeleteMapping("/batch")
    @ApiOperation("批量删除地址")
    public void deleteAddress(@RequestBody List<Long> ids) {
        addressBookService.removeByIds(ids);
    }

    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    public void setDefaultAddress(@RequestParam("id") Long id, @RequestParam("userId") Integer flag) {
        Long userId = UserContext.currentUserId();
        addressBookService.setDefaultAddress(userId,id,flag);
    }

    @GetMapping("/defaultAddress")
    @ApiOperation("获取默认地址")
    public AddressBookResDTO getDefaultAddress() {
        Long userId = UserContext.currentUserId();
        AddressBook addressBook = addressBookService.getDefaultAddress(userId);
        return BeanUtil.toBean(addressBook, AddressBookResDTO.class);
    }
}
