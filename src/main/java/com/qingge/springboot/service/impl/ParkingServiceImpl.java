package com.qingge.springboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingge.springboot.entity.Parking;
import com.qingge.springboot.mapper.ParkingMapper;
import com.qingge.springboot.service.IParkingService;
import org.springframework.stereotype.Service;

@Service
public class ParkingServiceImpl extends ServiceImpl<ParkingMapper, Parking> implements IParkingService {

}
