package com.example.xsservergood.serviceImpl;

import org.springframework.stereotype.Service;

import com.example.xs.bean.Product;
import com.example.xs.good.IProductService;
import com.example.xsservergood.aop.RPCService;
@Service
@RPCService(value = IProductService.class)
public class ProductServiceImpl implements IProductService{
    @Override
    public Product queryById(long id) {
        Product product = new Product();
        product.setId(id);
        product.setName("water");
        product.setPrice(1.0);
        return product;
    }

	@Override
	public String hello(String name) {
		return "hello "+name;
	}
}
