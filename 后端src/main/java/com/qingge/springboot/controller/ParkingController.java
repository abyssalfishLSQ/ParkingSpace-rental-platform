package com.qingge.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.entity.Parking;
import com.qingge.springboot.entity.User;
import com.qingge.springboot.mapper.ParkingMapper;
import com.qingge.springboot.service.IParkingService;
import com.qingge.springboot.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * <p>
 *  前端控制器
 * </p>
 * @author 李思琪
 * @since 2023-06-09
 */
@RestController
@RequestMapping("/parking")
public class ParkingController {
    @Resource
    private IParkingService parkingService;

    //删除
    @DeleteMapping("/{parkingid}")
    public Result delete(@PathVariable Integer parkingid) {
        return Result.success(parkingService.removeById(parkingid));
    }

    @GetMapping("/{parkingid}")
    public Result findOne(@PathVariable Integer parkingid) {
        return Result.success(parkingService.getById(parkingid));
    }


    @GetMapping("/mypublish") //得到某用户自己发的所有车位信息
    public Result findmypublish() {
        User currentUser = TokenUtils.getCurrentUser();//利用token得到现在的user
        int id = currentUser.getId();
        QueryWrapper<Parking>queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("owner_id",id);//这里对应的是数据库的字段而非Entity里的成员
        queryWrapper.eq("status","spare");//当车位已被预定，发布者不可更改车位信息，这里的处理是只要预定，就不会出现在”我的订单里“
        return Result.success(parkingService.list(queryWrapper));
    }

    @GetMapping("/myorder")//得到某用户自己的订单
    public Result findmyorder(){
        User currentUser = TokenUtils.getCurrentUser();//利用token得到现在的user
        int id = currentUser.getId();
        QueryWrapper<Parking>queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("bookedbywho",id);
        return Result.success(parkingService.list(queryWrapper));

    }

    //新增或修改
    @PostMapping
    public Result save(@RequestBody Parking parking) {
        return Result.success(parkingService.saveOrUpdate(parking));
    }


    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String parkingAddress,
                           @RequestParam(defaultValue = "") String priceNow,
                           @RequestParam(defaultValue = "") String status,
                           @RequestParam(defaultValue = "") String timeslot,
                           @RequestParam(defaultValue = "") String grades)
    {
        QueryWrapper<Parking> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("parkingid");


        if (!"".equals(parkingAddress)) {
            queryWrapper.like("parking_address", parkingAddress);//模糊查询
        }
        if (!"".equals(priceNow))
        {
            double priceNow_ = Double.parseDouble(priceNow);
            queryWrapper.between("price_now",priceNow_-4.0 ,priceNow_);
        }
        if(!"".equals(timeslot))
        {
            queryWrapper.eq("timeslot",timeslot);
        }
        if(!"".equals(grades))
        {
            double grades_=Double.parseDouble(grades);
            queryWrapper.between("grades",grades_-1.0,grades_);
        }
        queryWrapper.eq("status","spare");//添加判断条件，只会出现空闲的车位信息

        return Result.success(parkingService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @GetMapping("/booking")
    public boolean isBooking(@RequestParam String parkingid) {
        int parkingid_=Integer.parseInt(parkingid);
        User currentUser = TokenUtils.getCurrentUser();//利用token得到现在的user
        int id = currentUser.getId();
        Parking parking = parkingService.getById(parkingid_); // 根据ID查询对应的车位对象
        if (parking != null) {
            int cnt1=parking.getWeekUseCnt() +1;
            parking.setWeekUseCnt(cnt1);
            int cnt2=parking.getMonthUseCnt()+1;
            parking.setMonthUseCnt(cnt2);//只要一预定，就把
            parking.setStatus("occupied");
            parking.setBookedbywho(id);
            return parkingService.saveOrUpdate(parking); // 返回更新结果
        }
        return false; // 若未找到对应的Parking对象，则返回false
    }

    @GetMapping("/cancelbooking")
    public boolean cancelBooking(@RequestParam String parkingid){
        int parkingid_=Integer.parseInt(parkingid);
        Parking parking = parkingService.getById(parkingid_); // 根据ID查询对应的车位对象
        if (parking != null) {
            parking.setStatus("spare");
            parking.setBookedbywho(0);
            return parkingService.saveOrUpdate(parking); // 返回更新结果
        }
        return false; // 若未找到对应的Parking对象，则返回false
    }

    @GetMapping("/calculatingrate")
    public boolean calculateRate(@RequestParam String parkingid,
                                 @RequestParam (required = false) Double grades)
    {
        int parkingid_=Integer.parseInt(parkingid);
        Parking parking = parkingService.getById(parkingid_); // 根据ID查询对应的车位对象
       // double rate_=Double.parseDouble(grades);
        double rate_= grades;
        if (parking != null) {
            int cnt = parking.getMonthUseCnt();
            double oldRate = parking.getGrades();
            double newRate = rate_ + cnt*oldRate;
            newRate /=(cnt+1);
            parking.setGrades(newRate);
            return parkingService.saveOrUpdate(parking); // 返回更新结果
        }
        return false; // 若未找到对应的Parking对象，则返回false
    }


}
