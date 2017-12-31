import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zhaoyh on 2017/12/31.
 */
public class Processor implements Runnable {
    private Socket socket;
    private static HashMap<String,Method> remap=new HashMap<>();
    public Processor(Socket socket) {
        this.socket = socket;
    }
    static {
        Class  config= DemoHandler.class;
        Method[] methods = config.getDeclaredMethods();
        for(Method method:methods){
            Mapping mapping = method.getAnnotation(Mapping.class);
            String s = mapping.reqUrl();
            remap.put(s,method);
        }
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            //don't be curious. it will be block because of http1.1 keep alive .
            // i want to  complete this task more easily
            // so  i don't use nio to deal with this problem or other ideas
            // by just open a bigger buff to read all of the request head .
            byte[] buff=new byte[1024];
            int len=-1;
            StringBuffer stringBuffer=new StringBuffer();
            len=inputStream.read(buff);
            stringBuffer.append(new String(buff,0,len));
            System.out.println("get request info from client is "+stringBuffer.toString());
            String reqStr=stringBuffer.toString();
            String[] requests=reqStr.split("\r\n");
            //get request path don't be nervous just simply http knowledge
            HashMap<String,String> parameter=new HashMap<>();
            String reqstr=requests[0].split(" ")[1];
            String uri="";
            int paraIndex = reqstr.indexOf("?");
            if(paraIndex==-1){
                uri=reqstr;
            }else{
                uri=reqstr.substring(0,paraIndex);
                String parastr=reqstr.substring(paraIndex+1);
                String[] parameterPair=null;
                if(parastr.indexOf("&")==-1){
                    parameterPair= new String[]{parastr};
                }else{
                    parameterPair=parastr.split("&");
                }
                for(String para:parameterPair){
                    String[] kvpair = para.split("=");
                    parameter.put(kvpair[0],kvpair[1]);
                }
            }
            System.out.println("with parameters "+parameter);
            System.out.println("the request uri is "+uri);
            PrintWriter out=new PrintWriter(outputStream);
            //now just to find which is the right choose
            Method method = remap.get(uri);
            if(method==null){
                ResponseUtil.response(out,"404","no uri","i can't find this");
            }else{
                Parameter[] methodParameters = method.getParameters();
                ArrayList mp=new ArrayList();
                mp.add(out);
                if(methodParameters.length>1){
                    for(int i=1;i<methodParameters.length;i++){
                        Parameter p = methodParameters[i];
                        Field f = p.getAnnotation(Field.class);
                        String pname=f.value();
                        String methodP = parameter.get(pname);
                        mp.add(methodP);
                    }
                    method.invoke(DemoHandler.class.newInstance(),mp.toArray());
                }else{
                    method.invoke(DemoHandler.class.newInstance(),out);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
