package com.example.xsservergood.eventlistener;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.example.xsservergood.aop.RPCService;
@Component  
public class ContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {
	private Map<String,Object> serviceMap = new HashMap<String,Object>();
	public void onApplicationEvent(ContextRefreshedEvent event) {  
		// 根容器为Spring容器  
        if(event.getApplicationContext().getParent()==null){  
            Map<String,Object> beans = event.getApplicationContext().getBeansWithAnnotation((Class<? extends Annotation>) RPCService.class);  
            for(Map.Entry<String,Object> entry :beans.entrySet()){
                String interfaceName = entry.getValue().getClass().getAnnotation(RPCService.class).value().getName();
                serviceMap.put(interfaceName,entry.getValue());
                System.out.println(interfaceName+"---"+entry.getValue());
            }
        }  
        
        startServer();
	}
	
	public void startServer(){
		try{
            ServerSocket serverSocket = new ServerSocket(8888);
            while(true){
                Socket socket = serverSocket.accept();
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

                //读取网络协议
                String apiClassName = objectInputStream.readUTF();
                String methodName = objectInputStream.readUTF();
                Class[] parameterTypes = (Class[]) objectInputStream.readObject();
                Object[] args4Method = (Object[]) objectInputStream.readObject();

                Class clazz = null;

                //API 到  具体实现  的映射关系
                /*if(apiClassName.equals(IProductService.class.getName())){
                    clazz = ProductService.class;
                }*/
                Object requestBean = serviceMap.get(apiClassName);

                Method method = requestBean.getClass().getMethod(methodName, parameterTypes);
                //Object invoke = method.invoke(clazz.newInstance(), args4Method);
                Object invoke = method.invoke(requestBean, args4Method);
                System.out.println(methodName+"***************"+invoke.toString());

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeObject(invoke);
                objectOutputStream.flush();

                objectInputStream.close();
                objectOutputStream.close();
                socket.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
	}

}
