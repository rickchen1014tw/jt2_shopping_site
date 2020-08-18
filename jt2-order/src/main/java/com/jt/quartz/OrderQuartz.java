package com.jt.quartz;

import java.util.Calendar;
import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.OrderMapper;
import com.jt.pojo.Order;

//准备订单定时任务
@Component
public class OrderQuartz extends QuartzJobBean{

	@Autowired
	private OrderMapper orderMapper;

	/**当用户订单提交30分钟后,如果还没有支付.则交易关闭
	 * 现在时间 - 订单创建时间 > 30分钟  则超时
	 * new date - 30 分钟 > 订单创建时间
	 */
	/**
	 * 業務思想:
	 * 當用戶30分鐘內，沒有支付則將狀態改為6交易關閉
	 * sql: update tb_order set status=6, updated=#{date} 
	 *      where status=1 and created < 現在時間-30分
	 * 如果"取得現在的時間"操作放在數據庫會影響效能
	 * 所以我們盡可能把這樣的操作放在服務器
	 */
	//當程序執行時 執行該方法
	@Override
	@Transactional
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		//设定30分钟超时
		Calendar calendar = Calendar.getInstance();	//獲取一個當前時間的Calendar對象
		calendar.add(Calendar.MINUTE, -30); //把Calendar對象的分減去30分
		Date timeoutDate = calendar.getTime();
		Order order = new Order();
		order.setStatus(6)
		     .setUpdated(new Date());
		UpdateWrapper<Order> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("status", "1").lt("created",timeoutDate);
		orderMapper.update(order, updateWrapper);
		System.out.println("定時任務執行成功!");
	}
}
