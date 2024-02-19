package com.se.onlinelibrary.service.impl.bookImpls;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.se.onlinelibrary.bean.book.Reserve;
import com.se.onlinelibrary.mapper.ReserveMapper;
import com.se.onlinelibrary.service.bookServices.ReserveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ReserveServiceImpl extends ServiceImpl<ReserveMapper, Reserve> implements ReserveService {

    // 在线预约检查
    public boolean reserveCheckTwo(int userId){
        QueryWrapper<Reserve> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Reserve::getUserId, userId)
                .eq(Reserve::getStatus, "已预约");
        List<Reserve> reserveList = list(queryWrapper);
        return reserveList.size() < 5;
    }

    // 在线预约：预约信息记录
    public void reserve(int copyId, int userId){

        Reserve reserve = new Reserve();
        reserve.setCopyId(copyId);
        reserve.setUserId(userId);
        reserve.setStatus("已预约");

        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        reserve.setTime(timestamp);

        save(reserve);
    }

    // 用户查询预约信息
    public List<Reserve> allReservation(int userId){
        QueryWrapper<Reserve> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Reserve::getUserId, userId)
                .eq(Reserve::getStatus, "已预约");
        return list(queryWrapper);
    }

    // 查询所有未取书的预约信息
    public List<Reserve> reservationCheck(int userId){
        QueryWrapper<Reserve> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Reserve::getUserId, userId)
                .eq(Reserve::getStatus, "已预约");
        return list(queryWrapper);
    }

    // 查询是否存在此预约记录
    public boolean cancelCheck(int userId, int copyId){
        Wrapper<Reserve> queryWrapper = new QueryWrapper<Reserve>()
                .eq("user_id", userId)
                .eq("copy_id", copyId)
                .eq("status", "已预约");
        Reserve findReserve = getOne(queryWrapper);
        if(findReserve == null){
            return false;
        }else{
            findReserve.setStatus("已取消");
            update(findReserve, new QueryWrapper<Reserve>().lambda()
                    .eq(Reserve::getUserId, userId)
                    .eq(Reserve::getCopyId, copyId)
                    .eq(Reserve::getStatus, "已预约"));
            return true;
        }
    }

    // 预约取书：查询是否可以被该用户取走
    public boolean reservationBorrowCheckTwo(int reserveId, int userId){
        Wrapper<Reserve> queryWrapper = new QueryWrapper<Reserve>()
                .eq("reserve_id", reserveId)
                .eq("user_id", userId);
        Reserve findReserve = getOne(queryWrapper);
        if(findReserve == null){
            return false;
        }else{
            findReserve.setStatus("已取走");
            update(findReserve, new QueryWrapper<Reserve>().lambda()
                    .eq(Reserve::getReserveId, reserveId)
                    .eq(Reserve::getUserId, userId));
            return true;
        }
    }
}




