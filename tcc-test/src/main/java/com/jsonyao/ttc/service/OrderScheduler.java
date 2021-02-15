package com.jsonyao.ttc.service;

import com.jsonyao.ttc.db142.dao.PaymentMsgMapper;
import com.jsonyao.ttc.db142.model.PaymentMsg;
import com.jsonyao.ttc.db142.model.PaymentMsgExample;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class OrderScheduler {

    @Resource
    private PaymentMsgMapper paymentMsgMapper;

    /**
     * 定时任务轮训处理未发送的支付消息(推方式推支付域消息给订单域)
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void orderNotify() {
        log.info("本地消息轮训任务正在执行...");

        // 1. 查询未发送的本地消息表记录
        PaymentMsgExample paymentMsgExample = new PaymentMsgExample();
        paymentMsgExample.createCriteria().andStatusEqualTo(0);//未发送
        List<PaymentMsg> paymentMsgs = paymentMsgMapper.selectByExample(paymentMsgExample);
        if (paymentMsgs==null || paymentMsgs.size() ==0) return;

        // 2. 处理未发送的消息记录
        for (PaymentMsg paymentMsg : paymentMsgs) {
            Integer orderId = paymentMsg.getOrderId();

            // 3. 请求订单域更新操作接口
            CloseableHttpClient httpClient = null;
            String responseMsg = "";
            try {
                log.info("开始请求订单域操作接口...");
                httpClient = HttpClientBuilder.create().build();

                NameValuePair nameValuePair = new BasicNameValuePair("orderId", orderId+"");
                HttpEntity httpEntity = new UrlEncodedFormEntity(Arrays.asList(nameValuePair));
                HttpPost request = new HttpPost("http://localhost:8080/handleOrder");
                request.setEntity(httpEntity);

                CloseableHttpResponse response = httpClient.execute(request);
                responseMsg = EntityUtils.toString(response.getEntity());
                log.info("请求结果为: " + responseMsg);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // 4. 请求后根据订单域操作结果更新本地消息记录
            if("success".equals(responseMsg)){
                paymentMsg.setStatus(1);//发送成功
                log.info("订单更新成功, 本地消息发送成功!");
            }else {
                paymentMsg.setFalureCnt(paymentMsg.getFalureCnt() + 1);
                if(paymentMsg.getFalureCnt() >= 5) {
                    paymentMsg.setStatus(2);// 失败
                    log.info("订单更新失败, 本地消息发送失败! 失败原因: 已重新发送了"+ paymentMsg.getFalureCnt() +"次, 超出了最大重试次数!");
                }else {
                    log.info("订单更新失败, 本地消息开始重新发送...");
                }
            }
            paymentMsg.setUpdateTime(new Date());
            paymentMsg.setUpdateUser(0);//系统更新
            paymentMsgMapper.updateByPrimaryKey(paymentMsg);
        }
    }
}
