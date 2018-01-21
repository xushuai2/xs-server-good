package com.example.xsservergood.serviceImpl;

import org.springframework.stereotype.Service;

import com.example.xs.bean.Product;
import com.example.xs.good.ITestXuRpcService;
import com.example.xsservergood.aop.RPCService;
@Service
@RPCService(value = ITestXuRpcService.class)
public class TestXuRpcServiceImpl implements ITestXuRpcService {

	@Override
	public Product update(Product pro) {
		pro.setName(pro.getName() + "used by xushuai! ");
		pro.setPrice(300);
		return pro;
	}

}
