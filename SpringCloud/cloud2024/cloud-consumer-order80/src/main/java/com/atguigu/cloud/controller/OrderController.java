package com.atguigu.cloud.controller;

import com.atguigu.cloud.entities.PayDTO;
import com.atguigu.cloud.resp.ResultData;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@Tag(name = "客户服务模块", description = "客户服务CRUD")
public class OrderController {
    //    public static final String Paymentsrv_URL = "http://localhost:8001";//先写死，硬编码
    //如果使用微服务名称，默认是有多个服务，必须使用负载均衡
    public static final String Paymentsrv_URL = "http://cloud-payment-service"; //服务注册中心上的微服务名称
    // consule默认支持负载均衡，现在是通过微服务名字去访问的，所以需要通过在restTemplate注入的时候使用@LoadBalance注解
    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/consumer/pay/add")
    public ResultData addOrder(PayDTO payDTO) {
        return restTemplate.getForObject(Paymentsrv_URL + "/pay/add", ResultData.class, payDTO);
    }

    @GetMapping("/consumer/pay/get/{id}")
    public ResultData getPayInfo(@PathVariable("id") String id) {
        return restTemplate.getForObject(Paymentsrv_URL + "/pay/get/" + id, ResultData.class, id);
    }

    /**
     * 测试负载均衡，默认是轮训机制
     * @return
     */
    @GetMapping(value = "/consumer/pay/get/info")
    private String getInfoByConsul()
    {
        return restTemplate.getForObject(Paymentsrv_URL + "/pay/get/info", String.class);
    }

    /**
     * 获取consul中上线的服务
     */
    @Resource
    private DiscoveryClient discoveryClient;
    @GetMapping("/consumer/discovery")
    public String discovery()
    {
        List<String> services = discoveryClient.getServices();
        for (String element : services) {
            System.out.println(element);
        }

        System.out.println("===================================");

        List<ServiceInstance> instances = discoveryClient.getInstances("cloud-payment-service");
        for (ServiceInstance element : instances) {
            System.out.println(element.getServiceId()+"\t"+element.getHost()+"\t"+element.getPort()+"\t"+element.getUri());
        }

        return instances.get(0).getServiceId()+":"+instances.get(0).getPort();
    }
}
