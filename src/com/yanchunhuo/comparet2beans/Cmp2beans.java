package com.yanchunhuo.comparet2beans;


import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * compare two java beans with java Reflection
 * @author yanchunhuo
 * 
 */
public class Cmp2beans {
	
	/**
	 * compare two java beans with java Reflection.Support regular string comparison and only compare string in two beans. 
	 * @param source 
	 * @param target
	 * @param supportRegular
	 * @param onlyCmpString : only compare string in two beans
	 * @return
	 * @throws Exception
	 */
	public static <T> boolean compare(T source, T target,boolean supportRegular,boolean onlyCmpString) throws Exception {

        if(source == null||target==null) {
            throw new Exception("Parameters can not contain null");
        }
        Class sourceClass=source.getClass();//���Class����Class������Ի������������ͷ�����ע�������public�����Ժͷ�����
        Field[] fields =sourceClass.getFields();//������е�����
        for (int i = 0; i < fields.length; i++) {
        	boolean isPass=false;
        	String fieldType=fields[i].getType().getSimpleName();//�õ������Ե�����
        	
        	switch (fieldType.toLowerCase()) {
				case "string":
					isPass=compare2String((String)fields[i].get(source), (String)fields[i].get(target), supportRegular);
					//�Ƚϲ�ͨ�����׳��쳣
					if (!isPass) {
						throw new Exception("Class["+source.getClass().getSimpleName()+"] Attributes["+fields[i].getName()+"] Mismatch;source["+fields[i].get(source)+"],target["+fields[i].get(target)+"]");
					}
					break;
					
				case "list":
					if ((List)fields[i].get(source)!=null&&(List)fields[i].get(target)!=null) {
						int sourceListSize=((List)fields[i].get(source)).size();
						int targetListSize=((List)fields[i].get(target)).size();
						if (sourceListSize!=targetListSize) {
							//���Ȳ�һ�¿϶������
							isPass=false;
						}else {
							if (sourceListSize>0) {
								for (int j = 0; j <sourceListSize; j++) {
									Object tmpSourceObject=((List)fields[i].get(source)).get(j);
									Object tmpTargetObject=((List)fields[i].get(target)).get(j);
									isPass=compare(tmpSourceObject,tmpTargetObject, supportRegular,onlyCmpString);
								}
							}else {//����Ϊ0�����
								isPass=true;
							}
						}
					}else if ((List)fields[i].get(source)==null&&(List)fields[i].get(target)==null){//��Ϊnull�����
						isPass=true;
					}else {//һ��Ϊnull,һ����Ϊnull�����
						isPass=false;
					}
					
					//�Ƚϲ�ͨ�����׳��쳣
					if (!isPass) {
						throw new Exception("Class["+source.getClass().getSimpleName()+"] Attributes["+fields[i].getName()+"] Mismatch;source["+fields[i].get(source)+"],target["+fields[i].get(target)+"]");
					}
					
					break;
					
				case "int":
					if (onlyCmpString) {
						break;
					}
					//�Ƚϲ�ͨ�����׳��쳣
					int sourceInt=(Integer)fields[i].get(source);
					int targetInt=(Integer)fields[i].get(target);
					if (!(sourceInt==targetInt)) {
						throw new Exception("Class["+source.getClass().getSimpleName()+"] Attributes["+fields[i].getName()+"] Mismatch;source["+fields[i].get(source)+"],target["+fields[i].get(target)+"]");
					}
					break;
				
				case "double":
					if (onlyCmpString) {
						break;
					}
					//�Ƚϲ�ͨ�����׳��쳣
					double sourceDouble=(double)fields[i].get(source);
					double targetDouble=(double)fields[i].get(target);
					if (!(sourceDouble==targetDouble)) {
						throw new Exception("Class["+source.getClass().getSimpleName()+"] Attributes["+fields[i].getName()+"] Mismatch;source["+fields[i].get(source)+"],target["+fields[i].get(target)+"]");
					}
					break;
					
				case "float":
					if (onlyCmpString) {
						break;
					}
					//�Ƚϲ�ͨ�����׳��쳣
					float sourceFloat=(Float)fields[i].get(source);
					float targetFloat=(Float)fields[i].get(target);
					if (!(sourceFloat==targetFloat)) {
						throw new Exception("Class["+source.getClass().getSimpleName()+"] Attributes["+fields[i].getName()+"] Mismatch;source["+fields[i].get(source)+"],target["+fields[i].get(target)+"]");
					}
					break;
				
				default:
					Object tmpSourceObject=fields[i].get(source);
					Object tmpTargetObject=fields[i].get(target);
					if (tmpSourceObject!=null&&tmpTargetObject!=null) {
						compare(fields[i].get(source), fields[i].get(target),supportRegular,onlyCmpString);
					}
					break;
			}
		}
        
        return true;

    }
	
	/**
	 * compare two string.Support regular.
	 * @param source
	 * @param target
	 * @param supportRegular
	 * @return
	 */
	private static boolean compare2String(String source,String target,boolean supportRegular){
		boolean result=false;
		if (source!=null&&target!=null) {
			if (supportRegular) {
				Pattern pattern=Pattern.compile(target);
				Matcher matcher=pattern.matcher(source);
				result=matcher.find();
			}else {
				result=source.equalsIgnoreCase(target);
			}
		}else if (source==null&&target==null) {
			result=true;
		}else {
			result=false;
		}
		return result;
	}
	
	
}
