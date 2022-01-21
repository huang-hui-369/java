package lhn.annotation.processor;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;

import lhn.annotation.demo.entity.*;

public class MyProcessor extends AbstractProcessor {

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		//定义一个文件输出流，用于生成额外的文件
        PrintStream ps = null;
        try{
            //遍历每个被@Entity修饰的class文件,使用RoundEnvironment来获取Annotation信息
            for( Element t : roundEnv.getElementsAnnotatedWith(Entity.class)){
                //获取正在处理的类名
                Name clazzName = t.getSimpleName();
                //获取类定义前的@Entity Annotation
                Entity table = t.getAnnotation(Entity.class);
                //创建文件输出流
                String fileName =clazzName+".txt";
                ps = new PrintStream(new FileOutputStream(fileName));
                 // 执行输出
                 ps.print("class name=" + clazzName);
                 // 输出per的table()的值
                 ps.println(" table name=" + table.tableName() );
                 //获取@Entity修改类的各个属性字段。t.getEnclosedElements()获取该Elemet里定义的所有程序单元
                 for(Element ele : t.getEnclosedElements()){
                     
                     //只处理成员变量上的Annotation，ele.getKind()返回所代表的的程序单元
                     if( ele.getKind() == ElementKind.FIELD){
                        //被id注解修饰的字段
                    	 Column column= ele.getAnnotation(Column.class);
                         if( column != null){
                             String cname =column.cname();
                             String ctype =column.ctype();
                             boolean isPk = column.pk();
                             // 执行输出
                               ps.print(" column name=" + cname);
                               ps.print(" column type=" + ctype);
                               ps.println(" column is pk =" + isPk);
                         }
                     }
                 }// end for
                 ps.println("");
            }// end for 
            
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(ps!=null){
                try{
                    ps.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        return true;
	}
	
	 /** 
     * 这里必须指定，这个注解处理器是注册给哪个注解的。注意，它的返回值是一个字符串的集合，包含本处理器想要处理的注解类型的合法全称 
     * @return  注解器所支持的注解类型集合，如果没有这样的类型，则返回一个空集合 
     */  
    @Override  
    public Set<String> getSupportedAnnotationTypes() {  
        Set<String> annotataions = new LinkedHashSet<String>();  
        annotataions.add(Entity.class.getCanonicalName());  
        annotataions.add(Column.class.getCanonicalName());  
        return annotataions;  
    }  
  
    /** 
     * 指定使用的Java版本，通常这里返回SourceVersion.latestSupported()，默认返回SourceVersion.RELEASE_6 
     * @return  使用的Java版本 
     */
    @Override  
    public SourceVersion getSupportedSourceVersion() {  
        return SourceVersion.latestSupported();  
    }  
	
}
